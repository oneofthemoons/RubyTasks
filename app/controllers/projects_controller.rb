class ProjectsController < ApplicationController
  before_action :set_project, only: [:show, :edit, :update, :destroy]

  def index
	@projects = Project.all
	@project = Project.new

	if params[:project_id] != nil
		@project = Project.find(params[:project_id])
		@project.todos.create({"text": params[:todo_text]})
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

  private
	
	def todo_params
		params.permit(:todo_text)
	end
	
    def project_params
      	params.require(:project).permit(:title)
    end
end
