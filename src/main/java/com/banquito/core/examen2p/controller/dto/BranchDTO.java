package com.banquito.core.examen2p.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BranchDTO {
    private String id;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El formato del email no es válido")
    private String emailAddress;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    private String name;

    @NotBlank(message = "El número de teléfono es obligatorio")
    @Pattern(regexp = "^\\+?[0-9]{10,13}$", message = "El formato del número de teléfono no es válido")
    private String phoneNumber;

    @NotBlank(message = "El estado es obligatorio")
    @Pattern(regexp = "^(ACTIVE|INACTIVE)$", message = "El estado debe ser ACTIVE o INACTIVE")
    private String state;

    private LocalDateTime creationDate;
    private LocalDateTime lastModifiedDate;
    private List<BranchHolidayDTO> branchHolidays;
} 