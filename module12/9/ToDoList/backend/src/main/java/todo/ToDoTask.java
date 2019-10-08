package todo;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public class ToDoTask {
    private int id;

    private String name;

    @JsonFormat(pattern="dd.MM.yyyy HH:mm:ss")
    private final LocalDateTime createdAt;

    @JsonFormat(pattern="dd.MM.yyyy HH:mm:ss")
    private LocalDateTime completedAt;

    public ToDoTask() {
        this(LocalDateTime.now());
    }

    public ToDoTask(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ToDoTask(ToDoTask other) {
        this.id = other.id;
        this.name = other.name;
        this.createdAt = other.createdAt;
        this.completedAt = other.completedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public boolean isCompleted() {
        return completedAt != null;
    }

    public void complete(boolean value) {
        this.completedAt = value? LocalDateTime.now(): null;
    }
}
