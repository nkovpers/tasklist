package org.example.tasklist.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;

public record UserRecord(Long id,
                         @NotEmpty @Schema(description = "Der Vorname kann null oder leer nicht sein") String firstName,
                         @NotEmpty @Schema(description = "Der Nachname kann null oder leer nicht sein") String lastName) {

}
