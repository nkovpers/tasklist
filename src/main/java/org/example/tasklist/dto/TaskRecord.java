package org.example.tasklist.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;

public record TaskRecord(Long id,
                         @NotEmpty @Schema(description = "Der Name kann null oder leer nicht sein") String title,
                         boolean completed) {

}
