package todo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
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

    private final ToDoTaskService taskService;

    @Autowired
    public ToDoListController(ToDoTaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/tasks")
    public List<ToDoTask> getAll() {
        return taskService.getAll();
    }

    @GetMapping("/tasks/{id}")
    public ResponseEntity<ToDoTask> get(@PathVariable("id") int id) {
        ToDoTask task = taskService.get(id);

        if(task != null) {
            return ResponseEntity.ok(task);
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping(value = "/tasks")
    public ToDoTask create(@RequestBody CreateToDoTaskRequest request) {
        ToDoTask task = new ToDoTask();
        task.setName(request.getName());
        task.setCreatedAt(LocalDateTime.now());
        taskService.create(task);
        return task;
    }

    @Transactional
    @PatchMapping("/tasks/{id}/complete")
    public ResponseEntity<String> complete(@PathVariable("id") int id, @RequestBody CompleteToDoTaskRequest request) {
        LocalDateTime completedAt = request.getValue()? LocalDateTime.now(): null;

        if(taskService.completeById(id, completedAt)) {
            return ResponseEntity.ok(completedAt != null? completedAt.format(dateTimeFormatter): null);
        }

        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/tasks/{id}/rename")
    public ResponseEntity<Void> rename(@PathVariable("id") int id, @RequestBody RenameToDoTaskRequest request) {
        return taskService.renameById(id, request.getName())
            ? ResponseEntity.ok(null)
            : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") int id) {
        return taskService.removeById(id)
            ? ResponseEntity.ok(null)
            : ResponseEntity.notFound().build();
    }
}
