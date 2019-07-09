package task;

import java.io.IOException;


public class Main {

    public static void main(String[] args) throws InterruptedException {

        ConsoleController consoleController= new ConsoleController();
        try {
            consoleController.action();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


