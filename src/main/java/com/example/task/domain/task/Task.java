package com.example.task.domain.task;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;

    @Enumerated(value = EnumType.STRING)
    private Status status;

    private LocalDateTime localDateTime;

    @Column(name = "image")
    @CollectionTable(name = "tasks_images")
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> images;

}
