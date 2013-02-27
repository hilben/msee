require "savon"
require "nokogiri"

java_import Java::at.sti2.msee.registration.core.management.RegistrationWSDLToTriplestoreWriter
java_import Java::at.sti2.msee.registration.api.exception.ServiceRegistrationException

class Dashboards::RegistrationController < ApplicationController
  before_filter :authenticate_service_owner!

  def index
    input = params[:wsdl_input]


    writer = RegistrationWSDLToTriplestoreWriter.new

    if !input.blank?
      begin
        s = writer.transformWSDLtoTriplesAndStoreInTripleStore(input) 
        puts "Return string : #{s}"
        @notice = s
      rescue ServiceRegistrationException => e
        puts "Failed to register service: #{e.message}"
        @error = "Failed to register service: #{e.message}"
      end         
    end
  end
end
