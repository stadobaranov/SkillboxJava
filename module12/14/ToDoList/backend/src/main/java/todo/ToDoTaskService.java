package todo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ToDoTaskService {
    private final ToDoTaskRepository repository;

    @Autowired
    public ToDoTaskService(ToDoTaskRepository repository) {
        this.repository = repository;
    }

    public List<ToDoTask> getAll() {
        return repository.findAllOrdered();
    }

    public ToDoTask get(int id) {
        return repository.findById(id).orElse(null);
    }

    @Transactional
    public void create(ToDoTask task) {
        repository.save(task);
    }

    @Transactional
    public boolean completeById(int id, LocalDateTime completedAt) {
        return repository.completeById(id, completedAt) == 1;
    }

    @Transactional
    public boolean renameById(int id, String name) {
        return repository.renameById(id, name) == 1;
    }

    @Transactional
    public boolean removeById(int id) {
        return repository.removeById(id) == 1;
    }
}
