
java_import Java::at.sti2.msee.discovery.core.DiscoveryService
java_import Java::org.openrdf.rio.RDFFormat
#java_import Java::java.net.URI

class Dashboards::DiscoveriesController < ApplicationController

  def index
    method = params[:method]

    categories = DiscoveryService.getServiceCategories
    logger.debug "categories: #{categories}"

    if method == "lookup"
      lookup(params[:namespace], params[:operation])
    elsif method == "discover"
      discover(params[:categoryList])
    elsif method == "iserve"
      getIServeModel(params[:serviceID])
    end
  end

  # The lookup method call
  private
  def lookup(namespace, operation)

    rdfformat = RDFFormat::RDFXML

    begin
      discovery = DiscoveryService.new

      namespaceURI = java.net.URI.new(namespace)

      result = discovery.lookup(namespaceURI, operation,rdfformat)
      logger.debug "discovery lookup: #{result}"

      @lookup_output = result
      @notice = "The discovery was succesfull.";

    rescue => e
      @error = "Discovery Process failed, through exception: " + e.to_s
    end
  end

  # The discover method call
  private
  def discover(categories)

    rdfformat = RDFFormat::RDFXML

    begin

      discovery = DiscoveryService.new
      result = discovery.discover(categories, rdfformat)

      logger.debug "asdf asdf asdf asdf categories: #{categories} #{categories==nil}";
      logger.debug "discovery response #{result} + #{result.class}"

      @discover_output = result
      @notice = "The discovery was succesfull.";

    rescue => e
      @error = "Discovery Process failed, through exception: " + e.to_s
    end
  end

  # The IServe method call
  private
  def getIServeModel(serviceID)

    rdfformat = RDFFormat::RDFXML

    begin

      discovery = DiscoveryService.new
      result = discovery.getIServeModel("\""+serviceID+"\"", rdfformat)
      logger.debug "IServeModel Response #{result} + #{result.class}"

      @iserve_output = result;
      @notice = "The discovery was succesfull.";

    rescue => e
      @error = "Discovery Process failed, through exception: " + e.to_s
    end
  end

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
