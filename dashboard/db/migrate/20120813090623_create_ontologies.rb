class CreateOntologies < ActiveRecord::Migration
  def change
    create_table :ontologies do |t|
      t.string :ontology_url
      t.integer :user_id

      t.timestamps
    end
  end
end
