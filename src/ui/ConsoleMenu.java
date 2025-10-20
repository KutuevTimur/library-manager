package ui;

import model.Book;
import util.InputHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


import java.io.*; //для работы с файлами


public class ConsoleMenu {
    private List<Book> books;
    private boolean isRunning;

    public ConsoleMenu() {
        //основное хранилище всего класса
        this.books = new ArrayList<>();
        // для управл главным циклом проги
        this.isRunning = true;
    }

    // главнй метод для запуска всего приложения
    public void start() {
        System.out.println("___БИБЛИОТЕЧНЫЙ МЕНЕДЖЕР___");
        System.out.println("Приложение запущено!");

        // прога работает пока сами не выйдем
        while (isRunning) {
            try {
                showMainMenu();
            } catch (Exception e) {
                // для непредвиденных ошибок в менюшке
                System.out.println("непредвиденная ошибка в меню: " + e.getMessage());
                System.out.println("Возврат в главное меню...");
            }
        }
    }

    // показ главного меню
    private void showMainMenu() {
        System.out.println("\n ___ГЛАВНОЕ МЕНЮ___");
        System.out.println("1. Добавить книгу");
        System.out.println("2. Редактировать книгу");
        System.out.println("3. Показать все книги");
        System.out.println("4. Простой поиск");
        System.out.println("5. Сохранить в файл");
        System.out.println("6. Загрузить из файла");
        System.out.println("7. Продвинутый поиск по атрибутам");
        System.out.println("0. Выход");


        // из InputHelper.java, для ввода корректного интового числа с диапазоном (0, 7)
        int choice = InputHelper.readInt("Выберите пункт меню: ", 0, 7);
        processMenuChoice(choice);
    }

    // обработка выбора из меню
    private void processMenuChoice(int choice) {
        try {
            // Современный switch-expression для обработки выбора
            switch (choice) {
                case 1 -> addBook();             // добавить книгу
                case 2 -> editBook();            // редактировать книгу
                case 3 -> showAllBooks();        // показать все книги
                case 4 -> findBook();            // простой поиск книги (по всем полям)
                case 5 -> saveToFile();          // сохранить в файл
                case 6 -> loadFromFile();        // загрузить из сущ файла
                case 7 -> searchByAttributes();  // продвинутый поиск по атрибутам
                case 0 -> exitProgram();         // завершить работу приложения
                default -> System.out.println("неверный пункт меню, выберите от 0 до 7.");
            }
        } catch (Exception e) {
            // для ошибок в методах меню
            System.out.println("Ошибка при выполнении операции: " + e.getMessage());
        }
    }

    //_______ДОБАВЛЕНИЕ НОВОЙ КНИГИ____////
    private void addBook() {
        System.out.println("\n📖 ___ДОБАВЛЕНИЕ НОВОЙ КНИГИ___");

        // InputHelper.readNonEmptyString для корректности ввода строки (непустой)
        String title = InputHelper.readNonEmptyString("Введите название книги: ");
        String author = InputHelper.readNonEmptyString("Введите автора: ");
        String isbn = InputHelper.readNonEmptyString("Введите ISBN: ");

        // readYear для правильного ввода года (от 1000 до текущго)
        int year = InputHelper.readYear("Введите год издания: ");
        String genre = InputHelper.readNonEmptyString("Введите жанр: ");


        Book newBook = new Book(title, author, isbn, year, genre); //все данные проверены поэтому создаем
        books.add(newBook);

        System.out.println("Книга успешно добавлена!");
        System.out.println(newBook);
    }

    //__________редактирование с выбором полей___
    private void editBook() {
        System.out.println("\n ___РЕДАКТИРОВАНИЕ КНИГИ___");

        if (books.isEmpty()) {
            System.out.println("Список книг пуст -> нечего редактировать.");
            return;
        }


        showAllBooks(); // чтобы выбрать че редактировать

        // получаем проверенный индекс книги которую будем редактирвать
        int index = InputHelper.readListIndex("Введите номер книги для редактирования: ", books.size());

        Book book = books.get(index);

        processFieldSelection(book);  // выбор полей для реадктирования
    }


    private void showFieldSelectionMenu(Book book) {
        System.out.println("\n ___ВЫБЕРИТЕ ПОЛЯ ДЛЯ РЕДАКТИРОВАНИЯ___");
        System.out.println("Текущие данные книги:");
        System.out.println(book);

        System.out.println("\nКакие поля вы хотите изменить?");
        System.out.println("1. Название (текущее: " + book.getTitle() + ")");
        System.out.println("2. Автор (текущий: " + book.getAuthor() + ")");
        System.out.println("3. ISBN (текущий: " + book.getIsbn() + ")");
        System.out.println("4. Год издания (текущий: " + book.getYear() + ")");
        System.out.println("5. Жанр (текущий: " + book.getGenre() + ")");
        System.out.println("6. Завершить редактирование и сохранить");

        System.out.println("\n Введите номера полей через запятую (например: 1,3,5)");
        System.out.println("   или один номер для редактирования одного поля");
    }

    // обработка выбора полей
    private void processFieldSelection(Book book) {
        boolean editing = true;

        while (editing) {
            showFieldSelectionMenu(book);

            String input = InputHelper.readNonEmptyString("Ваш выбор: ");


            if (input.equals("6")) {  // 6 - сохранить и завершить редакктирование
                System.out.println("Изменения сохранены!");
                editing = false;
            } else {
                editSelectedFields(book, input);//редактируем выбранные поля
            }
        }
    }

    //редактирование выбранных полей
    private void editSelectedFields(Book book, String fieldNumbers) {
        try {
            String[] numbers = fieldNumbers.split(",");
            boolean anyFieldEdited = false;  // флаг для всего запроса

            for (String numberStr : numbers) {
                String trimmedNumber = numberStr.trim();

                if (!trimmedNumber.isEmpty()) {
                    int fieldNumber = Integer.parseInt(trimmedNumber);

                    // флаг для конкретного поля
                    boolean fieldWasEdited = switch (fieldNumber) {
                        case 1 -> {
                            String newTitle = InputHelper.readNonEmptyString("Введите новое название: ");
                            book.setTitle(newTitle);
                            System.out.println("Название обновлено!");
                            yield true; // возвращаем tru для fieldNumber и присваиваем результат  fieldWasEdited
                        }
                        case 2 -> {
                            String newAuthor = InputHelper.readNonEmptyString("Введите нового автора: ");
                            book.setAuthor(newAuthor);
                            System.out.println("Автор обновлен!");
                            yield true;
                        }
                        case 3 -> {
                            String newIsbn = InputHelper.readNonEmptyString("Введите новый ISBN: ");
                            book.setIsbn(newIsbn);
                            System.out.println("ISBN обновлен!");
                            yield true;
                        }
                        case 4 -> {
                            int newYear = InputHelper.readYear("Введите новый год издания: ");
                            book.setYear(newYear);
                            System.out.println("Год издания обновлен!");
                            yield true;
                        }
                        case 5 -> {
                            String newGenre = InputHelper.readNonEmptyString("Введите новый жанр: ");
                            book.setGenre(newGenre);
                            System.out.println("Жанр обновлен!");
                            yield true;
                        }
                        default -> {
                            System.out.println("Неверный номер поля: " + fieldNumber + ". Допустимые значения: 1-5");
                            yield false;
                        }
                    };

                    if (fieldWasEdited) {   // если хоть одно поменяли
                        anyFieldEdited = true; // то было редактирование
                    }
                }
            }

            if (anyFieldEdited) {
                System.out.println("Выбранные поля успешно обновлены!");
            } else {
                System.out.println("Не было изменено ни одного поля.");
            }

        } catch (NumberFormatException e) { //ошиббки в преобразовании чисел
            System.out.println("Ошибка! Введите номера полей через запятую (например: 1,3,5)");
        } catch (Exception e) {  // все ошибки
            System.out.println("Ошибка при редактировании: " + e.getMessage());
        }
    }

    // Метод показа всех книг
    private void showAllBooks() {
        System.out.println("\n ___ВСЕ КНИГИ В БИБЛИОТЕКЕ___");

        if (books.isEmpty()) {
            System.out.println("Библиотека пуста. Добавьте первую книгу!");
            return;
        }

        //вывод книг
        for (int i = 0; i < books.size(); i++) {
            System.out.println((i + 1) + ". " + books.get(i));
        }
        System.out.println("Всего книг: " + books.size());
    }



    //_____ПРОДВИНУТЫЙ ПОИСК ПО АТРИБУТАМ____

    private void searchByAttributes() {
        System.out.println("\n ___ПРОДВИНУТЫЙ ПОИСК КНИГ ПО АТРИБУТАМ___");

        if (books.isEmpty()) {
            System.out.println("Библиотека пуста. Нечего искать.");
            return;
        }

        System.out.println("Введите значения для поиска или оставьте пустым чтобы игнорировать поле:");

        // запрашивем поля для поиска (не из ImputHelper так как строка также может быть пустой
        String title = getOptionalInput("Название книги: ");
        String author = getOptionalInput("Автор: ");
        String genre = getOptionalInput("Жанр: ");
        String isbn = getOptionalInput("ISBN: ");

        // года также можем не вводить, чтобы искать по всем годам
        Integer minYear = null;
        Integer maxYear = null;

        System.out.println("\nПоиск по году:");
        System.out.println("Оставьте пустым для любого года");

        String minYearInput = getOptionalInput("   Минимальный год: ");
        String maxYearInput = getOptionalInput("   Максимальный год: ");

        try {

            if (!minYearInput.isEmpty()) minYear = Integer.parseInt(minYearInput); // если не пусто, то сохраняем мин и макс года
            if (!maxYearInput.isEmpty()) maxYear = Integer.parseInt(maxYearInput);
        } catch (NumberFormatException e) { // обработка чисел
            System.out.println("Ошибка. Год должен быть числом (поиск по году будет игнорирован :(");
        }

        // сам поиск
        List<Book> searchResults = performSearch(title, author, genre, isbn, minYear, maxYear);

        // вывод резов
        displaySearchByAttributesResults(searchResults, title, author, genre, isbn, minYear, maxYear);
    }
    // для ввода пустых и не пустых строк
    private String getOptionalInput(String prompt) {
        System.out.print(prompt);
        String input = new Scanner(System.in).nextLine().trim();
        return input;
    }

    // поиск
    private List<Book> performSearch(String title, String author, String genre, String isbn,
                                     Integer minYear, Integer maxYear) {
        List<Book> results = new ArrayList<>();

        for (Book book : books) {
            boolean matches = true; // флаг для проверки по атрибутам

            // Проверяем каждый критерий, если он не пустой и containsIgnoreCase - наличие подстроки в строке без учета регистра
            // если не пусто и containsIgnoreCase выдало false(нет подстроки в строке)  ->  matches = false (то есть эта книга не подходит)

            if (!title.isEmpty() && !containsIgnoreCase(book.getTitle(), title)) {
                matches = false;
            }

            if (!author.isEmpty() && !containsIgnoreCase(book.getAuthor(), author)) {
                matches = false;
            }

            if (!genre.isEmpty() && !containsIgnoreCase(book.getGenre(), genre)) {
                matches = false;
            }

            if (!isbn.isEmpty() && !containsIgnoreCase(book.getIsbn(), isbn)) {
                matches = false;
            }

            // проверяем год по диапазону если н есть
            if (minYear != null && book.getYear() < minYear) {
                matches = false;
            }

            if (maxYear != null && book.getYear() > maxYear) {
                matches = false;
            }

            if (matches) { // если ни разу не опустили флаг, то слово нам подходит
                results.add(book);
            }
        }

        return results;
    }

    // спец метод для проверки подстроки в строке без учета регистра
    // то есть сравнения того, что ищем с тем среди чего ищем
    private boolean containsIgnoreCase(String source, String search) {
        if (source == null || search == null) return false;
        return source.toLowerCase().contains(search.toLowerCase());
    }

    // вывод результатов поиска______

    //передаем массив res с найденными книгами и критерии, которые вводил пользователь
    private void displaySearchByAttributesResults(List<Book> results, String title, String author,
                                      String genre, String isbn, Integer minYear, Integer maxYear) {
        System.out.println("\n ___РЕЗУЛЬТАТЫ ПОИСКА___");

        // критерии, который использовали
        System.out.println("Использованные критерии:");
        List<String> criteria = new ArrayList<>();
        if (!title.isEmpty()) criteria.add("Название: \"" + title + "\"");
        if (!author.isEmpty()) criteria.add("Автор: \"" + author + "\"");
        if (!genre.isEmpty()) criteria.add("Жанр: \"" + genre + "\"");
        if (!isbn.isEmpty()) criteria.add("ISBN: \"" + isbn + "\"");
        if (minYear != null) criteria.add("Год от: " + minYear);
        if (maxYear != null) criteria.add("Год до: " + maxYear);

        if (criteria.isEmpty()) {
            showAllBooks();
        } else {
            for (String criterion : criteria) {
                System.out.println("   " + criterion);
            }
        }

        // вывод найденных книг
        if (results.isEmpty()) {
            System.out.println("\n Книги по заданным критериям не найдены.");
        } else {
            System.out.println("\nНайдено книг: " + results.size());
            for (int i = 0; i < results.size(); i++) {
                System.out.println((i + 1) + ". " + results.get(i));
            }
        }
    }


    //_________СОХРАНЕНИЕ В ФАЙЛ_________

    private void saveToFile() {
        System.out.println("\n💾 ___СОХРАНЕНИЕ В ФАЙЛ___");

        if (books.isEmpty()) {
            System.out.println("список книг пуст -> нечего сохранять");
            return;
        }

        // Запрашиваем имя файла
        String fileName = InputHelper.readNonEmptyString("Введите имя файла для сохранения (например: books.csv): ");

        try {
            // import java.io.*; сделали вначале, чтобы юзать все для работы с файлами
            // Создаем объект для записи в файл

            FileWriter fileWriter = new FileWriter(fileName); // базовый
            PrintWriter writer = new PrintWriter(fileWriter); // умная обертка с четким функционалос

            //  записываем каждую книгу в файл
            for (Book book : books) {
                // CSV формат
                String line = book.getTitle() + "," +
                        book.getAuthor() + "," +
                        book.getIsbn() + "," +
                        book.getYear() + "," +
                        book.getGenre();
                writer.println(line);
            }

            writer.close(); // закрываем файл!!!!
            System.out.println("Книги успешно сохранены в файл: " + fileName);
            System.out.println("Сохранено книг: " + books.size());

        } catch (IOException e) { //ошибка при операциях ввода-вывода
            System.out.println("Ошибка при сохранении файла: " + e.getMessage());
        }
    }


    //________________ЗАГРУЗКА ИЗ ФАЙЛА______
    private void loadFromFile() {
        System.out.println("\n📂 ___ЗАГРУЗКА ИЗ ФАЙЛА___");


        // нужно пождтверждение если книги есть
        if (!books.isEmpty()) {
            System.out.println("В текущей библиотеке уже есть " + books.size() + " книг.");

            // автосохранение данных в auto_save.csv
            System.out.println("Создается резервная копия текущих данных...");
            autoSave();


            //InputHelper.confirm() получает строку делает + "да/нет"
            // затем проссит ввести да или нет, если да -> возвращает true, если нет -> возвращает false
            if (!InputHelper.confirm("Вы уверены, что хотите загрузить новые книги? Старые данные будут записаны в файл auto_save.csv, с датой и временем сессии")) {
                // если  InputHelper.confirm вернуло false(пользователь ввел false) -> загрузку отменяем
                System.out.println("Загрузка отменена.");
                return;
            }
        }

        String fileName = InputHelper.readNonEmptyString("Введите имя файла для загрузки: ");

        try {
            // Создаем объект для чтения файла
            FileReader fileReader = new FileReader(fileName);     //аналогично Writer
            BufferedReader reader = new BufferedReader(fileReader);

            // очищаем текущий списко книг
            books.clear();

            String line;
            int loadedCount = 0; // загруженные строки
            int errorCount = 0; //количество ошибок при загрузке

            // читаем файл построчно
            while ((line = reader.readLine()) != null) {
                try {
                    // пропускаем пустые строки
                    if (line.trim().isEmpty()) {
                        continue;
                    }

                    // в массив по запятым
                    String[] parts = line.split(",");

                    // в строке должно быть 5 полей
                    if (parts.length == 5) {

                        try {
                            // берем из массива по parts по индексам, значения, соответсвующие полям книги (без пробелов)
                            String title = parts[0].trim();   //
                            String author = parts[1].trim();
                            String isbn = parts[2].trim();
                            int year = Integer.parseInt(parts[3].trim());
                            String genre = parts[4].trim();

                            // вызываем конструктор и передаем туда как раз поля книги из файла
                            Book book = new Book(title, author, isbn, year, genre);
                            books.add(book);
                            loadedCount++;
                        } catch (NumberFormatException e) { // форматы строк
                            System.out.println("Ошибка в строке (неверный формат года): " + line);
                            System.out.println("   Причина: " + e.getMessage());
                            errorCount++;
                        } catch (IllegalArgumentException e) { //передаётся недопустимый или неподходящий аргумент
                            System.out.println("Ошибка в строке (неверные данные книги): " + line);
                            System.out.println("   Причина: " + e.getMessage());
                            errorCount++;
                        }
                    } else {
                        System.out.println("Ошибка в строке: " + line);
                        System.out.println("Ожидается 5 полей, в файле: " + parts.length);
                        errorCount++;
                    }

                } catch (Exception e) {
                    System.out.println("Ошибка при обработке строки: " + line);
                    System.out.println("   Причина: " + e.getMessage());
                    errorCount++;
                }
            }

            reader.close(); // Важно: закрываем файл

            System.out.println("Загрузка завершена!");
            System.out.println("Успешно загружено книг: " + loadedCount);
            if (errorCount > 0) {
                System.out.println("Ошибок при загрузке: " + errorCount);
            }

        } catch (FileNotFoundException e) { //невозможность найти или открыть файл по указанному пути.
            System.out.println("Файл не найден: " + fileName);
        } catch (IOException e) { //ошибки в операциях ввода-вывода
            System.out.println("Ошибка при чтении файла: " + e.getMessage());
        }
    }

    // _____АВТОСОХРАНЕНИЕ С ВРЕМЕНЕМ, ДАТОЙ И ИСТОРИЕЙ КНИГ
    private void autoSave() {

        if (books.isEmpty()) {
            return;
        }

        String fileName = "auto_save.csv"; // в него сохраняем

        try {
            // существует ли файл
            File file = new File(fileName);
            boolean fileExists = file.exists();

            // FileWriter(fileName, true если дозаписать, false если перезаписать)
            PrintWriter writer = new PrintWriter(new FileWriter(fileName, true));

            // если файл новый или пустой -> добавляем заголовок
            if (!fileExists || file.length() == 0) {
                writer.println("___АВТОСОХРАНЕНИЕ БИБЛИОТЕКИ___");
                writer.println("Формат:--- СЕССИЯ: дата_время | \n количество книг в библиотеке \n название,автор,isbn,год,жанр");
                writer.println();
            }
            //     writer.println("hello") - печатает строку в файл

            // разделитель с датой и временем____
            String timestamp = java.time.LocalDateTime.now().format(
                    java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            );
            writer.println("--- СЕССИЯ: " + timestamp + " ---");
            writer.println("Книг в библиотеке: " + books.size());

            // Записываем каждую книгу в файл
            for (Book book : books) {
                // формируем строку (, , ,...)
                String line = book.getTitle() + "," +
                        book.getAuthor() + "," +
                        book.getIsbn() + "," +
                        book.getYear() + "," +
                        book.getGenre();
                writer.println(line); //
            }

            // чтоб сессии разделить
            writer.println();

            writer.close(); //после этого данные записываются на диск!!
            System.out.println("Данные автоматически сохранены в файл: " + fileName);
            System.out.println("Время сохранения: " + timestamp);

        } catch (IOException e) {
            System.out.println("Не удалось выполнить автосохранение");
        }
    }


    /// ПРОСТОЙ ПОИСК КНИГИ ПО ВСЕМ ПОЛЯМ-------------
    private void findBook() {
        System.out.println("\n🔍 ___ПРОСТОЙ ПОИСК КНИГИ___");

        if (books.isEmpty()) {
            System.out.println("библиотека пуста -> нечего искать.");
            return;
        }

        System.out.println("Введите поисковый запрос:");
        System.out.println("Поиск будет выполнен по названию, автору, жанру и ISBN");

        String searchQuery = InputHelper.readNonEmptyString("Поиск: ").toLowerCase();

        // созраняем резы поиска в List (performSimpleSearch выдает List)
        List<Book> results = performSimpleSearch(searchQuery);

        // Показываем результаты
        displaySimpleSearchResults(results, searchQuery);
    }

    // АЛГОРИТМ ПОИСКА
    private List<Book> performSimpleSearch(String searchQuery) {
        List<Book> results = new ArrayList<>();

        for (Book book : books) {
            // опять используем containsIgnoreCase (проверяем подстроку запроса в строке с каждым атрибуом каждой книги
            // если хоть одно истинно, то значит добавляем в result эту книгу
            boolean matches = containsIgnoreCase(book.getTitle(), searchQuery) ||
                    containsIgnoreCase(book.getAuthor(), searchQuery) ||
                    containsIgnoreCase(book.getGenre(), searchQuery) ||
                    containsIgnoreCase(book.getIsbn(), searchQuery);

            // Также проверяем год (преобразуем число в строку)
            try {
                String yearString = String.valueOf(book.getYear());
                if (yearString.contains(searchQuery)) {
                    matches = true;
                }
            } catch (Exception e) {
                // Игнорируем ошибки преобразования года
            }

            if (matches) {
                results.add(book);
            }
        }

        return results;
    }

    // вывод результатов простого поиска
    private void displaySimpleSearchResults(List<Book> results, String searchQuery) {
        System.out.println("\n___РЕЗУЛЬТАТЫ ПОИСКА___");
        System.out.println("Поисковый запрос: \"" + searchQuery + "\"");

        if (results.isEmpty()) {
            System.out.println("\nКниги по запросу \"" + searchQuery + "\" не найдены.");
        } else {
            System.out.println("\nНайдено книг: " + results.size());
            for (int i = 0; i < results.size(); i++) {
                System.out.println((i + 1) + ". " + results.get(i));
            }

        }
    }

    // Метод выхода из программы
    private void exitProgram() {
        System.out.println("\n ___ВЫХОД ИЗ ПРОГРАММЫ___");
        //опять InputHelper.confirm() если пользоветль введет да функция вернет true иначе false
        if (InputHelper.confirm("Вы уверены, что хотите выйти?")) {
            // dвтоматическое сохранение перед выходом
            if (!books.isEmpty()) {
                System.out.println("💾 Выполняется автосохранение...");
                autoSave();
            }

            System.out.println("До свидания :)");
            isRunning = false;
        } else {
            System.out.println("Продолжаем работу :)");
        }
    }
}