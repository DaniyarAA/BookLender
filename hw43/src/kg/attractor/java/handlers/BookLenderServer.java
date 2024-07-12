package kg.attractor.java.handlers;

import com.sun.net.httpserver.HttpExchange;
import kg.attractor.java.common.Utils;
import kg.attractor.java.model.Book;
import kg.attractor.java.model.Employee;
import kg.attractor.java.server.BasicServer;
import kg.attractor.java.server.Cookie;


import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class BookLenderServer extends BasicServer {

    private static final List<Book> books = DataHandler.loadBooks();
    private static final List<Employee> employees = DataHandler.loadEmployees();


    public BookLenderServer(String host, int port) throws IOException {
        super(host, port);
        registerGet("/register", this::registerGetHandler);
        registerPost("/register", this::registerPostHandler);
        registerGet("/login", this::loginGetHandler);
        registerPost("/login", this::loginPostHandler);
        registerGet("/profile", this::profileGetHandler);
        registerGet("/borrow", this::borrowGetHandler);
        registerPost("/borrow", this::borrowPostHandler);
        registerGet("/return", this::returnGetHandler);
        registerPost("/return", this::returnPostHandler);
        registerGet("/logout", this::logoutGetHandler);
        registerGet("/books", this::booksHandler);
        registerGet("/book", this::handleBookDetailRequest);
        registerGet("/employees", this::employeesHandler);
        registerGet("/employee", this::employeeDetailHandler);
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
            DataHandler.saveEmployees(employees);
            renderTemplate(exchange, "register_success.ftlh", Map.of("employee", newEmployee));
        }
    }

    private void loginGetHandler(HttpExchange exchange) {
        renderTemplate(exchange, "login.ftlh", null);
    }

    private void loginPostHandler(HttpExchange exchange) {
        String body = getBody(exchange);
        Map<String, String> params = parseFormData(body);

        String identifier = params.get("identifier");
        String password = params.get("password");

        Employee employee = employees.stream()
                .filter(e -> e.getIdentifier().equals(identifier) && e.getPassword().equals(password))
                .findFirst()
                .orElse(null);

        if (employee != null) {
            String sessionId = UUID.randomUUID().toString();
            SessionHandler.addSession(sessionId, identifier);
            Cookie<String> sessionCookie = Cookie.make("sessionId", sessionId);
            sessionCookie.setMaxAge(600);
            sessionCookie.setHttpOnly(true);
            setCookie(exchange, sessionCookie);
            redirect303(exchange, "/profile");
        } else {
            renderTemplate(exchange, "login_failed.ftlh", null);
        }
    }

    private void profileGetHandler(HttpExchange exchange) {
        if (!isAuthenticated(exchange)) {
            redirect303(exchange, "/login");
            return;
        }
        String sessionId = getSessionId(exchange);
        String identifier = SessionHandler.getUserIdentifier(sessionId);
        Employee employee = employees.stream()
                .filter(e -> e.getIdentifier().equals(identifier))
                .findFirst()
                .orElse(null);

        if (employee != null) {
            renderTemplate(exchange, "profile.ftlh", Map.of("employee", employee, "books", books));
        } else {
            renderTemplate(exchange, "profile.ftlh", Map.of("employee", new Employee(0, "Некий", "Пользователь", "unknown", "password")));
        }
    }

    private void booksHandler(HttpExchange exchange) {
        renderTemplate(exchange, "books.ftlh", Map.of("books", books));
    }

    private void handleBookDetailRequest(HttpExchange exchange) {
        String queryParams = getQueryParams(exchange);
        Map<String, String> params = Utils.parseUrlEncoded(queryParams, "&");
        String bookIdStr = params.get("id");
        if (bookIdStr != null) {
            try {
                int bookId = Integer.parseInt(bookIdStr);
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
        String queryParams = getQueryParams(exchange);
        Map<String, String> params = Utils.parseUrlEncoded(queryParams, "&");
        String employeeIdStr = params.get("id");
        if (employeeIdStr != null) {
            try {
                int employeeId = Integer.parseInt(employeeIdStr);
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

    private void borrowGetHandler(HttpExchange exchange) {
        if (!isAuthenticated(exchange)) {
            redirect303(exchange, "/login");
            return;
        }
        renderTemplate(exchange, "borrow.ftlh", Map.of("books", books));
    }

    private void borrowPostHandler(HttpExchange exchange) {
        if (!isAuthenticated(exchange)) {
            redirect303(exchange, "/login");
            return;
        }

        String body = getBody(exchange);
        Map<String, String> params = parseFormData(body);

        int bookId = Integer.parseInt(params.get("bookId"));
        String sessionId = getSessionId(exchange);
        String identifier = SessionHandler.getUserIdentifier(sessionId);

        Employee employee = employees.stream()
                .filter(e -> e.getIdentifier().equals(identifier))
                .findFirst()
                .orElse(null);

        if (employee != null) {
            if (employee.getCurrentBooks().size() >= 2) {
                renderTemplate(exchange, "borrow_failed.ftlh", Map.of("message", "Не больше двух книг в одни руки."));
                return;
            }

            Book book = books.stream()
                    .filter(b -> b.getId() == bookId && !b.isBorrowed())
                    .findFirst()
                    .orElse(null);

            if (book != null) {
                book.setBorrowed(true);
                book.setBorrowedBy(employee.getFirstName());
                employee.borrowBook(book.getId());
                DataHandler.saveBooks(books);
                DataHandler.saveEmployees(employees);
                renderTemplate(exchange, "borrow_success.ftlh", Map.of("book", book));
            } else {
                renderTemplate(exchange, "borrow_failed.ftlh", Map.of("message", "Книга недоступна."));
            }
        }
    }

    private void returnGetHandler(HttpExchange exchange) {
        if (!isAuthenticated(exchange)) {
            redirect303(exchange, "/login");
            return;
        }
        String sessionId = getSessionId(exchange);
        String identifier = SessionHandler.getUserIdentifier(sessionId);
        Employee employee = employees.stream()
                .filter(e -> e.getIdentifier().equals(identifier))
                .findFirst()
                .orElse(null);

        if (employee != null) {
            List<Book> borrowedBooks = books.stream()
                    .filter(b -> employee.getCurrentBooks().contains(b.getId()))
                    .collect(Collectors.toList());
            renderTemplate(exchange, "return.ftlh", Map.of("books", borrowedBooks));
        }
    }

    private void returnPostHandler(HttpExchange exchange) {
        if (!isAuthenticated(exchange)) {
            redirect303(exchange, "/login");
            return;
        }

        String body = getBody(exchange);
        Map<String, String> params = parseFormData(body);

        int bookId = Integer.parseInt(params.get("bookId"));
        String sessionId = getSessionId(exchange);
        String identifier = SessionHandler.getUserIdentifier(sessionId);

        Employee employee = employees.stream()
                .filter(e -> e.getIdentifier().equals(identifier))
                .findFirst()
                .orElse(null);

        if (employee != null) {
            Book book = books.stream()
                    .filter(b -> b.getId() == bookId && b.isBorrowed() && b.getBorrowedBy().equals(employee.getFirstName()))
                    .findFirst()
                    .orElse(null);

            if (book != null) {
                book.setBorrowed(false);
                book.setBorrowedBy("none");
                employee.returnBook(book.getId());
                DataHandler.saveBooks(books);
                DataHandler.saveEmployees(employees);
                renderTemplate(exchange, "return_success.ftlh", Map.of("book", book));
            } else {
                renderTemplate(exchange, "return_failed.ftlh", Map.of("message", "Книга не найдена или не принадлежит пользователю."));
            }
        }
    }


    private void logoutGetHandler(HttpExchange exchange) {
        String sessionId = getSessionId(exchange);
        if (sessionId != null) {
            SessionHandler.removeSession(sessionId);
            Cookie<String> sessionCookie = Cookie.make("sessionId", "");
            sessionCookie.setMaxAge(0);
            sessionCookie.setHttpOnly(true);
            setCookie(exchange, sessionCookie);
        }
        redirect303(exchange, "/login");
    }

    private boolean isAuthenticated(HttpExchange exchange) {
        String sessionId = getSessionId(exchange);
        return sessionId != null && SessionHandler.containsSession(sessionId);
    }

    private String getSessionId(HttpExchange exchange) {
        String cookieStr = getCookies(exchange);
        Map<String, String> cookies = Cookie.parse(cookieStr);
        return cookies.get("sessionId");
    }

    protected static String getCookies(HttpExchange exchange) {
        return exchange.getRequestHeaders()
                .getOrDefault("Cookie", List.of(""))
                .get(0);
    }

    protected void setCookie(HttpExchange exchange, Cookie cookie) {
        exchange.getResponseHeaders().add("Set-Cookie", cookie.toString());
    }

    protected String getQueryParams(HttpExchange exchange) {
        String query = exchange.getRequestURI().getQuery();
        return Objects.nonNull(query) ? query : "";
    }
}