class AddUsernameToPlatformOwner < ActiveRecord::Migration
  def change
    add_column :platform_owners, :username, :string

  end
end
