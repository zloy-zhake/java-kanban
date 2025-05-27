public class Managers {
    public TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public TaskManager getFileBackedTaskManager(String backUpFileName) {
        return new FileBackedTaskManager(backUpFileName);
    }

    public HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}