
java_import Java::at.sti2.msee.discovery.core.DiscoveryService
java_import Java::org.openrdf.rio.RDFFormat
#java_import Java::java.net.URI

class Dashboards::DiscoveriesController < ApplicationController

  #@@discovery_url = "http://localhost:9090/discovery-webservice/services/discovery?wsdl"
  @@discovery_url = "http://sesa.sti2.at:8080/discovery-webservice/services/discovery?wsdl"

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

  # The lookup method call
  private
  def lookup(namespace, operation)

    rdfformat = RDFFormat::RDFXML

    begin
      discovery = DiscoveryService.new

      namespaceURI = java.net.URI.new(namespace)


      result = discovery.lookup(namespaceURI, operation,rdfformat)
      logger.info "discovery lookup: #{result}"


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
      logger.info "discovery response #{result} + #{result.class}"

      @discover_output = result
      @notice = "The discovery was succesfull.";

    rescue => e
      @error = "Discovery Process failed, through exception: " + e.to_s
    end
  end

  # The IServe method call
  private
  def getIServeModel(serviceID)
    begin
      client = Savon::Client.new(@@discovery_url)
      response = client.request :getIServeModel do
        soap.body do |xml|
          xml.serviceID(serviceID)
        end
      end

      if response.success? && !response.soap_fault?
        @iserve_output = response.xpath("//return");
        @notice = "The discovery was succesfull.";
      else
        @error = "The discovery has unsuccesfull."  + response.soap_fault
      end
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
