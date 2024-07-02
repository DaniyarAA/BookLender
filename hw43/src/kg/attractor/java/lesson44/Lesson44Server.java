package kg.attractor.java.lesson44;

import com.sun.net.httpserver.HttpExchange;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import kg.attractor.java.server.BasicServer;
import kg.attractor.java.server.ContentType;
import kg.attractor.java.server.ResponseCodes;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class Lesson44Server extends BasicServer {

    private static final Configuration freemarker = initFreeMarker();
    private static final List<Book> books = List.of(
            new Book(1, "Атомные привычки", "Джеймс Клир", "book1.jpg"),
            new Book(2, "Накопительный эффект", "Даррен Харди", "book2.jpg")
    );

    private static final List<Employee> employees = List.of(
            new Employee(1, "Бекболот", "Нурманбетов"),
            new Employee(2, "Нурбек", "Максатбеков")
    );

    public Lesson44Server(String host, int port) throws IOException {
        super(host, port);
        registerGet("/books", this::booksHandler);
        registerGet("/books/", this::bookDetailHandler);
        registerGet("/employees", this::employeesHandler);
        registerGet("/employees/", this::employeeDetailHandler);
    }

    private static Configuration initFreeMarker() {
        try {
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_29);
            cfg.setDirectoryForTemplateLoading(new File("data"));
            cfg.setDefaultEncoding("UTF-8");
            cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            cfg.setLogTemplateExceptions(false);
            cfg.setWrapUncheckedExceptions(true);
            cfg.setFallbackOnNullLoopVariable(false);
            return cfg;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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


    private void renderTemplate(HttpExchange exchange, String templateFile, Map<String, Object> dataModel) {
        try {
            Template template = freemarker.getTemplate(templateFile);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            try (OutputStreamWriter writer = new OutputStreamWriter(stream)) {
                template.process(dataModel, writer);
                writer.flush();
                byte[] data = stream.toByteArray();
                sendByteData(exchange, ResponseCodes.OK, ContentType.TEXT_HTML, data);
            }
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }
    }

}