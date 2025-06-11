package httpserver;

import com.sun.net.httpserver.HttpServer;
import taskmanager.Managers;

import java.io.IOException;
import java.net.InetSocketAddress;


public class HttpTaskServer {
    private static final int PORT = 8080;

    public static void main(String[] args) throws IOException {
        Managers taskManagerUtil = new Managers();
        taskmanager.TaskManager taskManager = taskManagerUtil.getDefault();

        HttpServer httpServer = null;
        try {
            httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        } catch (IOException e) {
            System.out.println("Произошла ошибка при запуске сервера: " + e);
            return;
        }
        httpServer.createContext("/tasks", new TasksHandler(taskManager));
        httpServer.createContext("/subtasks", new SubtasksHandler(taskManager));
        httpServer.createContext("/epics", new EpicsHandler(taskManager));
        httpServer.createContext("/history", new HistoryHandler(taskManager));
        httpServer.createContext("/prioritized", new PrioritizedHandler(taskManager));
        httpServer.start();
    }
}
