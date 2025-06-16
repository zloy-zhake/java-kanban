package taskmanager;

import httpserver.HttpTaskServer;

public class Main {

    public static void main(String[] args) {
        Managers taskManagerUtil = new Managers();
        taskmanager.TaskManager taskManager = taskManagerUtil.getDefault();
        HttpTaskServer httpTaskServer = new HttpTaskServer(taskManager);
        httpTaskServer.start();
    }
}