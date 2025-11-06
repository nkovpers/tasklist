package org.example.tasklist.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.tasklist.dto.TaskRecord;
import org.example.tasklist.dto.UserRecord;
import org.example.tasklist.exception.TaskNotFoundException;
import org.example.tasklist.exception.UserNotFoundException;
import org.example.tasklist.service.TaskService;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/api/tasklist")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Operation(summary = "Die Liste der Benutzer erhalten")
    @GetMapping("/user/")
    public List<UserRecord> getUserList() {
        return taskService.findAllUsers();
    }

    @Operation(summary = "Einen neuen Benutzer erstellen")
    @PostMapping("/user/")
    public UserRecord addUser(@RequestBody @Valid UserRecord user) {
        return taskService.createUser(user);
    }

    @Operation(summary = "Den Benutzer entfernen")
    @DeleteMapping("/user/{userId}")
    public void deleteUser(@PathVariable("userId") long userId) {
        taskService.deleteUser(userId);
    }

    @Operation(summary = "Die Aufgabenliste eines Benutzers erhalten")
    @GetMapping("/task/{userId}")
    public List<TaskRecord> getTaskList(@PathVariable("userId") long userId) throws UserNotFoundException {
        return taskService.getTaskList(userId);
    }

    @Operation(summary = "Eine neue Aufgabe für einen Benutzer erstellen")
    @PostMapping("/task/{userId}")
    public TaskRecord addTask(@PathVariable("userId") long userId, @RequestBody @Valid TaskRecord task) {
        return taskService.createTask(userId, task);
    }

    @Operation(summary = "Die Aufgabe bearbeiten")
    @PostMapping("/task")
    public TaskRecord updateTask(@RequestBody @Valid TaskRecord task) throws TaskNotFoundException {
        return taskService.updateTask(task);
    }

    @Operation(summary = "Die Aufgabe entfernen")
    @DeleteMapping("/task/{taskId}")
    public void deleteTask(@PathVariable("taskId") long taskId) {
        taskService.deleteTask(taskId);
    }

}
