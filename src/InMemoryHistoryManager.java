import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collections;

public class InMemoryHistoryManager implements HistoryManager {
    //    ArrayList<Node> taskHistory;
    HashMap<Integer, Node> taskHistory;
    Node lastNode;

    public InMemoryHistoryManager() {
//        this.taskHistory = new ArrayList<>();
        this.taskHistory = new HashMap<>();
        this.lastNode = null;
    }

    @Override
    public ArrayList<Task> getHistory() {
        return this.getTasks();
    }

    @Override
    public void add(Task task) {
        if (this.taskHistory.containsKey(task.getId())) {
            this.removeNode(this.taskHistory.get(task.getId()));
        }
        this.linkLast(task);
    }

    @Override
    public void remove(int id) {
        if (this.taskHistory.containsKey(id)) {
            this.removeNode(this.taskHistory.get(id));
        }
    }

    void linkLast(Task task) {
        Node newNode;
        if (this.taskHistory.isEmpty()) {
            newNode = new Node(null, task, null);
            this.taskHistory.put(task.getId(), newNode);
        } else {
            Node prevNode = this.lastNode;
            newNode = new Node(prevNode, task, null);
            prevNode.setNextNode(newNode);
            this.taskHistory.put(task.getId(), newNode);
        }
        this.lastNode = newNode;
    }

    ArrayList<Task> getTasks() {
        ArrayList<Task> result = new ArrayList<>();
        Node node = this.lastNode;
        while (node != null) {
            result.add(node.getTask());
            node = node.getPrevNode();
        }
        Collections.reverse(result);
        return result;
    }

    void removeNode(Node node) {
        if (node.equals(this.lastNode)) {
            this.lastNode = node.getPrevNode();
        }
        Node prevNode = node.getPrevNode();
        Node nextNode = node.getNextNode();
        if (prevNode != null) {
            prevNode.setNextNode(nextNode);
        }
        if (nextNode != null) {
            nextNode.setPrevNode(prevNode);
        }
        int taskIdToRemove = node.getTask().getId();
        this.taskHistory.remove(taskIdToRemove);
    }
}