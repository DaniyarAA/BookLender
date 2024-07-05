package kg.attractor.java;

import kg.attractor.java.handlers.Lesson44Server;
import kg.attractor.java.handlers.Lesson45Server;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            new Lesson45Server("localhost", 9889).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
