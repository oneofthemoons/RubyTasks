# This file should contain all the record creation needed to seed the database with its default values.
# The data can then be loaded with the rails db:seed command (or created alongside the database with db:setup).
#
# Examples:
#
#   movies = Movie.create([{ name: 'Star Wars' }, { name: 'Lord of the Rings' }])
#   Character.create(name: 'Luke', movie: movies.first)
seed_file = Rails.root.join('db', 'seeds', 'seeds.yml')
config = YAML::load_file(seed_file)

todos1 = Todo.create([{text: 'Купить молоко', isCompleted: false}, {text: 'Заменить масло в двигателе до 23 апреля', isCompleted: false}, {text: 'Отправить письмо бабушке', isCompleted: true}, {text: 'Заплатить за квартиру', isCompleted:false}, {text: 'Забрать обувь из ремонта', isCompleted:false}])
todos2 = Todo.create([{text: 'Позвонить заказчику', isCompleted: true}, {text: 'Отправить документы', isCompleted: true}, {text: 'Заполнить отчет', isCompleted:false}])
todos3 = Todo.create([{text: 'Позвонить другу', isCompleted: false}, {text: 'Подготовиться к поездке', isCompleted:false}])
Project.create([{title: 'Семья', todos: todos1}, {title: 'Работа', todos: todos2}, {title: 'Прочее', todos: todos3}])