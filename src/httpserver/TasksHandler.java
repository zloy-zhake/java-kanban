package httpserver;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import taskmanager.Task;
import taskmanager.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

public class TasksHandler extends BaseHttpHandler implements HttpHandler {
    public TasksHandler(TaskManager taskManager) {
        super(taskManager);
    }

    enum TaskEndpoint {GET, GET_ID, POST, DELETE_ID, UNKNOWN}

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestPath = exchange.getRequestURI().getPath();
        switch (this.getEndpoint(exchange.getRequestMethod(), requestPath)) {
            case TaskEndpoint.GET : {
                String result = this.taskManager.getTasks().toString();
                this.sendText(exchange, result);
                break;
            }
            case TaskEndpoint.GET_ID: {
                int taskId = this.getTaskIdFromRequestPath(requestPath);
                Task task = this.taskManager.getTaskById(taskId);
                // если taskId = null происходит ошибка
                if (task == null) {
                    this.sendNotFound(exchange, "Запрошенная задача не найдена.");
                } else {
                    this.sendText(exchange, task.toString());
                }
                break;
            }
            case TaskEndpoint.POST: {
//                получить данные,
                InputStream requestBody = exchange.getRequestBody();
                String body = new String(requestBody.readAllBytes(), StandardCharsets.UTF_8);
//                сконвертировать их в Task,
                Gson gson = new GsonBuilder()
                        .setPrettyPrinting()
                        .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                        .registerTypeAdapter(Duration.class, new DurationAdapter())
                        .serializeNulls()
                        .create();
                Task task = gson.fromJson(body, Task.class);
//                проверить есть ли ID,
//                вызвать нужный метод
                if (task.getId() == 0) {
                    int newTaskId = this.taskManager.addTask(task);
                    if (newTaskId == -1) {
                        this.sendHasInteractions(exchange, "Срок выполнения новой задачи пересекается с существующими.");
                    }
                    this.sendCreated(exchange);
                } else {
                    this.taskManager.updateTask(task);
                    this.sendCreated(exchange);
                }
                break;
            }
            case TaskEndpoint.DELETE_ID: {
                int taskId = this.getTaskIdFromRequestPath(requestPath);
                this.taskManager.removeTaskById(taskId);
                this.sendText(exchange, "Задача с ID " + taskId + " удалена.");
                break;
            }
            case TaskEndpoint.UNKNOWN: {
                this.sendBadRequest(exchange, "Получен неверный запрос.");
                break;
            }
        }
    }

    private TaskEndpoint getEndpoint(String requestMethod, String requestPath) {
        switch (requestMethod) {
            case "GET" : {
                if (this.pathContainsId(requestPath)) {
                    return TaskEndpoint.GET_ID;
                } else if (!this.pathContainsParams(requestPath)) {
                    return TaskEndpoint.GET;
                } else {
                    return TaskEndpoint.UNKNOWN;
                }
            }
            case "POST": {
                if (this.pathContainsParams(requestPath)) {
                    return TaskEndpoint.UNKNOWN;
                } else {
                    return TaskEndpoint.POST;
                }
            }
            case "DELETE" : {
                if (this.pathContainsId(requestPath)) {
                    return TaskEndpoint.DELETE_ID;
                } else {
                    return TaskEndpoint.UNKNOWN;
                }
            }
            default: return TaskEndpoint.UNKNOWN;
        }
    }

    private boolean pathContainsParams(String requestPath) {
        int numPathParts = requestPath.split("/").length;
        return numPathParts > 2;
    }

    private boolean pathContainsId(String requestPath) {
        String[] pathParts = requestPath.split("/");
        if (pathParts.length != 3) {
            return false;
        }
        try {
            int id = Integer.parseInt(pathParts[2]);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    private int getTaskIdFromRequestPath(String requestPath) {
        String[] pathParts = requestPath.split("/");
        return Integer.parseInt(pathParts[2]);
    }
}
