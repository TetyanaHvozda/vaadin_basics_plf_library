package at.spengergasse.company1.model;

import java.io.Serializable;
import java.time.LocalDate;

public class Book implements Serializable {

    private static Long idCounter = 0L;
    public enum Category{
        KRIMI,
        DRAMA,
        SCIENCE_FICTION,
        KINDER,
        FACH_LITERATUR
    }

    private Long id;
    private String title;
    private String description;
    private int isbn;
    private LocalDate dateOfPublish;
    private Category category;
    private static String imageUrl;

    public Book(String title, String description, int isbn, LocalDate dateOfPublish, Category category, String imageUrl) throws LibraryException{
        setId(Book.idCounter++);
        setTitle(title);
        setDescription(description);
        setIsbn(isbn);
        setDateOfPublish(dateOfPublish);
        setCategory(category);
        setImageUrl(imageUrl);
    }

    public static Long getIdCounter() {
        return idCounter;
    }

    public static void setIdCounter(Long idCounter) {
        Book.idCounter = idCounter;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIsbn() {
        return isbn;
    }

    public void setIsbn(int isbn) {
        this.isbn = isbn;
    }

    public LocalDate getDateOfPublish() {
        return dateOfPublish;
    }

    public void setDateOfPublish(LocalDate dateOfPublish) {
        this.dateOfPublish = dateOfPublish;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
    public static String getImageUrl() {
        if (imageUrl == null) {
            // If no image URL is available, return a default image URL
            return "images/default.jpg";
        } else {
            return imageUrl;
        }
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
