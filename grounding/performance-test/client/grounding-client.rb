require 'rails'
require 'savon'
require 'nokogiri'
require 'benchmark'
include Benchmark

Savon.configure do |config|
    config.log = false              # disable logging
    config.log_level = :fatal       # changing the log level
    config.logger = Rails.logger    # using the Rails logger
end

class GroundingClient
    attr_reader :data
    attr_reader :xslt_to_ontology_url
    attr_reader :xslt_to_output_url

    WSDL_URL = "http://localhost:9090/grounding-webservice/services/grounding?wsdl"

    def initialize(new_file_name, new_xslt_to_ontology_url, new_xslt_to_output_url)
        @xslt_to_ontology_url = new_xslt_to_ontology_url
        @xslt_to_output_url = new_xslt_to_output_url
        File.open(new_file_name, "r") do |f|
            @data = f.read
        end
    end

    def output_performance_measurements(response)
        xml = response.xpath("//return")[0]
        puts "Total (server-side) Execution Time   : #{xml.xpath('//totalExecutionTime/text()')} milliseconds"
        puts "|--> Input to Ontology               : #{xml.xpath('//input2OntologyExecutionTime/text()')} milliseconds"
        puts "|--> Ontology to Output              : #{xml.xpath('//ontology2OutputExecutionTime/text()')} milliseconds"

    end

    def execute
        client = Savon::Client.new do
            wsdl.document = WSDL_URL
        end

        soapData = @data
        xsltOntology = @xslt_to_ontology_url
        xsltOutput = @xslt_to_output_url
        response = client.request :transform_debug do
            soap.input = ["gr:transform_debug", { "xmlns:gr" => "http://sesa.sti2.at/services/" } ]
            soap.body = {
                "inputMessage" => soapData,
                "xsltToOntologyURL" => xsltOntology,
                "xsltToOutputURL" => xsltOutput
            }
            soap.element_form_default = :unqualified
        end
    end
end

grounding = GroundingClient.new("gs1_example.xml", "http://localhost:3000/efreight/gs1_tep_demonstration/gs12ontology.xslt", "http://localhost:3000/efreight/gs1_tep_demonstration/ontology2tep.xslt")
grounding2 = GroundingClient.new("tep_example.xml", "http://localhost:3000/efreight/gs1_tep_demonstration/tep2ontology.xslt", "http://localhost:3000/efreight/gs1_tep_demonstration/ontology2gs1.xslt")

#time = Benchmark.bmbm do |t|
#    t.report("GS1 -> TEP") { grounding.output_performance_measurements(grounding.execute) }
#    t.report("TEP -> GS1") { grounding2.output_performance_measurements(grounding2.execute) }
#end
#puts "-----------------------"
#puts time

Benchmark.benchmark(CAPTION, 7, FORMAT, ">avg:") do |t|
    for i in 1..100 do
        time = t.report("GS1 -> TEP") {
            grounding.output_performance_measurements(grounding.execute)
        }
    end
end
