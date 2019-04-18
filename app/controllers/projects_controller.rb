class ProjectsController < ApplicationController
  before_action :set_project, only: [:show, :edit, :update, :destroy]
  skip_before_action :verify_authenticity_token

  def index
	@projects = Project.all
	@project = Project.new

	if params[:project_id] != nil
		@project = Project.find(params[:project_id])
    @project.todos.create({"text": params[:todo_text], "isCompleted": false})
    for todo in @project.todos
      if todo.isCompleted != true
        todo.isCompleted = false
      end
    end
		redirect_to root_path
	end
  end

  def new
    @project = Project.new
  end

  def create
    @project = Project.new(project_params)
    respond_to do |format|
      if @project.save
        format.html { redirect_to root_path, notice: 'Проект был успешно создан.' }
        format.json { render :show, status: :created, location: @project }
      end
    end
  end

  def update
	@project = Project.find(params[:project_id])
  end

  private
	
	def todo_params
		params.permit(:todo_text)
	end
	
    def project_params
      	params.require(:project).permit(:title)
    end
end
