package com.example.yourlibrary_v1;

import java.util.ArrayList;
import java.util.List;

public class Book {
    private String description;
    private String thumbnail;
    private String category;
    private String title;
    private String author;
    private String data_publisher;

    public String getIdBook() {
        return idBook;
    }

    private String idBook;
    private int info_link, nr_rating, page_count, rating, image;

    // this constructor we use for fragment home view and rest of the adapters

    public Book(String title, String category, String id_book, String thumbnail, String author) {
        this.title = title;
        this.category = category;
        idBook = id_book;
        this.thumbnail = thumbnail;
        this.author = author;
    }

    public Book() {
    }

    public Book(String title, String category, String description, String thumbnail, int info_link, int nr_rating, int page_count, int rating, int image, String author, String data_publisher) {
        this.title = title;
        this.category = category;
        this.description = description;
        this.thumbnail = thumbnail;
        this.info_link = info_link;
        this.nr_rating = nr_rating;
        this.page_count = page_count;
        this.rating = rating;
        this.image = image;
        this.author = author;
        this.data_publisher = data_publisher;
    }

    public int getInfo_link() {
        return info_link;
    }

    public int getNr_rating() {
        return nr_rating;
    }

    public int getPage_count() {
        return page_count;
    }

    public int getRating() {
        return rating;
    }

    public int getImage() {
        return image;
    }

    public String getAuthor() {
        return author;
    }

    public String getData_publisher() {
        return data_publisher;
    }

    @Override
    public String toString() {
        return "Book{" +
                "Title='" + title + '\'' +
                ", Category='" + category + '\'' +
                ", Description='" + description + '\'' +
                ", Thumbnail=" + thumbnail +
                ", info_link=" + info_link +
                ", nr_rating=" + nr_rating +
                ", page_count=" + page_count +
                ", rating=" + rating +
                ", image=" + image +
                ", author='" + author + '\'' +
                ", data_publisher='" + data_publisher + '\'' +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public ArrayList<Book> filter_book(List<Book> list_receive, String txt) {
        ArrayList<Book> list_sort = new ArrayList<>();
        for (int i = 0; i < list_receive.size(); i++) {
            if (list_receive.get(i).getTitle().toLowerCase().contains(txt.toLowerCase())) {
                list_sort.add(list_receive.get(i));
            }

        }

        return list_sort;
    }

    public ArrayList<Book> sort_books_after_rating(ArrayList<Book> sort_books) {
        boolean swapped = false;
        for (int i = 0; i < sort_books.size(); i++) {
            swapped = false;
            for (int j = 0; j < sort_books.size() - 1 - i; i++) {
                if (sort_books.get(j).rating > sort_books.get(j + 1).rating) {
                    Book temp = sort_books.get(j);
                    sort_books.set(j, sort_books.get(j + 1));
                    sort_books.set(j + 1, temp);
                    swapped = true;
                }
            }
            if (!swapped) {
                break;
            }
        }
        return sort_books;
    }
}


