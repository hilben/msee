# Load the rails application
require File.expand_path('../application', __FILE__)


# Load all jar files to use Java classes with jRuby
require 'java'
begin
	Dir["lib/*.jar"].each{|jar| require jar}
rescue Exception => e
	# we are on the tomcat
	path = File.expand_path File.dirname(__FILE__)
	puts "Current path with exeption #{path}"
	puts "Exception #{e}"
	begin
		Dir["../lib/*.jar"].each{|jar| require jar}
	rescue Exception => e
		puts "Exception #{e}"
	end
end



# Initialize the rails application
SesaWebsite::Application.initialize!
