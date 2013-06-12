SesaWebsite::Application.routes.draw do

  resources :users


  root :to => "home#index"
  match "dashboards/registration" => "dashboards/registration#index"
  match "dashboards/discovery" => "dashboards/discoveries#index"
  match "dashboards/invocation" => "dashboards/invocation#index"
  
  match "dashboards/discovery/categories" => "dashboards/discoveries#getCategoriesOfServiceIDForAutoCompleteBox"

  match "monitorings" => "monitorings#index"
  get "monitorings/index"

  match "monitorings/index" => "monitorings#index"
  get "monitorings/index"

  #match "monitorings/showEndpointDetails/:qos/*url" => "monitorings#showEndpointDetails"

  match "monitorings/getServiceDetails" => "monitorings#getServiceDetails"


  match "monitorings/getGoogleGraphData" => "monitorings#getGoogleGraphData"
  #match "monitorings/getGoogleGraphData/:qos/*url" => "monitorings#getGoogleGraphData"

 # match "monitorings/getRankedEndpoints/:qos/:values/*endpoints" => "monitorings#getRankedEndpoints"

  match "monitorings/setSelectedQoSParams" => "monitorings#getQoSParamKeys"
  
  match "monitorings/qoSParamsRanking" => "monitorings#getQoSParamKeysRanking"


  match "monitorings/doRanking" => "monitorings#doRanking"

  #Json services
  match "monitorings/getSubcategoriesAndServices/:category" => "monitorings#getSubcategoriesAndServices"

  match "release" => "release#index"

  match "doc" => "doc#index"
  namespace :doc do
      get "index"
      get "service_annotation"
  end


  match "rules" => "rules#index"
  match "monitorings/options" => "monitorings/rulesoptions#index"

  #get "services/:id/update", :controller => "services", :action => "update"
  #get "services/:id/destroy", :controller => "services", :action => "destroy"

  #get "ontologies/:id/update", :controller => "ontologies", :action => "update"
  #get "ontologies/:id/destroy", :controller => "ontologies", :action => "destroy"
  #match "ontologies/add" => "ontologies#create"

  #get "services/:id/update", :controller => "services", :action => "update"
  #get "services/:id/destroy", :controller => "services", :action => "destroy"

  #get "ontologies/:id/update", :controller => "ontologies", :action => "update"
  #get "ontologies/:id/destroy", :controller => "ontologies", :action => "destroy"
  #match "ontologies/add" => "ontologies#create"

  match "faq" => "faq#index"
  get "faq/index"

  match "about" => "about#index"
  get "about/index"

  match "development" => "development#index"
  get "development/index"

  resources :services, :only => [:index, :show, :update, :delete]
  resources :ontologies, :only => [:index, :show, :create, :update, :delete]

  devise_for :platform_owners
  resources :platform_owners, :only => [:show, :edit, :update]

  devise_for :service_owners
  resources :service_owners, :only => [:show, :edit, :update]

end