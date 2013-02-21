class UsersController < ApplicationController
  # GET /users
  # GET /users.json
  def index    
     if service_owner_signed_in?
      redirect_to service_owner
    else
      redirect_to "/login"
    end   
  end

  # GET /users/1
  # GET /users/1.json
  def show
    @user = ServiceOwner.find(params[:id])
    
    @services = Services.where(:owner_id => params[:id])
    @ontologies = Ontology.where(:user_id => params[:id])
    
    respond_to do |format|
      format.html
      format.json { render :json => @user }
      format.xml { render :xml => @user }
    end
  end

end
