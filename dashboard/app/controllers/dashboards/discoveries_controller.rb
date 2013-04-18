
include Java
java_import Java::at.sti2.msee.discovery.core.DiscoveryServiceImpl
java_import Java::at.sti2.msee.discovery.api.webservice.Discovery
java_import Java::at.sti2.msee.discovery.api.webservice.DiscoveryException
java_import Java::at.sti2.msee.discovery.core.ServiceDiscoveryFactory
java_import Java::at.sti2.msee.discovery.core.common.ServiceDiscoveryConfiguration
java_import Java::at.sti2.msee.triplestore.ServiceRepositoryConfiguration
java_import Java::at.sti2.msee.triplestore.impl.SesameServiceRepositoryImpl
#java_import Java::java.net.URI

class Dashboards::DiscoveriesController < ApplicationController

  def index
    method = params[:method]


    if method == "lookup"
      lookup(params[:namespace], params[:operation])
    elsif method == "discover"
      discover(params[:categoryList])
    elsif method == "iserve"
      getIServeModel(params[:serviceID])
    end
  end

  def getCategoriesOfServiceIDForAutoCompleteBox

    #obtain the categories
    categories = Array.new(1) { "http://msee.sti2.at/categories#business"}
    logger.debug "categories: #{categories}"

    jsonAutoCompleteData = Array.new

    categories.each do |category|
      entry = Hash.new
      entry[:key] = category
      entry[:value] = category

      strIndex = category.index("#")

      cutDownCategory = category[strIndex+1, (category.length - 1) - strIndex]
      entry[:value] = cutDownCategory

      jsonAutoCompleteData.push(entry)
    end


    logger.debug "JSON RETURN: #{jsonAutoCompleteData}"

    respond_to do |format|
      format.html  {render :json => jsonAutoCompleteData}
      format.js  {render :json => jsonAutoCompleteData}
    end
  end

  # # The lookup method call
  # private
  # def lookup(namespace, operation)

  #   rdfformat = RDFFormat::RDFXML

  #   begin
  #     discovery = DiscoveryService.new

  #     namespaceURI = java.net.URI.new(namespace)

  #     result = discovery.lookup(namespaceURI, operation,rdfformat)
  #     logger.debug "discovery lookup: #{result}"

  #     @lookup_output = result
  #     @notice = "The discovery was succesfull.";

  #   rescue => e
  #     @error = "Discovery Process failed, through exception: " + e.to_s
  #   end
  # end

  # The discover method call
  private
  def discover(categories)



    begin

      repositoryId = "http://sesa.sti2.at:8080/openrdf-sesame"
      serverEndpoint = "msee"

      discoveryConfiguration = ServiceDiscoveryConfiguration.new

      repositoryConfiguration = ServiceRepositoryConfiguration.new
      repositoryConfiguration.setRepositoryID(repositoryId)
      repositoryConfiguration.setServerEndpoint(serverEndpoint)
      discoveryConfiguration.setRepositoryConfiguration(repositoryConfiguration)

      serviceRepository = SesameServiceRepositoryImpl.new(repositoryConfiguration)

      discovery = DiscoveryServiceImpl.new(serviceRepository)

      result = discovery.discover(categories)

      logger.debug "asdf asdf asdf asdf categories: #{categories} #{categories==nil}";
      logger.debug "discovery response #{result} + #{result.class}"

      @discover_output = result
      @notice = "The discovery was succesfull.";

    rescue => e
      @error = "Discovery Process failed, through exception: " + e.to_s
    end
  end

  # # The IServe method call
  # private
  # def getIServeModel(serviceID)

  #   rdfformat = RDFFormat::RDFXML

  #   begin

  #     discovery = DiscoveryService.new
  #     result = discovery.getIServeModel("\""+serviceID+"\"", rdfformat)
  #     logger.debug "IServeModel Response #{result} + #{result.class}"

  #     @iserve_output = result;
  #     @notice = "The discovery was succesfull.";

  #   rescue => e
  #     @error = "Discovery Process failed, through exception: " + e.to_s
  #   end
  # end

  private
  def getBody(categories)
    body = ""
    categories.each do |category|
      body += getCategories(category)
    end
    return body
  end

  private
  def getCategories(category)
    return "<categoryList>" + category + "</categoryList>"
  end
end
