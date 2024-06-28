import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Server {

    public static HttpServer makeServer() throws IOException {
        String host = "localhost";
        InetSocketAddress address = new InetSocketAddress(host, 9889);
        String msg = "запускаем сервер по адресу" + " http://%s:%s/%n";
        System.out.printf(msg, address.getHostName(), address.getPort());
        HttpServer server = HttpServer.create(address, 50);
        System.out.println("  удачно!");
        return server;
    }

    public static void initRoutes(HttpServer server) {
        server.createContext("/", Server::handleRootRequest);
        server.createContext("/apps/", Server::handleAppsRequest);
        server.createContext("/apps/profile", Server::handleProfileRequest);
    }

    private static void handleRootRequest(HttpExchange exchange) {
        String path = exchange.getRequestURI().getPath();
        if (path.equals("/")) {
            handleRequest(exchange, "Обработчик корневого пути");
        } else {
            handleFileRequest(exchange);
        }
    }

    private static void handleAppsRequest(HttpExchange exchange) {
        handleRequest(exchange, "Обработчик для путии /apps/");
    }

    private static void handleProfileRequest(HttpExchange exchange) {
        handleRequest(exchange, "Обработчик для пути /apps/profile ");
    }

    private static void handleFileRequest(HttpExchange exchange) {
        String filePath = "homework" + exchange.getRequestURI().getPath();
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            handleNotFound(exchange);
            return;
        }

        try {
            String contentType = Files.probeContentType(path);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            byte[] fileData = Files.readAllBytes(path);
            exchange.getResponseHeaders().add("Content-Type", contentType);
            exchange.sendResponseHeaders(200, fileData.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(fileData);
            }
        } catch (IOException e) {
            e.printStackTrace();
            handleServerError(exchange);
        }
    }

    private static void handleNotFound(HttpExchange exchange) {
        try {
            String response = "404 (Not Found)\nТакого документа нет.";
            byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().add("Content-Type", "text/plain; charset=utf-8");
            exchange.sendResponseHeaders(404, responseBytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(responseBytes);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleServerError(HttpExchange exchange) {
        try {
            String response = "500 (Internal Server Error)\nПроизошла ошибка на сервере.";
            exchange.getResponseHeaders().add("Content-Type", "text/plain; charset=utf-8");
            exchange.sendResponseHeaders(500, response.length());
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes(StandardCharsets.UTF_8));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void handleRequest(HttpExchange exchange, String message) {
        try {
            exchange.getResponseHeaders().add("Content-Type", "text/plain; charset=utf-8");
            int responseCode = 200;
            int length = 0;
            exchange.sendResponseHeaders(responseCode, length);
            try (PrintWriter writer = getWriterFrom(exchange)) {
                writer.println(message);
                String method = exchange.getRequestMethod();
                URI uri = exchange.getRequestURI();
                String ctxPath = exchange.getHttpContext().getPath();
                write(writer, "HTTP Метод", method);
                write(writer, "Запрос", uri.toString());
                write(writer, "Обработан через", ctxPath);
                writeHeaders(writer, "Заголовки запроса", exchange.getRequestHeaders());
                writeData(writer, exchange);
                writer.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    private static PrintWriter getWriterFrom(HttpExchange exchange) {
        OutputStream output = exchange.getResponseBody();
        Charset charset = StandardCharsets.UTF_8;
        return new PrintWriter(output, false, charset);
    }



    private static void write(Writer writer, String msg, String method) {
        String data = String.format("%s: %s%n%n", msg, method);
        try {
            writer.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeHeaders(Writer writer, String type, Headers headers) {
        write(writer, type, "");
        headers.forEach((k, v) -> write(writer, "\t" + k, v.toString()));
    }

    private static void writeData(Writer writer, HttpExchange exchange){
        try(BufferedReader reader = getReader(exchange)){
            if(!reader.ready()){
                return;
            }
            write(writer, "Блок данных", "");
            reader.lines().forEach(v -> write(writer, "\t", v));
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private static BufferedReader getReader(HttpExchange exchange) {
        InputStream is = exchange.getRequestBody();
        Charset charset = StandardCharsets.UTF_8;
        InputStreamReader isr = new InputStreamReader(is, charset);
        return new BufferedReader(isr);
    }


}
