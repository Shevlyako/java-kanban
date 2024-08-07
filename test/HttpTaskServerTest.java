package server;

import com.google.gson.Gson;
import exception.NotFoundException;
import manager.Managers;
import modeltask.TaskStatus;
import manager.TaskManager;
import modeltask.Epic;
import modeltask.Subtask;
import modeltask.Task;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpTaskServerTest {

    Gson gson = Managers.getGson();
    TaskManager taskManager;
    HttpTaskServer taskServer;
    HttpClient client = HttpClient.newHttpClient();

    @BeforeEach
    void BeforeEach() throws IOException {
        taskManager = Managers.getDefault();
        taskServer = new HttpTaskServer(taskManager);
        taskServer.start();
    }

    @AfterEach
    void afterEach() {
        taskServer.stop();
    }

    @Test
    public void shouldCreateTask() throws IOException, InterruptedException, NotFoundException {
        Task expectedTask = new Task("Task", "Description",
                Duration.ofMinutes(15), LocalDateTime.of(2024, 12, 12, 12, 12));
        URI url = URI.create("http://localhost:8080/tasks");
        String body = gson.toJson(expectedTask);

        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(body,
                              StandardCharsets.UTF_8)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        expectedTask.setId(1);

        assertEquals(201, response.statusCode(), "Код ответа не совпадает");
        assertEquals(expectedTask, taskManager.getTaskId(1), "Задачи не совпадают");

    }

    @Test
    public void shouldCorrectlyReturnTaskById() throws IOException, InterruptedException {
        Task createdTask = new Task("Task", "Description",
                Duration.ofMinutes(15), LocalDateTime.of(2024, 12, 12, 12, 12));
        URI url = URI.create("http://localhost:8080/tasks/1");
        taskManager.addTask(createdTask);
        String expectedResponse = gson.toJson(createdTask);

        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Код ответа не совпадает");

        assertEquals(expectedResponse, response.body());
    }

    @Test
    public void shouldCorrectlyReturnListOfTasks() throws IOException, InterruptedException {
        Task createdTask = new Task("Task", "Description",
                Duration.ofMinutes(15), LocalDateTime.of(2024, 12, 12, 12, 12));
        Task createdTask2 = new Task("Task", "Description",
                Duration.ofMinutes(15), LocalDateTime.of(2025, 12, 12, 12, 12));
        URI url = URI.create("http://localhost:8080/tasks");
        taskManager.addTask(createdTask);
        taskManager.addTask(createdTask2);
        String expectedResponse = gson.toJson(taskManager.getTasks());

        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        assertEquals(expectedResponse, response.body());
    }

    @Test
    public void shouldCorrectlyDeleteListOfTasks() throws IOException, InterruptedException {
        Task task = new Task("Test 2", "Testing task 2", Duration.ofMinutes(5), LocalDateTime.now());
        taskManager.addTask(task);

        URI url = URI.create("http://localhost:8080/tasks/1");
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(url).method("DELETE", HttpRequest.BodyPublishers.ofString("tasks")).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Код ответа не совпадает");

        List<Task> tasks = taskManager.getTasks();

        assertEquals(tasks.size(), 0, "Задачи не удалена");
    }

    @Test
    public void shouldCorrectlyUpdateTask() throws IOException, InterruptedException, NotFoundException {
        Task createdTask = new Task("Task", "Description",
                Duration.ofMinutes(15), LocalDateTime.of(2024, 12, 12, 12, 12));
        Task updatedTask = new Task("Task", "Description",
                Duration.ofMinutes(15), LocalDateTime.of(2025, 12, 12, 12, 12));
        URI url = URI.create("http://localhost:8080/tasks/1");

        taskManager.addTask(createdTask);
        updatedTask.setId(1);

        String body = gson.toJson(updatedTask);

        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(body, StandardCharsets.UTF_8)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());

        assertEquals(updatedTask, taskManager.getTaskId(1));
    }

    @Test
    public void shouldCreateSubtask() throws IOException, InterruptedException, NotFoundException {
        Epic epic = new Epic("Epic", "Description");
        taskManager.addEpic(epic);
        Subtask expectedSubTask = new Subtask("Task", "Description", Duration.ofMinutes(15),
                        LocalDateTime.of(2024, 12, 12, 12, 12), epic.getId());
        URI url = URI.create("http://localhost:8080/subtasks");
        String body = gson.toJson(expectedSubTask);

        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(body, StandardCharsets.UTF_8)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        expectedSubTask.setId(2);

        assertEquals(expectedSubTask, taskManager.getSubtaskId(2));
    }

    @Test
    public void shouldCorrectlyUpdateSubtask() throws IOException, InterruptedException, NotFoundException {
        Epic epic = new Epic("Epic", "Description");
        taskManager.addEpic(epic);
        Subtask createdSub = new Subtask("Task", "Description", Duration.ofMinutes(20),
                        LocalDateTime.of(2024, 11, 12, 9, 12), epic.getId());
        Subtask updatedSub = new Subtask("Task1", "Description", Duration.ofMinutes(30),
                        LocalDateTime.of(2024, 11, 15, 11, 7), epic.getId());
        URI url = URI.create("http://localhost:8080/subtasks");

        taskManager.addSubTask(createdSub);
        updatedSub.setId(2);

        String body = gson.toJson(updatedSub);

        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(body, StandardCharsets.UTF_8)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        assertEquals(updatedSub, taskManager.getSubtaskId(2));
    }

    @Test
    public void shouldCorrectlyReturnSubtaskById() throws IOException, InterruptedException, NotFoundException {
        Epic epic = new Epic("Epic", "Description");
        taskManager.addEpic(epic);
        Subtask expectedSubTask = new Subtask("Task", "Description", Duration.ofMinutes(15),
                        LocalDateTime.of(2024, 12, 12, 12, 12), epic.getId());
        taskManager.addSubTask(expectedSubTask);

        URI url = URI.create("http://localhost:8080/subtasks/2");
        String expected = gson.toJson(expectedSubTask);

        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(expected, response.body());

    }

    @Test
    public void shouldCorrectlyReturnSubtasksMap() throws IOException, InterruptedException, NotFoundException {

        Epic epic = new Epic("Epic", "Description");
        taskManager.addEpic(epic);
        Subtask subTask1 = new Subtask("Task", "Description", Duration.ofMinutes(15),
                        LocalDateTime.of(2024, 12, 12, 12, 12), epic.getId());
        Subtask subTask2 = new Subtask("Task", "Description", Duration.ofMinutes(15),
                        LocalDateTime.of(2024, 11, 11, 11, 11), epic.getId());
        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);

        URI url = URI.create("http://localhost:8080/subtasks");
        String expected = gson.toJson(taskManager.getSubTasks());

        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(expected, response.body());


    }

    @Test
    public void shouldCreateEpic() throws IOException, InterruptedException, NotFoundException {
        Epic expectedEpic = new Epic("Epic", "Description");
        URI url = URI.create("http://localhost:8080/epics");
        String body = gson.toJson(expectedEpic);


        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(url).method("POST", HttpRequest.BodyPublishers.ofString(body)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());


        assertEquals(201, response.statusCode());
        expectedEpic.setId(1);

        assertEquals(expectedEpic, taskManager.getEpicId(1));
    }

    @Test
    public void shouldCorrectlyReturnEpicById() throws IOException, InterruptedException {
        Epic expectedEpic = new Epic("Epic", "Description");
        URI url = URI.create("http://localhost:8080/epics/1");
        taskManager.addEpic(expectedEpic);
        String expectedResponse = gson.toJson(expectedEpic);

        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        assertEquals(expectedResponse, response.body());
    }

    @Test
    public void shouldCorrectlyReturnEpicsMap() throws IOException, InterruptedException {
        Epic epic1 = new Epic("Epic1", "Description1");
        Epic epic2 = new Epic("Epic2", "Description2");
        URI url = URI.create("http://localhost:8080/epics");
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        String expectedResponse = gson.toJson(taskManager.getEpics());

        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        assertEquals(expectedResponse, response.body());
    }

    @Test
    public void shouldReturnMethodNotAllowedtOnWrongRequestMethod() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks");

        HttpRequest request = HttpRequest.newBuilder().uri(url).PUT(HttpRequest.BodyPublishers.ofString("Test")).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());

    }
}