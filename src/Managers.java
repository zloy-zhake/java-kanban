public class Managers {
    public TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
