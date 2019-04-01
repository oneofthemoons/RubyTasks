class TodosController < ApplicationController
	before_action :set_todo, only: [:show, :edit, :update, :destroy]
  
	def create
	  @project = Project.find(params[:project_id])
	  @todo = @project.todos.create(todo_params)
  
	  redirect_to root_path
	end
  
	def update
	  @project = Project.find(params[:project_id])
	  @todo = @project.todos.find(params[:id])
	  if @todo.isCompleted
		@todo.isCompleted = false
	  else
		@todo.isCompleted = true
	  end
	  @todo.save
	end

	private
	  def set_todo
		@todo = Todo.find(params[:id])
	  end
  
	  def todo_params
		params.require(:todo).permit(:text, :isCompleted, :project_id)
	  end
  end
  
