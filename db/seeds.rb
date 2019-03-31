# This file should contain all the record creation needed to seed the database with its default values.
# The data can then be loaded with the rails db:seed command (or created alongside the database with db:setup).
#
# Examples:
#
#   movies = Movie.create([{ name: 'Star Wars' }, { name: 'Lord of the Rings' }])
#   Character.create(name: 'Luke', movie: movies.first)
seed_file = Rails.root.join('db', 'seeds', 'seeds.yml')
config = HashWithIndifferentAccess.new(YAML.load(File.read(File.expand_path(seed_file, __FILE__))))
for p in config[:projects]
	@project = Project.new
	@project.title = p[:title]
	@project.save
	for t in p[:todos]
		@project.todos.create(t)
	end
end