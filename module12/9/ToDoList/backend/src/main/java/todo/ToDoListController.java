package todo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import todo.request.CompleteToDoTaskRequest;
import todo.request.CreateToDoTaskRequest;
import todo.request.RenameToDoTaskRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
public class ToDoListController {
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    private final ToDoList tasks = new ToDoList();

    @GetMapping("/tasks")
    public List<ToDoTask> getAll() {
        return tasks.getAll();
    }

    @GetMapping("/tasks/{id}")
    public ResponseEntity<ToDoTask> get(@PathVariable("id") int id) {
        ToDoTask task = tasks.get(id);

        if(task != null) {
            return ResponseEntity.ok(task);
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping(value = "/tasks")
    public ToDoTask create(@RequestBody CreateToDoTaskRequest request) {
        ToDoTask task = new ToDoTask();
        task.setName(request.getName());
        tasks.add(task);
        return task;
    }

    @PatchMapping("/tasks/{id}/complete")
    public ResponseEntity<String> complete(@PathVariable("id") int id, @RequestBody CompleteToDoTaskRequest request) {
        ToDoTask task = tasks.get(id);

        if(task != null) {
            task.complete(request.getValue());

            LocalDateTime completedAt = task.getCompletedAt();
            return ResponseEntity.ok(completedAt != null? completedAt.format(dateTimeFormatter): null);
        }

        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/tasks/{id}/rename")
    public ResponseEntity<Void> rename(@PathVariable("id") int id, @RequestBody RenameToDoTaskRequest request) {
        ToDoTask task = tasks.get(id);

        if(task != null) {
            task.setName(request.getName());
            return ResponseEntity.ok(null);
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") int id) {
        if(tasks.remove(id)) {
            return ResponseEntity.ok(null);
        }

        return ResponseEntity.notFound().build();
    }
}
