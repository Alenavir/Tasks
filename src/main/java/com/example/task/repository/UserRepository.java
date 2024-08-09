package com.example.task.repository;

import com.example.task.domain.task.Task;
import com.example.task.domain.user.Role;
import com.example.task.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(
            String username
    );

    @Query(value = """
            SELECT u.id as id,
            u.name as name,
            u.username as username,
            u.password as password
            FROM users_tasks ut
            JOIN users u ON ut.user_id = u.id
            WHERE ut.task_id = :taskId
            """, nativeQuery = true)
    Optional<User> findTaskAuthor(@Param("taskId") Long taskId);

    @Query(value = """
             SELECT exists(
                           SELECT 1
                           FROM users_tasks
                           WHERE user_id = :userId
                             AND task_id = :taskId)
            """, nativeQuery = true)
    boolean isTaskOwner(@Param("userId") Long userId, @Param("taskId") Long taskId);

}
