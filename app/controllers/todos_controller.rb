class TodosController < ApplicationController
  before_action :set_todo, only: [:show, :edit, :update, :destroy]

  # GET /todos
  # GET /todos.json
  def index
    @todos = Todo.all
  end

  # GET /todos/1
  # GET /todos/1.json
  def show
  end

  # GET /todos/new
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
	puts "URAAAAA"
    # respond_to do |format|
	# 	if @todo.save
	# 		format.html { redirect_to @todo, notice: 'Todo was successfully created.' }
	# 		format.json { render :show, status: :created, location: @todo }
	# 	else
	# 		format.html { render :new }
	# 		format.json { render json: @todo.errors, status: :unprocessable_entity }
	# 	end
	# end
	
	# @project.save

	redirect_to root_path
  end

  # PATCH/PUT /todos/1
  # PATCH/PUT /todos/1.json
  def update
	@project = Project.find(params[:project_id])
    respond_to do |format|
      if @todo.update(todo_params)
        format.html { redirect_to root_path, notice: 'Todo was successfully updated.' }
        format.json { render :show, status: :ok, location: @todo }
      else
        format.html { render :edit }
        format.json { render json: @todo.errors, status: :unprocessable_entity }
      end
    end
  end

  # DELETE /todos/1
  # DELETE /todos/1.json
  def destroy
	@project = Project.find(params[:project_id])
    @todo.destroy
    respond_to do |format|
      format.html { redirect_to todos_url, notice: 'Todo was successfully destroyed.' }
      format.json { head :no_content }
    end
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
