package com.example.task.repository;

import com.example.task.domain.task.Task;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query(value = """
            SELECT * FROM tasks t
            JOIN users_tasks ut ON ut.task_id = t.id
            WHERE ut.user_id = :userId
            """, nativeQuery = true)
    List<Task> findAllByUserId(@Param("userId") Long userId);

    @Query(value = """
            SELECT * FROM tasks t
            JOIN users_tasks ut ON ut.task_id = t.id
            WHERE ut.user_id = :userId
            AND ((t.local_date_time IS NULL)
            OR (t.local_date_time BETWEEN :start AND :end))
            """, nativeQuery = true)
    List<Task> findAllTasksForWeek(@Param("userId") Long userId, @Param("start") Timestamp start, @Param("end") Timestamp end);

    @Query(value = """
            SELECT * FROM tasks t
            WHERE t.local_date_time is not null
            AND t.local_date_time between :start and :end
            """, nativeQuery = true)
    List<Task> findAllSoonTasks(@Param("start") Timestamp start, @Param("end") Timestamp end);

    @Modifying
    @Query(value = """
            DELETE FROM users_tasks ut 
            WHERE ut.task_id = :taskId
            """, nativeQuery = true)
    void deleteByTaskId(@Param("taskId") Long taskId);

    @Modifying
    @Query(value = """
            INSERT INTO users_tasks (user_id, task_id)
            VALUES (:userId, :taskId)
            """, nativeQuery = true)
    void assignTask(@Param("userId") Long userId, @Param("taskId") Long taskId);

}
