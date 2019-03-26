class ProjectsController < ApplicationController
  before_action :set_project, only: [:show, :edit, :update, :destroy]

  # GET /projects
  # GET /projects.json
  def index
	@projects = Project.all
	@project = Project.new

	if params[:project_id] != nil
		@project = Project.find(params[:project_id])
		@project.todos.create({"text": params[:todo_text]})
		redirect_to root_path
	end
  end

  # GET /projects/1
  # GET /projects/1.json
  def show
  end

  # GET /projects/new
  def new
    @project = Project.new
  end

  # GET /projects/1/edit
  def edit
  end

  # POST /projects
  # POST /projects.json
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
    # Use callbacks to share common setup or constraints between actions.
    def set_project
      @project = Project.find(params[:id])
	end
	
	def todo_params
		puts params
		params.permit(:todo_text)
	end

    # Never trust parameters from the scary internet, only allow the white list through.
    def project_params
      	params.require(:project).permit(:title)
    end
end
