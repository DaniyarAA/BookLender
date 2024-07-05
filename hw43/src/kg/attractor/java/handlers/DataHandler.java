package kg.attractor.java.handlers;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import kg.attractor.java.model.Book;
import kg.attractor.java.model.Employee;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DataHandler {
    private static final String EMPLOYEES_FILE = "data/employees.json";
    private static final String BOOKS_FILE = "data/books.json";
    private static final Gson gson = new Gson();

    public static List<Employee> loadEmployees() {
        try (Reader reader = new FileReader(EMPLOYEES_FILE)) {
            Type listType = new TypeToken<ArrayList<Employee>>() {}.getType();
            System.out.println("Нашел файл");
            return gson.fromJson(reader, listType);
        } catch (IOException e) {
            System.out.println("Файл с сотрудниками не найден. Создайте сотрудников.");
            return new ArrayList<>();
        }
    }

    public static void saveEmployees(List<Employee> employees) {
        try (Writer writer = new FileWriter(EMPLOYEES_FILE)) {
            gson.toJson(employees, writer);
        } catch (IOException e) {
            System.err.println("Ошибка при сохранении сотрудников: " + e.getMessage());
        }
    }

    public static List<Book> loadBooks() {
        try (Reader reader = new FileReader(BOOKS_FILE)) {
            Type listType = new TypeToken<ArrayList<Book>>() {}.getType();
            return gson.fromJson(reader, listType);
        } catch (IOException e) {
            System.out.println("Файл с книгами не найден. Создайте книги.");
            return new ArrayList<>();
        }
    }

    public static void saveBooks(List<Book> books) {
        try (Writer writer = new FileWriter(BOOKS_FILE)) {
            gson.toJson(books, writer);
        } catch (IOException e) {
            System.err.println("Ошибка при сохранении книг: " + e.getMessage());
        }
    }
}
