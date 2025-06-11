package httpserver;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import taskmanager.TaskManager;

import java.io.IOException;

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
            }
            case TaskEndpoint.GET_ID: {
                int taskId = this.getTaskIdFromRequestPath(requestPath);
                String result = this.taskManager.getTaskById(taskId).toString();
                this.sendText(exchange, result);
            }
            case TaskEndpoint.POST: {
//                получить данные,
//                сконвертировать их в Task,
//                проверить есть ли ID,
//                вызвать нужный метод
            }
            case TaskEndpoint.DELETE_ID: {
                int taskId = this.getTaskIdFromRequestPath(requestPath);
                this.taskManager.removeTaskById(taskId);
                this.sendText(exchange, "Задаче с ID " + taskId + " удалена.");
            }
            case TaskEndpoint.UNKNOWN: {
                this.sendBadRequest(exchange, "Получен неверный запрос.");
            }
        }
    }

    private TaskEndpoint getEndpoint(String requestMethod, String requestPath) {
        switch (requestMethod) {
            case "GET" : {
                if (this.pathContainsId(requestPath)) {
                    return TaskEndpoint.GET_ID;
                } else if (this.pathContainsParams(requestPath)) {
                    return TaskEndpoint.UNKNOWN;
                } else {
                    return TaskEndpoint.GET;
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
        return requestPath.split("/").length == 2;
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
