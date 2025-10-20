package model;

public class Book {
    private String title;
    private String author;
    private String isbn;   //уникальный идентификационный номер
    private int year;
    private String genre;

    //КоНсТрУкТоР_-_-_-_-_-_-_-
    public Book(String title, String author, String isbn, int year, String genre) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.year = year;
        this.genre = genre;
    }

    //геттеры, сеттеры
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    //переопределенный метод toString, для того чтобы возвращало в нужном для нас формате
//    public String toString() {
//        return this;
//    }
    // это обычный toString и оон вызвоащает hex код
    // toString() автоматически испольуется например:   в конкатенации строк "Книга: " + book <- автоматически добавляется toString()
    //                                                  в sout
    //                                                  в string.format и так далее
    @Override
    public String toString() {
        return String.format("""
                   Книга:
                   Название: %s
                   Автор: %s
                   ISBN: %s
                   Год: %d
                   Жанр: %s
                """, title, author, isbn, year, genre);
    }
}
