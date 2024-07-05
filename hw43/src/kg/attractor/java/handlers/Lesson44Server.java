package kg.attractor.java.handlers;
import com.sun.net.httpserver.HttpExchange;
import kg.attractor.java.model.Book;
import kg.attractor.java.model.Employee;
import kg.attractor.java.server.BasicServer;
import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Lesson44Server extends BasicServer {

    protected static List<Book> books = DataHandler.loadBooks();
    protected static List<Employee> employees = DataHandler.loadEmployees();

    public Lesson44Server(String host, int port) throws IOException {
        super(host, port);
        registerGet("/books", this::booksHandler);
        registerGet("/books/*", this::bookDetailHandler);
        registerGet("/employees", this::employeesHandler);
        registerGet("/employees/*", this::employeeDetailHandler);
        registerGet("/register", this::registerGetHandler);
        registerPost("/register", this::registerPostHandler);
    }

    private void booksHandler(HttpExchange exchange) {
        renderTemplate(exchange, "books.ftlh", Map.of("books", books));
    }

    private void bookDetailHandler(HttpExchange exchange) {
        String path = exchange.getRequestURI().getPath();
        if (path.startsWith("/books/") && path.length() > "/books/".length()) {
            try {
                int bookId = Integer.parseInt(path.substring("/books/".length()));
                Book book = books.stream().filter(b -> b.getId() == bookId).findFirst().orElse(null);
                if (book != null) {
                    renderTemplate(exchange, "book.ftlh", Map.of("book", book));
                } else {
                    respond404(exchange);
                }
            } catch (NumberFormatException e) {
                respond404(exchange);
            }
        } else {
            respond404(exchange);
        }
    }

    private void employeesHandler(HttpExchange exchange) {
        renderTemplate(exchange, "employees.ftlh", Map.of("employees", employees, "books", books));
    }

    private void employeeDetailHandler(HttpExchange exchange) {
        String path = exchange.getRequestURI().getPath();
        if (path.startsWith("/employees/") && path.length() > "/employees/".length()) {
            try {
                int employeeId = Integer.parseInt(path.substring("/employees/".length()));
                Employee employee = employees.stream().filter(e -> e.getId() == employeeId).findFirst().orElse(null);
                if (employee != null) {
                    renderTemplate(exchange, "employee.ftlh", Map.of("employee", employee, "books", books));
                } else {
                    respond404(exchange);
                }
            } catch (NumberFormatException e) {
                respond404(exchange);
            }
        } else {
            respond404(exchange);
        }
    }


    private void registerGetHandler(HttpExchange exchange) {
        renderTemplate(exchange, "register.ftlh", null);
    }

    private void registerPostHandler(HttpExchange exchange) {
        String body = getBody(exchange);
        Map<String, String> params = parseFormData(body);

        String identifier = params.get("identifier");
        String firstName = params.get("firstName");
        String lastName = params.get("lastName");
        String password = params.get("password");

        boolean userExists = employees.stream().anyMatch(e -> e.getIdentifier().equals(identifier));

        if (userExists) {
            renderTemplate(exchange, "register_failed.ftlh", null);
        } else {
            int newId = employees.size() + 1;
            Employee newEmployee = new Employee(newId, firstName, lastName, identifier, password);
            employees.add(newEmployee);
            System.out.println("Пытаюсь сохранить");
            DataHandler.saveEmployees(employees);
            renderTemplate(exchange, "register_success.ftlh", Map.of("employee", newEmployee));
            System.out.println("успешно сохраняю");
        }
    }

    protected Map<String, String> parseFormData(String formData) {
        return Arrays.stream(formData.split("&"))
                .map(s -> s.split("="))
                .collect(Collectors.toMap(
                        a -> a[0],
                        a -> a.length > 1 ? URLDecoder.decode(a[1], StandardCharsets.UTF_8) : ""
                ));
    }
}
