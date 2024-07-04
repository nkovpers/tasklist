package org.example.tasklist.service;

import lombok.extern.slf4j.Slf4j;
import org.example.tasklist.dto.TaskRecord;
import org.example.tasklist.dto.UserRecord;
import org.example.tasklist.exception.TaskNotFoundException;
import org.example.tasklist.exception.UserNotFoundException;
import org.example.tasklist.model.Task;
import org.example.tasklist.repository.TaskRepository;
import org.example.tasklist.model.User;
import org.example.tasklist.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toCollection;

@Service
@Slf4j
public class TaskService {

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    public TaskService(UserRepository userRepository, TaskRepository taskRepository) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }

    public List<UserRecord> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(TaskService::getConvertedUserRecord)
                .collect(toCollection(ArrayList::new));
    }

    public List<TaskRecord> getTaskList(long userId) throws UserNotFoundException {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            log.error("User is not found: id={}", userId);
            throw new UserNotFoundException("User is not found: id=" + userId);
        }
        return user.getTasks().stream().map(TaskService::getConvertedTaskRecord)
                .collect(toCollection(ArrayList::new));
    }

    public TaskRecord createTask(long userId, TaskRecord taskRecord) {
        Task task = new Task(taskRecord.title(), userId);
        task = taskRepository.save(task);
        return getConvertedTaskRecord(task);
    }

    private static TaskRecord getConvertedTaskRecord(Task task) {
        return task != null ? new TaskRecord(task.getId(), task.getTitle(), task.isCompleted()) : null;
    }

    public UserRecord createUser(UserRecord userRecord) {
        User user = new User(userRecord.firstName(), userRecord.lastName());
        user = userRepository.save(user);
        return getConvertedUserRecord(user);
    }

    private static UserRecord getConvertedUserRecord(User user) {
        return new UserRecord(user.getId(), user.getFirstName(), user.getLastName());
    }

    public TaskRecord updateTask(TaskRecord taskRecord) throws TaskNotFoundException {
        Task task = getTask(taskRecord.id());
        if (task == null) {
            log.error("Task is not found: {}", taskRecord);
            throw new TaskNotFoundException("Task is not found: id=" + taskRecord.id());
        }
        task.setTitle(taskRecord.title());
        task.setCompleted(taskRecord.completed());
        task = taskRepository.save(task);
        return getConvertedTaskRecord(task);
    }

    public Task getTask(long taskId) {
        return taskRepository.findById(taskId).orElse(null);
    }

    public void deleteTask(long taskId) {
        taskRepository.deleteById(taskId);
    }

    public void deleteUser(long userId) {
        userRepository.deleteById(userId);
    }

}
