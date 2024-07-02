package kg.attractor.java.lesson44;

import java.util.ArrayList;
import java.util.List;


public class Employee {
    private int id;
    private String firstName;
    private String lastName;
    private List<Integer> currentBooks;
    private List<Integer> historyBooks;

    public Employee(int id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.currentBooks = new ArrayList<>();
        this.historyBooks = new ArrayList<>();
    }


    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public List<Integer> getCurrentBooks() {
        return currentBooks;
    }

    public List<Integer> getHistoryBooks() {
        return historyBooks;
    }

    public void borrowBook(int bookId) {
        currentBooks.add(bookId);
    }

    public void returnBook(int bookId) {
        currentBooks.remove(Integer.valueOf(bookId));
        historyBooks.add(bookId);
    }
}
