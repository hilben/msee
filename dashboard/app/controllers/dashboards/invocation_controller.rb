
java_import Java::at.sti2.msee.invocation.core.ServiceInvocationImpl


class Dashboards::InvocationController < ApplicationController


	def index

		serviceID = params[:serviceID]
		data = params[:data]
		operation = params[:operation]

		if !data.blank? and !serviceID.blank?
			#transform(input, xslt_input, xslt_output)
			logger.info "data: #{data}"
			logger.info "operation: #{operation}"
			logger.info "serviceID: #{serviceID}"

			begin
				invoker = ServiceInvocationImpl.new

				result = invoker.invoke(serviceID, operation,data)
				logger.debug "invoker : #{result}"

				@output = result
				@notice = "The invocation was succesfull.";

			rescue => e
				@error = "Invocation Process failed, through exception: " + e.to_s
			end
		end
	end
end
