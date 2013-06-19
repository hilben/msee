java_import Java::at.sti2.msee.discovery.core.DiscoveryServiceImpl
java_import Java::at.sti2.msee.discovery.api.webservice.Discovery
java_import Java::at.sti2.msee.discovery.api.webservice.DiscoveryException
java_import Java::at.sti2.msee.discovery.core.ServiceDiscoveryFactory
java_import Java::at.sti2.msee.discovery.core.common.ServiceDiscoveryConfiguration
java_import Java::at.sti2.msee.triplestore.ServiceRepositoryConfiguration
java_import Java::at.sti2.msee.triplestore.impl.SesameServiceRepositoryImpl



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
    serverEndpoint = "http://sesa.sti2.at:8080/openrdf-sesame"
    repositoryId = "msee"

    repositoryConfiguration = ServiceRepositoryConfiguration.new
    repositoryConfiguration.setRepositoryID(repositoryId)
    repositoryConfiguration.setServerEndpoint(serverEndpoint)

    serviceDiscoveryConfiguration = ServiceDiscoveryConfiguration.new(repositoryConfiguration)
    discovery = ServiceDiscoveryFactory.createDiscoveryService(serviceDiscoveryConfiguration)

    $categories = nil
    begin
      $categories = discovery.getServiceCategories()
    rescue Exception => e
      logger.error "Discovery Process failed, through exception: " + e.to_s + "\n" + "Stack trace: #{e.backtrace.map {|l| "  #{l}\n"}.join}"
      $categories = Array.new(1) { "http://msee.sti2.at/categories#business"}
    end

    jsonAutoCompleteData = Array.new

    $categories.each do |category|
      logger.debug category
      entry = Hash.new
      entry[:key] = category
      entry[:value] = category

      strIndex = category.index("#")

      if strIndex!=nil
        cutDownCategory = category[strIndex+1, (category.length - 1) - strIndex]
        entry[:value] = cutDownCategory
      end

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
  #     @notice = "The discovery was successful.";

  #   rescue => e
  #     @error = "Discovery Process failed, through exception: " + e.to_s
  #   end
  # end

  # The discover method call
  private
  def discover(categories)



    begin

      serverEndpoint = "http://sesa.sti2.at:8080/openrdf-sesame"
      repositoryId = "msee"


      repositoryConfiguration = ServiceRepositoryConfiguration.new
      repositoryConfiguration.setRepositoryID(repositoryId)
      repositoryConfiguration.setServerEndpoint(serverEndpoint)

      serviceDiscoveryConfiguration = ServiceDiscoveryConfiguration.new(repositoryConfiguration)
      discovery = ServiceDiscoveryFactory.createDiscoveryService(serviceDiscoveryConfiguration)

      result = discovery.discoverMap(categories)

      output = ""
      output += "<div class=\"accordion\" id=\"accordion2\">"
      catcounter = 1
      for s in result.keySet()
        output += " <div class=\"accordion-group\">"
        output += "  <div class=\"accordion-heading\">"
        output += "   <a class=\"accordion-toggle theButton\" href=\"#collapse_#{catcounter}\" data-toggle=\"collapse\" data-parent=\"#accordion2\">#{s}</a>"
        output += "  </div>"
        output += "  <div id=\"collapse_#{catcounter}\" class=\"accordion-body in collapse\" style=\"height:0px;\">"
        output += "   <div class=\"accordion-inner\" style=\"white-space: pre-wrap;\">"
        inner = result.get s
        output += CGI::escapeHTML(inner.html_safe)
        output += "   </div>"
        output += "  </div>"
        output += " </div>"
        catcounter +=1
      end
      output += "</div>"

      output += "<script>"
      output += " $(document).ready(function(){"
      output += "  $('#accordion2 .theButton').on('click', function(e) {"
      output += "   $('#collapse_discover').css('height', 'auto');"
      output += "  });"
      output += " });"
      output += "</script>"


      @discover_output = output
      @notice = "The discovery was successful.";

    rescue Exception => e
      @error = "Discovery Process failed, through exception: " + e.to_s + "\n" # + "Stack trace: #{e.backtrace.map {|l| "  #{l}\n"}.join}"
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
  #     @notice = "The discovery was successful.";

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
