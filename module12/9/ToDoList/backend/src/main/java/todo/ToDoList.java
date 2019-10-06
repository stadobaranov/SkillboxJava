package todo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ToDoList {
    private final Map<Integer, ToDoTask> tasks = new HashMap<>();
    private int nextId = 1;

    public List<ToDoTask> getAll() {
        List<ToDoTask> ordered = new ArrayList<>(tasks.values());
        ordered.sort((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()));
        return ordered;
    }

    public ToDoTask get(int id) {
        return tasks.get(id);
    }

    public void add(ToDoTask task) {
        int id = nextId++;
        tasks.put(id, task);
        task.setId(id);
    }

    public boolean remove(int id) {
        return tasks.remove(id) != null;
    }
}
