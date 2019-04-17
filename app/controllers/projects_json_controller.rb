class ProjectsJsonController < ApplicationController
    def index
        render json: Project.all
    end
end
