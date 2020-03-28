package com.example.yourlibrary_v1.More;

import java.util.ArrayList;
import java.util.List;

public class Book {
    private String description;
    private String thumbnail;
    private String category;
    private String title;
    private String author;
    private String data_publisher;
    private String id_book, info_link;
    private long nr_rating;
    private long page_count;
    private long rating;
    private String image;

    public Book(String title, String category, String book_id, String thumbnail, String author) {
        this.title = title;
        this.category = category;
        id_book = book_id;
        this.thumbnail = thumbnail;
        this.author = author;
    }

    public Book() {
    }

    public Book(String description, String thumbnail, String category, String title, String author, String data_publisher, String info_link, int nr_rating, int page_count, int rating, String image) {
        this.description = description;
        this.thumbnail = thumbnail;
        this.category = category;
        this.title = title;
        this.author = author;
        this.data_publisher = data_publisher;
        this.info_link = info_link;
        this.nr_rating = nr_rating;
        this.page_count = page_count;
        this.rating = rating;
        this.image = image;
    }
    //    public Book(String title, String category, String description, String thumbnail, int info_link, int nr_rating, int page_count, int rating, int image, String author, String data_publisher) {
//        this.title = title;
//        this.category = category;
//        this.description = description;
//        this.thumbnail = thumbnail;
//        this.info_link = info_link;
//        this.nr_rating = nr_rating;
//        this.page_count = page_count;
//        this.rating = rating;
//        this.image = image;
//        this.author = author;
//        this.data_publisher = data_publisher;
//    }

    public String getId_book() {
        return id_book;
    }

    public String getInfo_link() {
        return info_link;
    }

    public long getNr_rating() {
        return nr_rating;
    }

    public long getPage_count() {
        return page_count;
    }

    public long getRating() {
        return rating;
    }

    public String getImage() {
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


