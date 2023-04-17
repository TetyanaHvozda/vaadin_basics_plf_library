package at.spengergasse.company1.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;

public class Library implements Serializable {

    private ArrayList<Book> books;
    private static Library LibraryInstance = null;

    public Library() {
        books = new ArrayList<>();
    }

    public static Library getInstance(){
        if (LibraryInstance == null){
            LibraryInstance = new Library();
            try {
                LibraryInstance.fillDebugData();
            }catch(LibraryException e){
                e.printStackTrace();
            }
        }return LibraryInstance;
    }

    public ArrayList<Book> getBooks() {
        return new ArrayList<>(books);
    }

    private void fillDebugData() throws LibraryException{
        if(books.size() == 0){
            publish(new Book("Adventures", "test", 12345,
                    LocalDate.of(1994, 7, 2), Book.Category.KINDER, "images/default.jpg"));
            publish(new Book("Python", "python foundations", 1935625,
                    LocalDate.of(2000, 10, 2), Book.Category.FACH_LITERATUR, "images/default.jpg"));
            publish(new Book("Java", "introduction to Java", 18263745,
                    LocalDate.of(2012, 9, 22), Book.Category.FACH_LITERATUR, "images/default.jpg"));
            publish(new Book("Test", "test", 123905,
                    LocalDate.of(2023, 7, 2), Book.Category.DRAMA, "images/default.jpg"));
            publish(new Book("Mystery Murder", "story of a murder", 987655,
                    LocalDate.of(2007, 7, 30), Book.Category.KRIMI, "images/default.jpg"));
        }
    }

    public void publish(Book book) throws LibraryException{
        if (book != null){
            if (!books.contains(book)){
                books.add(book);
            }else {
                throw new LibraryException("already exist");
            }
        }else {
            throw new LibraryException("Fehler: book is null");
        }
    }

    public boolean delete(Book book) throws LibraryException{
        if (book != null){
            if(books.contains(book)){
                books.remove(book);
                return true;
            }else{
                return false;
            }
        }else{
            throw new LibraryException("Fehler: null");
        }
    }

    public boolean delete(Long id) throws LibraryException{
        if (id != null){
            Iterator<Book> b = books.iterator();
            while (b.hasNext()){
                if (id.equals(b.next().getId())){
                    b.remove();
                    return true;
                }
            }
            return false;
        }
        else{
            throw new LibraryException("Error: provided id is null. ");
        }
    }

    public Book getBook(Long id) throws LibraryException{
        return books.stream().filter(e -> e.getId().equals(id))
                .findFirst().orElseThrow(() -> new LibraryException("Error: no book with id " + id + " exists. " ));
    }
}
