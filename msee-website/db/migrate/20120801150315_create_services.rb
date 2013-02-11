class CreateServices < ActiveRecord::Migration
  def change
    create_table :services do |t|
      t.string :wsdl_url
      t.integer :owner_id

      t.timestamps
    end
  end
end
