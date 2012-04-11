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

    def get_node_count
        reader = Nokogiri::XML(@data)
        nodes = reader.xpath("//*")
        nodes.count
    end

    def output_performance_measurements(response)
        xml = response.xpath("//return")[0]
        puts "Total (server-side) Execution Time   : #{xml.xpath('//totalExecutionTime/text()')} milliseconds"
        puts "|--> Input to Ontology               : #{xml.xpath('//input2OntologyExecutionTime/text()')} milliseconds"
        puts "|--> Ontology to Output              : #{xml.xpath('//ontology2OutputExecutionTime/text()')} milliseconds"
    end

    def average_execution(x_times, filename)
        client = Savon::Client.new do
            wsdl.document = WSDL_URL
        end

        soapData = @data
        xsltOntology = @xslt_to_ontology_url
        xsltOutput = @xslt_to_output_url

        totalAverage = 0
        liftingAverage = 0
        loweringAverage = 0
        for i in 1..x_times
            response = client.request :transform_debug do
                soap.input = ["gr:transform_debug", { "xmlns:gr" => "http://sesa.sti2.at/services/" } ]
                soap.body = {
                    "inputMessage" => soapData,
                    "xsltToOntologyURL" => xsltOntology,
                    "xsltToOutputURL" => xsltOutput
                }
                soap.element_form_default = :unqualified
            end
            xml = response.xpath("//return")[0]
            totalAverage += xml.xpath('//totalExecutionTime/text()').to_s.to_i
            liftingAverage += xml.xpath('//input2OntologyExecutionTime/text()').to_s.to_i
            loweringAverage += xml.xpath('//ontology2OutputExecutionTime/text()').to_s.to_i
        end
        totalAverage /= x_times
        liftingAverage /= x_times
        loweringAverage /= x_times

        File.open(filename, 'a+') {|f| f.write("#{get_node_count} #{totalAverage}\n") }

        puts "Average values with #{x_times} cycles"
        puts "Total (server-side) Execution Time (Average) : #{totalAverage} milliseconds"
        puts "|--> Input to Ontology (Average})            : #{liftingAverage} milliseconds"
        puts "|--> Ontology to Output (Average)            : #{loweringAverage} milliseconds"
        #puts "Output Message: #{xml.xpath('//intermediateMessage/text()')}"
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

#groundingGS1 = GroundingClient.new("perf_xml/gs1_example.xml", "http://localhost:3000/efreight/gs1_tep_demonstration/gs12ontology.xslt", "http://localhost:3000/efreight/gs1_tep_demonstration/ontology2tep.xslt")
#groundingGS1.output_performance_measurements(groundingGS1.execute)

filename = "performance.xgraph"

File.open(filename, 'w') {|f| f.write("") }
for i in 0..6
    groundingGS1 = GroundingClient.new("perf_xml/gs1_example_#{i}.xml", "http://localhost:3000/efreight/gs1_tep_demonstration/gs12ontology.xslt", "http://localhost:3000/efreight/gs1_tep_demonstration/ontology2tep.xslt")
    groundingGS1.average_execution(10, filename)
    puts groundingGS1.get_node_count
end

#groundingTEP = GroundingClient.new("tep_example.xml", "http://localhost:3000/efreight/gs1_tep_demonstration/tep2ontology.xslt", "http://localhost:3000/efreight/gs1_tep_demonstration/ontology2gs1.xslt")
#groundingTEP.output_performance_measurements(groundingTEP.execute)
#puts groundingTEP.get_node_count

#time = Benchmark.bmbm do |t|
#    t.report("GS1 -> TEP") { grounding.output_performance_measurements(grounding.execute) }
#    t.report("TEP -> GS1") { grounding2.output_performance_measurements(grounding2.execute) }
#end
#puts "-----------------------"
#puts time

#Benchmark.benchmark(CAPTION, 7, FORMAT, ">avg:") do |t|
#    for i in 1..100 do
#        time = t.report("GS1 -> TEP") {
#            grounding.output_performance_measurements(grounding.execute)
#        }
#    end
#end
