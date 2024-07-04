package org.example.tasklist.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.tasklist.Application;
import org.example.tasklist.dto.TaskRecord;
import org.example.tasklist.dto.UserRecord;
import org.example.tasklist.model.Task;
import org.example.tasklist.model.User;
import org.example.tasklist.repository.TaskRepository;
import org.example.tasklist.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(classes = Application.class)
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@AutoConfigureMockMvc
class TaskControllerTest {

    public static final long TEST_USER_ID = 111L;
    public static final long TEST_USER2_ID = 11111L;
    public static final long TEST_TASK_ID = 2L;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private TaskRepository taskRepository;

    @AfterEach
    void tearDown() {
        Mockito.reset(userRepository, taskRepository);
    }

    @Test
    void givenValidGetAllUsersRequest_whenGetUsers_shouldReturnOkAndUserList() throws Exception {
        User u1 = buildNewUser(TEST_USER_ID, "Firstname 1", "Lastname 1");
        User u2 = buildNewUser(TEST_USER2_ID,"Firstname 2", "Lastname 2");
        when(userRepository.findAll()).thenReturn(List.of(u1, u2));

        this.mockMvc.perform(get("/api/tasklist/user/"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.*", hasSize(2)))
                .andExpect(jsonPath("$[*].firstName", containsInAnyOrder(u1.getFirstName(), u2.getFirstName())))
                .andExpect(jsonPath("$[*].lastName", containsInAnyOrder(u1.getLastName(), u2.getLastName())));
    }

    private static User buildNewUser(Long userId, String firstName, String lastName) {
        User u1 = new User(userId);
        u1.setFirstName(firstName);
        u1.setLastName(lastName);
        return u1;
    }

    @Test
    void givenValidUserRequest_whenAddUser_shouldReturnOkAndNewUser() throws Exception {
        when(userRepository.save(any(User.class))).thenAnswer(i -> {
            User user = (User) i.getArguments()[0];
            user.setId(TEST_USER_ID);
            return user;
        });

        UserRecord userRecordForSave = new UserRecord(0L,"Firstname", "Lastname");
        this.mockMvc.perform(post("/api/tasklist/user/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(userRecordForSave)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName").value(userRecordForSave.firstName()))
                .andExpect(jsonPath("$.lastName").value(userRecordForSave.lastName()));
    }

    @Test
    void givenValidUserId_whenDeleteUser_shouldReturnOk() throws Exception {
        this.mockMvc.perform(delete("/api/tasklist/user/" + TEST_USER_ID)).andExpect(status().isOk());
        Mockito.verify(userRepository).deleteById(eq(TEST_USER_ID));
    }

    @Test
    void givenValidUserId_whenGetTasks_schouldReturnOkAndTaskList() throws Exception {
        User user = buildNewUser(TEST_USER_ID, "Test firstname", "Test lastname");
        user.getTasks().add(new Task("Test title 1", user.getId()));
        user.getTasks().add(new Task("Test title 2", user.getId()));
        when(userRepository.findById(eq(user.getId()))).thenReturn(Optional.of(user));

        this.mockMvc.perform(get("/api/tasklist/task/" + user.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.*", hasSize(2)))
                .andExpect(jsonPath("$[*].title",
                        containsInAnyOrder(user.getTasks().get(0).getTitle(), user.getTasks().get(1).getTitle())));
    }

    @Test
    void givenValidTaskRequest_whenAddTask_schouldReturnOkAndNewTask() throws Exception {
        TaskRecord taskRecordForSave = new TaskRecord(0L, "Test title", false);

        when(taskRepository.save(any(Task.class))).thenAnswer(i -> {
            Task task = (Task) i.getArguments()[0];
            task.setId(TEST_TASK_ID);
            return task;
        });

        this.mockMvc.perform(post("/api/tasklist/task/" + TEST_USER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(taskRecordForSave)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(TEST_TASK_ID))
                .andExpect(jsonPath("$.title").value(taskRecordForSave.title()))
                .andExpect(jsonPath("$.completed").value(taskRecordForSave.completed()));
    }

    private static Task getConvertedTask(long userId, TaskRecord taskRecord) {
        User user = new User(userId);
        Task t = new Task(taskRecord.title(), user.getId());
        t.setId(taskRecord.id());
        t.setCompleted(taskRecord.completed());
        return t;
    }

    @Test
    void givenValidTaskRequest_whenUpdateTask_schouldReturnOkAndTask() throws Exception {
        Task existingTask = new Task("Existing title", TEST_USER_ID);
        existingTask.setId(TEST_TASK_ID);
        when(taskRepository.findById(eq(existingTask.getId()))).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any())).thenReturn(existingTask);

        TaskRecord taskRecordForUpdate = new TaskRecord(existingTask.getId(), "New title", true);
        this.mockMvc.perform(post("/api/tasklist/task")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(taskRecordForUpdate)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(taskRecordForUpdate.id()))
                .andExpect(jsonPath("$.title").value(taskRecordForUpdate.title()))
                .andExpect(jsonPath("$.completed").value(taskRecordForUpdate.completed()));
    }

    @Test
    void givenValidTaskId_whenDeleteTask_schouldRteurnOk() throws Exception {
        this.mockMvc.perform(delete("/api/tasklist/task/" + TEST_TASK_ID))
                .andExpect(status().isOk());
        Mockito.verify(taskRepository).deleteById(eq(TEST_TASK_ID));
    }

}