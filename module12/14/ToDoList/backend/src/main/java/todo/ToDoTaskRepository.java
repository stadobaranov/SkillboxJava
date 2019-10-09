package todo;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ToDoTaskRepository extends CrudRepository<ToDoTask, Integer> {
    @Query("FROM ToDoTask ORDER BY createdAt DESC")
    List<ToDoTask> findAllOrdered();

    @Modifying
    @Query("UPDATE ToDoTask SET completedAt = :completedAt WHERE id = :id")
    int completeById(@Param("id") int id, @Param("completedAt") LocalDateTime completedAt);

    @Modifying
    @Query("UPDATE ToDoTask SET name = :name WHERE id = :id")
    int renameById(@Param("id") int id, @Param("name") String name);

    @Modifying
    @Query("DELETE ToDoTask WHERE id = :id")
    int removeById(@Param("id") int id); // Метод deleteById не возвращает кол-во затронутых строк.
}
