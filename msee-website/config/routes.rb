SesaWebsite::Application.routes.draw do

  resources :users
  
  match "monitorings" => "monitorings#index"
  get "monitorings/index"

  match "monitorings/index" => "monitorings#index"
  get "monitorings/index"

  match "monitorings/showEndpointDetails/:qos/*url" => "monitorings#showEndpointDetails"

  match "monitorings/showMonitoringViszualization" => "monitorings#showMonitoringViszualization"

  match "monitorings/getRankedEndpoints/:qos/:values/*endpoints" => "monitorings#getRankedEndpoints"

  match "monitorings/setSelectedQoSParams" => "monitorings#getQoSParamKeys"
  
  match "monitorings/qoSParamsRanking" => "monitorings#getQoSParamKeysRanking"



  #Json services
  match "monitorings/getSubcategoriesAndServices/:category" => "monitorings#getSubcategoriesAndServices"


  
  root :to => "home#index"
  get "home/index"
    
  match "dashboards/grounding" => "dashboards/grounding#index"
  get "dashboards/grounding/index"

  match "dashboards/registration" => "dashboards/registration#index"
  get "dashboards/registration/index"
    
  match "dashboards/discovery" => "dashboards/discoveries#index"
  get "dashboards/discoveries/index"
    
 
  match "release" => "release#index"
  get "release/index"

  match "doc" => "doc#index"
  namespace :doc do
      get "index"
      get "service_annotation"
      namespace :efreight do
          match "ontology" => "ontology#index"
          get "ontology/index"
          match "transformation" => "transformation#index"
          get "transformation/index"
      end
  end
    
    get "services/:id/update", :controller => "services", :action => "update"
    get "services/:id/destroy", :controller => "services", :action => "destroy"
    
    get "ontologies/:id/update", :controller => "ontologies", :action => "update"
    get "ontologies/:id/destroy", :controller => "ontologies", :action => "destroy"
    match "ontologies/add" => "ontologies#create"

    match "faq" => "faq#index"
    get "faq/index"

    match "about" => "about#index"
    get "about/index"

    match "development" => "development#index"
    get "development/index"

    match "msee" => "msee#index"
    namespace :msee do
        get "index"
    end
  


  #resources :monitorings

  resources :services, :only => [:index, :show, :update, :delete]
  resources :ontologies, :only => [:index, :show, :create, :update, :delete]

  devise_for :platform_owners
  resources :platform_owners, :only => [:show, :edit, :update]

  devise_for :service_owners
  resources :service_owners, :only => [:show, :edit, :update]

  get "shared/index"

  get "transformation/index"

  root :to => "home#index"
  get "home/index"

  match "dashboards/grounding" => "dashboards/grounding#index"
  get "dashboards/grounding/index"

  match "dashboards/registration" => "dashboards/registration#index"
  get "dashboards/registration/index"

  match "dashboards/discovery" => "dashboards/discoveries#index"
  get "dashboards/discoveries/index"

  match "release" => "release#index"
  get "release/index"

  match "doc" => "doc#index"
  namespace :doc do
    get "index"
    get "service_annotation"
    namespace :efreight do
      match "ontology" => "ontology#index"
      get "ontology/index"
      match "transformation" => "transformation#index"
      get "transformation/index"
    end
  end

  get "services/:id/update", :controller => "services", :action => "update"
  get "services/:id/destroy", :controller => "services", :action => "destroy"

  get "ontologies/:id/update", :controller => "ontologies", :action => "update"
  get "ontologies/:id/destroy", :controller => "ontologies", :action => "destroy"
  match "ontologies/add" => "ontologies#create"

  match "faq" => "faq#index"
  get "faq/index"

  match "about" => "about#index"
  get "about/index"

  match "development" => "development#index"
  get "development/index"

  match "msee" => "msee#index"
  namespace :msee do
    get "index"
  end

end
