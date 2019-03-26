class TodosController < ApplicationController
  before_action :set_todo, only: [:show, :edit, :update, :destroy]

  def show
  end

  def new
	@project = Project.find(params[:project_id])
    @todo = Todo.new
  end

  # GET /todos/1/edit
  def edit
  end

  # POST /todos
  # POST /todos.json
  def create
	@project = Project.find(params[:project_id])
	@todo = @project.todos.create(todo_params)

	redirect_to root_path
  end

  private
    # Use callbacks to share common setup or constraints between actions.
    def set_todo
      @todo = Todo.find(params[:id])
    end

    # Never trust parameters from the scary internet, only allow the white list through.
    def todo_params
      params.require(:todo).permit(:text, :isCompleted, :project_id)
    end
end
