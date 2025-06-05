import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class FileBackedTaskManager extends InMemoryTaskManager {
    String backUpFilePath;

    FileBackedTaskManager(String backUpFilePath) {
        super();
        this.backUpFilePath = backUpFilePath;
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        String rawBackUp = "";
        try {
            rawBackUp = Files.readString(file.toPath());
        } catch (IOException e) {
            System.out.println("Ошибка чтения из файла " + file.getName());
        }
        FileBackedTaskManager tm = new FileBackedTaskManager(file.getPath());
        String[] taskStrs = rawBackUp.split("\n");
        for (int i = 1; i < taskStrs.length; i++) {
            Task taskFromStr = tm.fromString(taskStrs[i]);
            if (taskFromStr instanceof Epic) {
                tm.addEpicFromFile((Epic) taskFromStr);
            } else if (taskFromStr instanceof Subtask) {
                tm.addSubtaskFromFile((Subtask) taskFromStr);
            } else {
                tm.addTaskFromFile(taskFromStr);
            }
        }
        return tm;
    }

    @Override
    public void removeAllTasks() {
        super.removeAllTasks();
        this.save();
    }

    @Override
    public void removeAllEpics() {
        super.removeAllEpics();
        this.save();
    }

    @Override
    public void removeAllSubtasks() {
        super.removeAllSubtasks();
        this.save();
    }

    @Override
    public int addTask(Task newTask) {
        super.addTask(newTask);
        this.save();
        return newTask.getId();
    }

    @Override
    public int addSubtask(Subtask newSubtask) {
        super.addSubtask(newSubtask);
        this.save();
        return newSubtask.getId();
    }

    @Override
    public int addEpic(Epic newEpic) {
        super.addEpic(newEpic);
        this.save();
        return newEpic.getId();
    }

    @Override
    public void updateTask(Task newTask) {
        super.updateTask(newTask);
        this.save();
    }

    @Override
    public void updateSubtask(Subtask newSubtask) {
        super.updateSubtask(newSubtask);
        this.save();
    }

    @Override
    public void updateEpic(Epic newEpic) {
        super.updateEpic(newEpic);
        this.save();
    }

    @Override
    public void removeTaskById(int idToRemove) {
        super.removeTaskById(idToRemove);
        this.save();
    }

    @Override
    public void removeSubtaskById(int idToRemove) {
        super.removeSubtaskById(idToRemove);
        this.save();
    }

    @Override
    public void removeEpicById(int idToRemove) {
        super.removeEpicById(idToRemove);
        this.save();
    }

    protected void updateEpicStatus(Epic epicToUpdate) {
        super.updateEpicStatus(epicToUpdate);
        this.save();
    }

    private void addTaskFromFile(Task newTask) {
        this.tasks.put(newTask.getId(), newTask);
    }

    private void addEpicFromFile(Epic newEpic) {
        this.epics.put(newEpic.getId(), newEpic);
    }

    private void addSubtaskFromFile(Subtask newSubtask) {
        this.subtasks.put(newSubtask.getId(), newSubtask);
        Epic connectedEpic = getEpicById(newSubtask.getEpicId());
        connectedEpic.addSubtaskId(newSubtask.getId());
        this.updateEpicStatus(connectedEpic);
    }

    private void save() {
        ArrayList<String> csvLines = new ArrayList<>();
        csvLines.add("id,type,name,status,description,epic,startTime,duration");
        for (Task task : this.getTasks()) {
            csvLines.add(this.taskToCSV(task));
        }
        for (Epic epic : this.getEpics()) {
            csvLines.add(this.taskToCSV(epic));
        }
        for (Subtask subtask : this.getSubtasks()) {
            csvLines.add(this.taskToCSV(subtask));
        }
        try (FileWriter writer = new FileWriter(this.backUpFilePath); BufferedWriter br = new BufferedWriter(writer)) {
            for (String csvLine : csvLines) {
                br.write(csvLine + "\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException();
        }
    }

    private String taskToCSV(Task task) {
        String id = String.valueOf(task.getId());
        String type = task.getClass().getName();
        String name = task.getName();
        String status = task.getStatus().name();
        String description = task.getDescription();
        String epic = "";
        String startTime = "";
        if (task.getStartTime() != null) {
            startTime = task.getStartTime().toString();
        }
        String duration = "";
        if (task.getDuration() != null) {
            duration = task.getDuration().toString();
        }
        if (type.equals("Subtask")) {
            Subtask st = (Subtask) task;
            epic = String.valueOf(st.getEpicId());
        }
        return String.join(",", id, type, name, status, description, epic, startTime, duration);
    }

    private Task fromString(String value) {
        String[] values = value.split(",");
        int id = Integer.parseInt(values[0]);
        String type = values[1];
        String name = values[2];
        Status status = Status.valueOf(values[3]);
        String description = values[4];
        LocalDateTime startTime = LocalDateTime.parse(values[6]);
        Duration duration = Duration.parse(values[7]);

        Task result = new Task(name, description);
        switch (type) {
            case "Task" -> result = new Task(name, description, id, status, startTime, duration);
            case "Epic" ->
                result = new Epic(name, description, id, status, startTime, duration);
            case "Subtask" -> {
                int epic = Integer.parseInt(values[5]);
                result = new Subtask(name, description, id, status, epic);
            }
        }
        return result;
    }
}
