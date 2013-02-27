require "savon"
require "nokogiri"

class ServicesController < ApplicationController
  
  # GET /users
  # GET /users.json
  def index
    @services = Services.where(:owner_id => current_service_owner)
  end
   
  # Show user's services
  def show    
    @service = Services.find(params[:id])    
    if !@service.blank?    
      wsdl_file = File.new(@service.wsdl_url.gsub("file://", ""), "r")
      if wsdl_file
        @wsdl_content = File.read(wsdl_file)
      end
    end
  end 
  
  # Update service
  def update
    @service = Services.find(params[:id])
    new_service_url = params[:wsdl_input]
    
    if !new_service_url.blank?              
      updateService(@service.wsdl_url, new_service_url)                       
    end
    
    redirect_to "/users/#{current_service_owner.id}"
  end
  
  
  # Delete service
  def destroy
    @service = Services.find(params[:id])    
    
    if !@service.blank?              
      deleteService(@service.wsdl_url)                       
    end
    
    redirect_to "/users/#{current_service_owner.id}"
  end
  
  # Update service
  private
  def updateService(oldServiceURI, newServiceURI)
    begin
      client = Savon::Client.new("http://sesa.sti2.at:8080/registration-webservice/services/register?wsdl")
        response = client.request :update do
          soap.body do |xml|
            xml.oldServiceURI(oldServiceURI)
            xml.newServiceURI(newServiceURI)
          end
        end
        
        if response.success? && !response.soap_fault?
          output = response.xpath("//return/text()");
          flash[:notice] = "The update was succesfull. The service identifier is: <b> " + output.to_s + "</b>";
          
          @service.updated_at = Time.now
          @service.wsdl_url = newServiceURI
          @service.save!
        else
          flash[:alert] = "The update has unsuccesfull."  + response.soap_fault          
        end        
    rescue => e
      flash[:alert] = "Update Process failed, through exception: " + e.to_s
    end
  end
  
  # Delete service
  private
  def deleteService(serviceURI)
    begin
      client = Savon::Client.new("http://sesa.sti2.at:8080/registration-webservice/services/register?wsdl")
        response = client.request :delete do
          soap.body do |xml|
            xml.serviceURI(serviceURI)
          end
        end
        
        if response.success? && !response.soap_fault?          
          flash[:notice] = "The service was succesfully deleted.";          
          @service.destroy
        else
          flash[:alert] = "The delete has unsuccesfull."  + response.soap_fault          
        end        
    rescue => e
      flash[:alert] = "Delete Process failed, through exception: " + e.to_s
    end
  end
   
end