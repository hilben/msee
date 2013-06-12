java_import Java::at.sti2.msee.ranking.repository.RankingRepositoryHandler

class RulesController < ApplicationController


	def index

		serviceID = params[:serviceID]
		data = params[:data]

		logger.info "data: #{data}"
		logger.info "serviceID: #{serviceID}"

		if !data.blank? and !serviceID.blank?

			logger.info "data: #{data}"
			logger.info "serviceID: #{serviceID}"

			begin

				handler = RankingRepositoryHandler.getInstance

				url = java.net.URL.new(serviceID)

				logger.info("url: #{url}");
				logger.info("data: #{data}");

				handler.setRulesForWebservice(url, data);

				@notice = "The rule was succesfully set.";

			rescue => e
				@error = "Setting the rule failed, through exception: " + e
			end
		end
	end

	def getParametersForTemplate
		
	end
end
