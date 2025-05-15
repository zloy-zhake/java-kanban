import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryHistoryManager implements HistoryManager {
    ArrayList<Task> taskHistory;
    ArrayList<Node> linkedHistory;
    HashMap<Integer, Node> indexForLinkedHistory;

    public InMemoryHistoryManager() {
        this.taskHistory = new ArrayList<>();
        this.indexForLinkedHistory = new HashMap<>();
    }

    @Override
    public ArrayList<Task> getHistory() {
        return this.getTasks();
    }

    @Override
    public void add(Task task) {
        if (this.indexForLinkedHistory.containsKey(task.getId())) {
            this.removeNode(this.indexForLinkedHistory.get(task.getId()));
        }
        this.linkLast(task);
    }

//    @Override
//    public void remove(int id) {
//        for (Task task : taskHistory) {
//            if (task.getId() == id) {
//                this.taskHistory.remove(task);
//                break;
//            }
//        }
//    }

    void linkLast(Task task) {
        if (this.linkedHistory.isEmpty()) {
            Node newNode = new Node(null, task, null);
            this.linkedHistory.add(newNode);
            this.indexForLinkedHistory.put(task.getId(), newNode);
        }
        else {
            Node prevNode = this.linkedHistory.getLast();
            Node newNode = new Node(prevNode, task, null);
            this.linkedHistory.add(newNode);
            prevNode.setNextNode(newNode);
            this.indexForLinkedHistory.put(task.getId(), newNode);
        }
    }

    ArrayList<Task> getTasks() {
        ArrayList<Task> result = new ArrayList<>();
        for (Node node : this.linkedHistory) {
            result.add(node.task);
        }
        return result;
    }

    void removeNode(Node node) {
        int taskIdToRemove = node.getTask().getId();
        Node prevNode = node.getPrevNode();
        Node nextNode = node.getNextNode();
        prevNode.setNextNode(nextNode);
        nextNode.setPrevNode(prevNode);
        this.indexForLinkedHistory.remove(taskIdToRemove);
    }
}