package org.example.tasklist.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "task")
@Getter
@Setter
@NoArgsConstructor
public class Task {

    @Id
    @GeneratedValue
    private Long id;

    private String title;
    private boolean completed = false;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    public Task(String title, Long userId) {
        this.title = title;
        this.user = new User(userId);
    }

}
