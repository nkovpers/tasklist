package org.example.tasklist.service;

import org.example.tasklist.dto.TaskRecord;
import org.example.tasklist.exception.TaskNotFoundException;
import org.example.tasklist.model.Task;
import org.example.tasklist.repository.TaskRepository;
import org.example.tasklist.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    public static final long TEST_USER_ID = 11L;
    public static final long TEST_TASK_ID = 111L;

    @Mock
    private UserRepository userRepository;
    @Mock
    private TaskRepository taskRepository;
    @InjectMocks
    private TaskService taskService;

    @AfterEach
    void tearDown() {
        Mockito.reset(userRepository, taskRepository);
    }

    @Test
    void givenValidTaskRecord_whenUpdateTask_schouldAllFieldsUpdated() throws TaskNotFoundException {
        TaskRecord taskRecordForUpdate = new TaskRecord(TEST_TASK_ID, "New title", true);

        Task existingTask = new Task("Existing title", TEST_USER_ID);
        existingTask.setId(taskRecordForUpdate.id());
        Mockito.when(taskRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(existingTask));
        Mockito.when(taskRepository.save(Mockito.any(Task.class))).thenReturn(existingTask);

        TaskRecord updatedTaskRecord = taskService.updateTask(taskRecordForUpdate);

        Mockito.verify(taskRepository, Mockito.times(1)).findById(Mockito.eq(taskRecordForUpdate.id()));
        Mockito.verify(taskRepository, Mockito.times(1)).save(Mockito.any(Task.class));

        assertEquals(taskRecordForUpdate, updatedTaskRecord);
    }

    @Test
    void givenUnexistedTaskRecord_whenUpdateTask_schouldThrowTaskNotFoundException() {
        Mockito.when(taskRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        TaskRecord taskRecord = new TaskRecord(TEST_USER_ID, "Test title", true);
        assertThrows(TaskNotFoundException.class, () -> taskService.updateTask(taskRecord));
        Mockito.verify(taskRepository, Mockito.times(1)).findById(Mockito.eq(taskRecord.id()));
    }

}