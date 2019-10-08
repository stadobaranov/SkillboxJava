package todo;

import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class ToDoList {
    private final Map<Integer, TaskHolder> taskHolders = new ConcurrentHashMap<>();
    private final AtomicInteger nextId = new AtomicInteger(1);

    public List<ToDoTask> getAll() {
        List<ToDoTask> ordered = new ArrayList<>();

        for(TaskHolder taskHolder: taskHolders.values()) {
            ordered.add(taskHolder.getTask());
        }

        ordered.sort((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()));
        return ordered;
    }

    public ToDoTask get(int id) {
        TaskHolder taskHolder = taskHolders.get(id);
        return taskHolder != null? taskHolder.getTask(): null;
    }

    public void add(ToDoTask task) {
        int id = nextId.getAndIncrement();
        task.setId(id);
        taskHolders.put(id, new TaskHolder(task));
    }

    public boolean update(ToDoTask task) {
        TaskHolder taskHolder = taskHolders.get(task.getId());

        if(taskHolder != null) {
            taskHolder.setTask(task);
            return true;
        }

        return false;
    }

    public boolean remove(int id) {
        return taskHolders.remove(id) != null;
    }

    // Класс, хранящий волатильную ссылку (копию) на задачу. Необходимость дополнительной
    // обертки для хранения задачи заключается в отсутствии метода putIfPresent у класса
    // ConcurrentHashMap и избежании "фантомных" состояний методом update:
    //
    // public ToDoTask get(int id) {
    //     return tasks.get(id); // (1), (4)
    // }
    //
    // public boolean update(ToDoTask task) {
    //     int id = task.getId();
    //
    //     if(tasks.put(id, new ToDoTask(task)) == null) { // (3)
    //         // Задача уже была удалена методом remove.
    //         tasks.remove(id); // (5)
    //         return false;
    //     }
    //
    //     return true;
    // }
    //
    // public boolean remove(int id) {
    //     return tasks.remove(id) != null; // (2)
    // }
    //
    // Если допустить такой порядок действий:
    // (1) Поток1: ToDoTask task1 = get(42);
    //
    // (2) Поток2: remove(42);
    //
    // (3) Поток1: task1.setName("Phantom task");
    //             update(task1); // Зашел в блок if, обнаружив null, но еще не удалил.
    //
    // (4) Поток3: ToDoTask task2 = get(42); // Прочитал "фантомную" задачу.
    //
    // (5) Поток1: update(task1); // Выполнил метод до конца, удалив "фантомную" задачу.
    private static class TaskHolder {
        private volatile ToDoTask task;

        TaskHolder(ToDoTask task) {
            this.task = new ToDoTask(task);
        }

        ToDoTask getTask() {
            return new ToDoTask(task);
        }

        void setTask(ToDoTask task) {
            this.task = new ToDoTask(task);
        }
    }
}
