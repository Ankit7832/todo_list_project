package com.ankit.todo_list.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "task", uniqueConstraints = @UniqueConstraint(columnNames = {
        "title","is_completed"
}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    private String description;

    @Column(name = "is_completed")
    private Boolean isCompleted;
    @Column(name = "created_at")
    private Instant createdAt;
    private Instant updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @PrePersist
    protected void onCreate(){
        this.createdAt=this.updatedAt=Instant.now().truncatedTo(ChronoUnit.SECONDS);
        this.isCompleted=false;
    }
    @PreUpdate
    protected void onUpdate(){
        this.updatedAt=Instant.now().truncatedTo(ChronoUnit.SECONDS);
    }
}
