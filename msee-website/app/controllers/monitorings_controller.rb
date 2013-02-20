require 'java'

#java_import Java::at.sti2.ngsee.monitoring.webservice.ManagementWebService
#java_import Java::at.sti2.ngsee.monitoring.webservice.EventStreamWebService
#java_import Java::at.sti2.ngsee.monitoring.webservice.ProxyWebService
#java_import Java::at.sti2.wsmf.core.common.MonitoringConfig
#java_import Java::at.sti2.wsmf.core.PersistentHandler

# Retrieve a JSON Resource
class MonitoringsController < ApplicationController

  @@ranking_url = "http://sesa.sti2.at:8080/ranking-webservice/services/ranking?wsdl"
  @@monitoring_url = "http://sesa.sti2.at:8080/monitoring-webservice/management?wsdl"


  #@@ranking_url = "http://localhost:9196/ranking-webservice/services/ranking?wsdl"
  #@@monitoring_url = "http://localhost:9197/monitoring-webservice/services/management?wsdl"


  @categories_endpoints
  @currentEndpoint
  @currentParameters
  @qosParamKeys

  @serviceCatalogeEntries

  def index
    #WSDL File HARDCODED!!!


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


   # @b = PersistentHandler.
    #@a = MonitoringConfig.getConfig
    #@a = @a.toString()
    #logger.info "Config for Monitoring #{@a}"


    # render :id
    @currentEndpoint = "http://"+params[:url]
    @currentParameters = params[:qos]


    logger.info "Received Parameter: #{@currentEndpoint}"
    logger.info "Received Parameter: #{@currentParameters}"


    render :partial => "monitorings/endpointdetails"
  end

  def getQoSParamKeys

    #soap call to the server
    # #TODO: hardcoded
    client = Savon::Client.new(@@monitoring_url)
    responseclient = client.request :ns2, :getQoSParamKeys
    data = responseclient.to_hash.first

    @qosParamKeys = data[1][:return]
    logger.info "Received : #{@qosParamKeys}"

    render :partial => "monitorings/selectedqosparams"
  end

  def getQoSParamKeysRanking

    #soap call to the server
    client = Savon::Client.new(@@monitoring_url)
    responseclient = client.request :ns2, :getQoSParamKeys
    data = responseclient.to_hash.first

    @qosParamKeys = data[1][:return]
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

    #array storing nodes for the json return
    nodes = Array.new

    #soap call to the server
    #TODO: hardcoded
    client = Savon::Client.new(@@monitoring_url)
    #TODO: arg0 should be renamed see soap service in backend
    #responseclient = client.request :ns2, :getSubcategoriesAndServices, arg0: params[:category]

    # responseclient = client.request :ns2, :getQoSRankedEndpoints, arg0: @qos, arg1: @values, arg2: @endpoints
    responseclient = client.request :ns2, :getSubcategoriesAndServices do
      soap.body = {
        "arg0" => params[:category],
      }
    end

    if responseclient.success?

      data = responseclient.to_hash.first
      responseArray = Array.new

      if data[1][:return].is_a? String
        responseArray.push(data[1][:return])
      else
        responseArray = data[1][:return]
      end

      if !responseArray.nil?

        responseArray.each do |entry|

          node = Hash.new
          node[:title] = entry
          node[:key] = entry

          if !entry.to_s.match(/^http:/)
            node[:isFolder] = true
            node[:isLazy] = true
          end
          nodes.push(node)

        end
      end
    else
      node = Hash.new
      node[:title] = "Error retrieving categories"
      node[:key] = "Error"
      nodes.push(node)
    end

    logger.info("Json : #{nodes}")

    respond_to do |format|
      format.js  {render :json => nodes}
    end
  end

  # The lookup method call
  private

  Savon.configure do |config|
    config.logger = Rails.logger  # using the Rails logger
  end



end
