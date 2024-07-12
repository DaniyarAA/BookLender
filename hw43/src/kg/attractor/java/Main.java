package kg.attractor.java;
import kg.attractor.java.handlers.BookLenderServer;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            new BookLenderServer("localhost", 9889).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
