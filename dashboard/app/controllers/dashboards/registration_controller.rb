

java_import Java::at.sti2.msee.triplestore.ServiceRepositoryConfiguration
java_import Java::at.sti2.msee.triplestore.ServiceRepositoryFactory
java_import Java::at.sti2.msee.triplestore.impl.SesameServiceRepositoryImpl
java_import Java::at.sti2.msee.registration.api.exception.ServiceRegistrationException
java_import Java::at.sti2.msee.registration.core.configuration.ServiceRegistrationConfiguration
java_import Java::at.sti2.msee.registration.core.ServiceRegistrationImpl



class Dashboards::RegistrationController < ApplicationController
  before_filter :authenticate_service_owner!

  def index
    input = params[:wsdl_input]

    serverEndpoint = "http://sesa.sti2.at:8080/openrdf-sesame"
    repositoryId = "msee"

    begin
      repositoryConfiguration = ServiceRepositoryConfiguration.new
      repositoryConfiguration.setRepositoryID(repositoryId)
      repositoryConfiguration.setServerEndpoint(serverEndpoint)

      serviceRepository = ServiceRepositoryFactory.newInstance(repositoryConfiguration);
      serviceRepository.init()
      writer = ServiceRegistrationImpl.new(serviceRepository);

    rescue Exception => e
      logger.debug "#{e.message}"
      puts "Failed "
    end
    #writer = RegistrationWSDLToTriplestoreWriter.new

    if !input.blank?
      begin
        s = writer.register(input)
        puts "Return string : #{s}"
        @notice = s
      rescue Exception => e
        puts "Failed to register service: #{e.message}"
        @error = "Failed to register service: #{e.message}"
      end
    end
  end
end
