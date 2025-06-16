package httpservertests;

import com.google.gson.Gson;
import httpserver.HttpTaskServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskmanager.Epic;
import taskmanager.InMemoryTaskManager;
import taskmanager.Status;
import taskmanager.TaskManager;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpTaskManagerEpicsTest {
    TaskManager manager = new InMemoryTaskManager();
    HttpTaskServer taskServer = new HttpTaskServer(manager);
    Gson taskAdaptedGson = HttpTaskServer.getTaskAdaptedGson();

    public HttpTaskManagerEpicsTest() {
    }

    @BeforeEach
    public void setUp() {
        manager.removeAllTasks();
        manager.removeAllSubtasks();
        manager.removeAllEpics();
        taskServer.start();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    @Test
    public void testGetEpics() throws IOException, InterruptedException {
        Epic epic = new Epic("epic", "description");
        manager.addEpic(epic);
        HttpResponse<String> response;
        try (HttpClient client = HttpClient.newHttpClient()) {
            URI url = URI.create("http://localhost:8080/epics");
            HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        }
        assertEquals(200, response.statusCode());
        String expectedString = manager.getEpics().toString();
        assertEquals(expectedString, response.body());
    }

    @Test
    public void testGetEpicById() throws IOException, InterruptedException {
        Epic epic = new Epic("epic", "description");
        manager.addEpic(epic);
        int epicId = manager.getEpics().getLast().getId();
        HttpResponse<String> response;
        try (HttpClient client = HttpClient.newHttpClient()) {
            URI url = URI.create("http://localhost:8080/epics/" + epicId);
            HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        }
        assertEquals(200, response.statusCode());
        String expectedString = manager.getEpics().getLast().toString();
        assertEquals(expectedString, response.body());
    }

    @Test
    public void testPostNewEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("epic", "description");
        String epicJson = taskAdaptedGson.toJson(epic);
        HttpResponse<String> response;
        try (HttpClient client = HttpClient.newHttpClient()) {
            URI url = URI.create("http://localhost:8080/epics/");
            HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(epicJson)).build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        }
        assertEquals(201, response.statusCode());
        assertEquals(1, manager.getEpics().size());
    }

    @Test
    public void testPostUpdatedEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("epic", "description");
        manager.addEpic(epic);
        int epicId = manager.getEpics().getLast().getId();
        ArrayList<Integer> subtaskIds = manager.getEpics().getLast().getSubtaskIds();
        Epic updatedEpic = new Epic("epic", "description", epicId, Status.IN_PROGRESS, LocalDateTime.now(), Duration.ofHours(1), subtaskIds);
        String updatedEpicJson = taskAdaptedGson.toJson(updatedEpic);
        HttpResponse<String> response;
        try (HttpClient client = HttpClient.newHttpClient()) {
            URI url = URI.create("http://localhost:8080/epics/");
            HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(updatedEpicJson)).build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        }
        assertEquals(201, response.statusCode());
        assertEquals(1, manager.getEpics().size());
    }

    @Test
    public void testDeleteEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("epic", "description");
        manager.addEpic(epic);
        int epicId = manager.getEpics().getLast().getId();
        HttpResponse<String> response;
        try (HttpClient client = HttpClient.newHttpClient()) {
            URI url = URI.create("http://localhost:8080/epics/" + epicId);
            HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        }
        assertEquals(200, response.statusCode());
        assertEquals(0, manager.getEpics().size());
    }
}
