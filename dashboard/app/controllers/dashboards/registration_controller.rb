require "savon"
require "nokogiri"

class Dashboards::RegistrationController < ApplicationController
  before_filter :authenticate_service_owner!
  
  @@registration_webservice = "http://localhost:8080/registration-webservice/services/register?wsdl"
  @@registration_webservice = "http://sesa.sti2.at:8080/registration-webservice/services/register?wsdl"

  def index
    input = params[:wsdl_input]

    if !input.blank?            
      register(input)
    end
  end
  
  private
    def register(input)      
        begin
              client = Savon::Client.new(@@registration_webservice)
                  response = client.request :register do
                  soap.body do |xml|
                      xml.wsdlURL(input)
                  end
                end
                if response.success? && !response.soap_fault?
                  output = response.xpath("//return");
                  @notice = "The registration was succesfull. The service identifier is: <b> " + output.to_s + "</b>";
                  
                  #Save the service URI and the service owner.
                  service = Services.where(:wsdl_url => input, :owner_id => current_service_owner)[0]
                  if service.nil?                  
                    service = Services.new
                  end
                  
                  service.wsdl_url = input
                  service.owner_id = current_service_owner                  
                  service.save!
                  
                else
                   @error = "The registration has unsuccesfull."  + response.soap_fault
                end
          rescue => e
              @error = "Registration Process failed, through exception: " + e.to_s
          end          
    end
end
