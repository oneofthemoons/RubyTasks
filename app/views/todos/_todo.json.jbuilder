json.extract! todo, :id, :text, :isCompleted, :project_id, :created_at, :updated_at
json.url todo_url(todo, format: :json)
