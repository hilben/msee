require 'java'

java_import Java::at.sti2.msee.discovery.core.DiscoveryServiceImpl
java_import Java::at.sti2.msee.discovery.api.webservice.Discovery
java_import Java::at.sti2.msee.discovery.api.webservice.DiscoveryException
java_import Java::at.sti2.msee.discovery.core.ServiceDiscoveryFactory
java_import Java::at.sti2.msee.discovery.core.common.ServiceDiscoveryConfiguration
java_import Java::at.sti2.msee.triplestore.ServiceRepositoryConfiguration
java_import Java::at.sti2.msee.triplestore.impl.SesameServiceRepositoryImpl



java_import Java::at.sti2.msee.monitoring.api.qos.QoSType
java_import Java::at.sti2.msee.monitoring.core.chart.GoogleChart
java_import Java::at.sti2.msee.monitoring.core.MonitoringComponentImpl


# Retrieve a JSON Resource
class MonitoringsController < ApplicationController
  @categories_endpoints
  @currentEndpoint
  @currentParameters
  @qosParamKeys

  @serviceCatalogeEntries

  def index
    # getCategoriesAndEndpoints()

    respond_to do |format|
      format.html # index.html.erb
      format.json
    end
  end

  # GET /monitorings/1
  # GET /monitorings/1.json
  def show

    respond_to do |format|
      format.html # show.html.erb
      format.json
    end
  end

  def showEndpointDetails
    logger.info "Received Parameter: #{params[:url]}"
    logger.info "Received Parameter: #{params[:qos]}"

    @currentEndpoint = "http://"+params[:url]
    @currentParameters = params[:qos]

    logger.info "Received Parameter: #{@currentEndpoint}"
    logger.info "Received Parameter: #{@currentParameters}"

    render :partial => "monitorings/endpointdetails"
  end

  def getQoSParamKeys
    @qosParamKeys = QoSType.values
    logger.info "Received : #{@qosParamKeys}"
    render :partial => "monitorings/selectedqosparams"
  end


  def getQoSParamKeysRanking
    #soap call to the server
    @qosParamKeys = QoSType.values
    logger.info "Received : #{@qosParamKeys}"

    render :partial => "monitorings/qosparamsranking"
  end

  def getRankedEndpoints
    @errors = "no erros"

    #soap call to the server
    client = Savon::Client.new(@@ranking_url)

    logger.info("qos: #{params[:qos]}  values: #{params[:values]} endpoints: #{params[:endpoints]} ");

    @qos = params[:qos].split(',').to_a
    @values = params[:values].split(',')
    @endpoints = params[:endpoints].split(',')

    for x in @values do
      x.to_s["x"]="."
      x = x.to_f
    end

    q = @qos
    v = @values
    e = @endpoints
    z = @qos

    logger.info("qos: #{@qos}  values: #{@values} endpoints: #{@endpoints} ");
    logger.info("qos: #{@qos.class}  values: #{@values.class} endpoints: #{@endpoints.class} z: #{z}");
    #TODO: arg0 should be renamed see soap service in backend
    begin
      # responseclient = client.request :ns2, :getQoSRankedEndpoints, arg0: @qos, arg1: @values, arg2: @endpoints
      responseclient = client.request :ns2, :getQoSRankedEndpoints do
        soap.body = {
          "QoSParamKeys" => q,
          "preferenceValues" => v,
          "endpoints" => e,
        }
      end

      data = responseclient.to_hash.first

      @results = data[1][:return]

      logger.info("Data: #{data}")

      if responseclient.success?
        logger.info("Success!")
      end

    rescue Savon::SOAP::Fault => fault
      @errors = fault.to_s
      logger.info("FAULT: #{fault.to_s}")
    end

    #

    render :partial => "monitorings/rankings"
  end


  #Json call for lazy loading of category/service tree
  def getSubcategoriesAndServices

    #obtain the categories
    serverEndpoint = "http://sesa.sti2.at:8080/openrdf-sesame"
    repositoryId = "msee-test"

    repositoryConfiguration = ServiceRepositoryConfiguration.new
    repositoryConfiguration.setRepositoryID(repositoryId)
    repositoryConfiguration.setServerEndpoint(serverEndpoint)

    serviceDiscoveryConfiguration = ServiceDiscoveryConfiguration.new(repositoryConfiguration)

    serviceTree = ServiceDiscoveryFactory.createDiscoveryService(serviceDiscoveryConfiguration)

    logger.info "DIS #{serviceTree.discoverCategoryAndService}"
    logger.info "LENGTH #{serviceTree.getServiceCategories.length}"
    for s in serviceTree.discoverCategoryAndService
      logger.info "Cats: #{s}"
    end

    #array storing nodes for the json return
    nodes = Array.new

    serviceTree.discoverCategoryAndService.each do |entry|

      node = Hash.new
      node[:title] = entry.getName
      node[:key] = entry.getName

      node[:isFolder] = true
      node[:isLazy] = false
      childrenNodes = Array.new

      entry.getServiceSet.each do |service|
        children = Hash.new
        children[:title] = service.getName
        children[:key] = service.getName
        children[:isFolder] = true

        operationNodes = Array.new

        service.getOperationSet.each do |operation|
          operationNode = Hash.new
          operationNode[:title] = operation.getName
          operationNode[:key] = operation.getName
          operationNode[:isFolder] = false
          operationNodes.push(operationNode)
        end

        children[:children] = operationNodes

        childrenNodes.push(children)
      end

      node[:children] = childrenNodes
      nodes.push(node)

    end


    logger.info("Json : #{nodes}")

    respond_to do |format|
      format.js  {render :json => nodes}
    end
  end


  def getGoogleGraphData
    begin
      chart = GoogleChart.new

      @qos = params[:qos]
      @url = params[:url]

      @url = "http://" + @url[6,@url.length]

      logger.info "getGoogleGraphData Received Parameter: #{@qos}"
      logger.info "getGoogleGraphData Received Parameter: #{@url}"

      listEndpoints = Java::JavaUtil::ArrayList.new
      listEndpoints << @url

      listParameters = Java::JavaUtil::ArrayList.new
      listParameters << @qos

      logger.info "getGoogleGraphData listEndpoints: #{listEndpoints}"
      logger.info "getGoogleGraphData listParameters: #{listParameters}"

      jsonData = chart.asJson(listEndpoints,listParameters)

      jsonData = "ajaxCallSucceed("+jsonData+")"

      logger.info "JSON DATA: #{jsonData}"

    rescue => e
      @error = "getGoogleGraphData failed, through exception: " + e.to_s
      jsonData = @error
    end

    respond_to do |format|
      format.js  {render :json => jsonData}
    end
  end


end
