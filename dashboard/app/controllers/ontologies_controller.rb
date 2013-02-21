require "savon"
require "nokogiri"

class OntologiesController < ApplicationController
  # GET /ontologies
  # GET /ontologies.json
  def index
    @ontologies = Ontology.where(:user_id => current_service_owner)
  end

  # GET /ontologies/1
  # GET /ontologies/1.json
  def show
    @ontology = Ontology.find(params[:id])     
    if !@ontology.blank?    
      ontology_file = File.new(@ontology.ontology_url.gsub("file://", ""), "r")
      if ontology_file
        @ontology_content = File.read(ontology_file)
      end
    end
  end
  
  # POST /ontologies/add  
  def create
    ontology_url = params[:ontology_url]           
    addOntology(ontology_url)
    
    redirect_to "/users/#{current_service_owner.id}"      
  end  
  
  # PUT /ontologies/1
  # PUT /ontologies/1.json
  def update
    @ontology = Ontology.find(params[:id])
    new_ontology_url = params[:ontology_url]
    if !new_ontology_url.blank?              
      updateOntology(@ontology.ontology_url, new_ontology_url)                       
    end
    
    redirect_to "/users/#{current_service_owner.id}"
  end

  # DELETE /ontologies/1
  # DELETE /ontologies/1.json
  def destroy
    @ontology = Ontology.find(params[:id])
    
    if !@ontology.blank?              
      deleteOntology(@ontology.ontology_url)                       
    end
    
    redirect_to "/users/#{current_service_owner.id}"
  end
  
  # Add ontology
  private
  def addOntology(ontologyURL)
    begin
      client = Savon::Client.new("http://sesa.sti2.at:8080/management-webservice/services/manamentTesting?wsdl")
        response = client.request :addOntology do
          soap.body do |xml|
            xml.ontologyURL(ontologyURL)
          end
        end
        
        if response.success? && !response.soap_fault?          
          flash[:notice] = "The ontology was succesfully add.";        
          @ontology = Ontology.new  
          @ontology.ontology_url = ontologyURL      
          @ontology.user_id = current_service_owner
          @ontology.save
        else
          flash[:alert] = "The ontology adding has unsuccesfull."  + response.soap_fault          
        end        
    rescue => e
      flash[:alert] = "Adding Process failed, through exception: " + e.to_s
    end
  end
  
  # Update ontology
  private
  def updateOntology(oldOntologyURL, newOntologyURL)
    begin
      client = Savon::Client.new("http://sesa.sti2.at:8080/management-webservice/services/manamentTesting?wsdl")
        response = client.request :updateOntology do
          soap.body do |xml|
            xml.oldOntologyURL(oldOntologyURL)
            xml.newOntologyURL(newOntologyURL)
          end
        end
        
        if response.success? && !response.soap_fault?
          output = response.xpath("//return/text()");
          flash[:notice] = "The update was succesfull. The ontology identifier is: <b> " + output.to_s + "</b>";
          
          @ontology.updated_at = Time.now
          @ontology.ontology_url = newOntologyURL
          @ontology.save
        else
          flash[:alert] = "The update has unsuccesfull."  + response.soap_fault          
        end        
    rescue => e
      flash[:alert] = "Update Process failed, through exception: " + e.to_s
    end
  end
  
  # Delete ontology
  private
  def deleteOntology(ontologyURL)
    begin
      client = Savon::Client.new("http://sesa.sti2.at:8080/management-webservice/services/manamentTesting?wsdl")
        response = client.request :deleteOntology do
          soap.body do |xml|
            xml.ontologyURL(ontologyURL)
          end
        end
        
        if response.success? && !response.soap_fault?          
          flash[:notice] = "The ontology was succesfully deleted.";          
          @ontology.destroy
        else
          flash[:alert] = "The delete has unsuccesfull."  + response.soap_fault          
        end        
    rescue => e
      flash[:alert] = "Delete Process failed, through exception: " + e.to_s
    end
  end
end
