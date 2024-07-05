package kg.attractor.java.handlers;
import com.sun.net.httpserver.HttpExchange;
import kg.attractor.java.model.Employee;
import java.io.IOException;
import java.util.Map;


public class Lesson45Server extends Lesson44Server {

    public Lesson45Server(String host, int port) throws IOException {
        super(host, port);
        registerGet("/login", this::loginGet);
        registerPost("/login", this::loginPost);
        registerGet("/profile", this::profileGet);
    }

    private void loginGet(HttpExchange exchange) {
        renderTemplate(exchange, "login.ftlh", null);
    }

    private void loginPost(HttpExchange exchange) {
        String body = getBody(exchange);
        Map<String, String> params = parseFormData(body);

        String identifier = params.get("identifier");
        String password = params.get("password");

        Employee employee = employees.stream()
                .filter(e -> e.getIdentifier().equals(identifier) && e.getPassword().equals(password))
                .findFirst()
                .orElse(null);

        if (employee != null) {
            renderTemplate(exchange, "profile.ftlh", Map.of("employee", employee));
        } else {
            renderTemplate(exchange, "login_failed.ftlh", null);
        }
    }

    private void profileGet(HttpExchange exchange) {
        renderTemplate(exchange, "profile.ftlh", Map.of("employee", new Employee(0, "Некий", "Пользователь", "unknown", "password")));
    }
}
