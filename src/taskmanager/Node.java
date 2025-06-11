package taskmanager;

public class Node {
    Node prevNode;
    Task task;
    Node nextNode;

    Node(Node prevNode, Task task, Node nextNode) {
        this.prevNode = prevNode;
        this.task = task;
        this.nextNode = nextNode;
    }

    public Node getPrevNode() {
        return prevNode;
    }

    public void setPrevNode(Node prevNode) {
        this.prevNode = prevNode;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Node getNextNode() {
        return nextNode;
    }

    public void setNextNode(Node nextNode) {
        this.nextNode = nextNode;
    }
}
