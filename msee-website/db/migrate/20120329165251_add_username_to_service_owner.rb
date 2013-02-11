class AddUsernameToServiceOwner < ActiveRecord::Migration
  def change
    add_column :service_owners, :username, :string

  end
end
