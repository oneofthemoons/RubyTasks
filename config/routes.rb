Rails.application.routes.draw do
  get 'projects/index'
  post 'projects/index'

  resources :projects_json
  resources :todos_json

  resources :projects do
	  resources :todos
  end

  root 'projects#index'
end
