class AddNameToPlatformOwner < ActiveRecord::Migration
  def change
    add_column :platform_owners, :name, :string

  end
end
