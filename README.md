# shoppingCart
Приложение позволяет пробегать по спискам, складывать товары в корзину, создавать свои шаблоны списков, которые также можно бросить в корзину. Список корзины можно отправлять смс или через мессенджеры
Хранение файлов в JSON локально, в планах лить в облако

Стэк:
-работа с RecyclerView и кастомными адаптерами
-работа с фрагментами и диалогами
-RxJava
-JSON
-SharedPreferences
-ButterKnife(была попытка внедрить, есть ветка, но там бага была со списками, руки не добрались до нее)
-использование сторонних библиотек - sectionedrecyclerviewadapter, materialsearchview
-drag & drop

Планы:
Лить JSON в облако(ORM) - это позволить реализовать авторизацию и доступность с другого устройства
Довнедрить ButterKnife либо юзать DataBinding
Дизайн бы еще

Не покрыто тестами(на андроиде тесты пока не разбирала на практике)!