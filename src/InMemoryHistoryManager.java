import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryHistoryManager implements HistoryManager {
    ArrayList<Node> taskHistory;
    HashMap<Integer, Node> indexForTaskHistory;

    public InMemoryHistoryManager() {
        this.taskHistory = new ArrayList<>();
        this.indexForTaskHistory = new HashMap<>();
    }

    @Override
    public ArrayList<Task> getHistory() {
        return this.getTasks();
    }

    @Override
    public void add(Task task) {
        if (this.indexForTaskHistory.containsKey(task.getId())) {
            this.removeNode(this.indexForTaskHistory.get(task.getId()));
        }
        this.linkLast(task);
    }

    @Override
    public void remove(int id) {
        if (this.indexForTaskHistory.containsKey(id)) {
            this.removeNode(this.indexForTaskHistory.get(id));
        }
    }

    void linkLast(Task task) {
        if (this.taskHistory.isEmpty()) {
            Node newNode = new Node(null, task, null);
            this.taskHistory.add(newNode);
            this.indexForTaskHistory.put(task.getId(), newNode);
        }
        else {
            Node prevNode = this.taskHistory.getLast();
            Node newNode = new Node(prevNode, task, null);
            this.taskHistory.add(newNode);
            prevNode.setNextNode(newNode);
            this.indexForTaskHistory.put(task.getId(), newNode);
        }
    }

    ArrayList<Task> getTasks() {
        ArrayList<Task> result = new ArrayList<>();
        for (Node node : this.taskHistory) {
            result.add(node.task);
        }
        return result;
    }

    void removeNode(Node node) {
        int taskIdToRemove = node.getTask().getId();
        Node prevNode = node.getPrevNode();
        Node nextNode = node.getNextNode();
        if (prevNode != null) {
            prevNode.setNextNode(nextNode);
        }
        if (nextNode != null) {
            nextNode.setPrevNode(prevNode);
        }
        this.indexForTaskHistory.remove(taskIdToRemove);
    }
}