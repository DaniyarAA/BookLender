package kg.attractor.java.model;

import java.util.ArrayList;
import java.util.List;



import java.util.ArrayList;
import java.util.List;

public class Employee {
    private int id;
    private String firstName;
    private String lastName;
    private String identifier;
    private String password;
    private List<Integer> currentBooks;
    private List<Integer> historyBooks;

    public Employee(int id, String firstName, String lastName, String identifier, String password) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.identifier = identifier;
        this.password = password;
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

    public String getIdentifier() {
        return identifier;
    }

    public String getPassword() {
        return password;
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

