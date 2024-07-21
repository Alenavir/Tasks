package com.example.task.domain.task;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Task {

    private Long id;
    private String title;
    private String description;
    private Status status;
    private LocalDateTime localDateTime;

}
