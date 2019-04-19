class CreateTodos < ActiveRecord::Migration[5.2]
  def change
    create_table :todos do |t|
      t.string :text
      t.boolean :isCompleted
      t.references :project, foreign_key: true

      t.timestamps
    end
  end
end
