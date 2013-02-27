class AddNameToServiceOwner < ActiveRecord::Migration
  def change
    add_column :service_owners, :name, :string

  end
end
