package com.banquito.core.examen2p.controller;

import com.banquito.core.examen2p.controller.dto.BranchDTO;
import com.banquito.core.examen2p.controller.dto.BranchHolidayDTO;
import com.banquito.core.examen2p.controller.mapper.BranchMapper;
import com.banquito.core.examen2p.exception.BranchNotFoundException;
import com.banquito.core.examen2p.exception.ErrorResponse;
import com.banquito.core.examen2p.exception.InvalidBranchDataException;
import com.banquito.core.examen2p.exception.HolidayOperationException;
import com.banquito.core.examen2p.service.BranchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/v1/branches")
@RequiredArgsConstructor
@Tag(name = "Branch", description = "API para gestionar sucursales bancarias y sus feriados")
public class BranchController {

    private final BranchService branchService;
    private final BranchMapper branchMapper;

    @GetMapping
    @Operation(summary = "Obtener todas las sucursales", 
              description = "Retorna una lista de todas las sucursales bancarias activas e inactivas")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de sucursales obtenida exitosamente",
                content = @Content(schema = @Schema(implementation = BranchDTO.class)))

    })
    public ResponseEntity<List<BranchDTO>> getAllBranches() {
        try {
            log.info("Obteniendo todas las sucursales");
            List<BranchDTO> branches = branchService.findAll().stream()
                    .map(branchMapper::toDto)
                    .collect(Collectors.toList());
            log.info("Se encontraron {} sucursales", branches.size());
            return ResponseEntity.ok(branches);
        } catch (Exception e) {
            log.error("Error al obtener las sucursales", e);
            throw e;
        }
    }

    @PostMapping
    @Operation(summary = "Crear una nueva sucursal", 
              description = "Crea una nueva sucursal bancaria sin feriados. Todos los campos son obligatorios excepto el ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Sucursal creada exitosamente",
                content = @Content(schema = @Schema(implementation = BranchDTO.class)))
    })
    public ResponseEntity<BranchDTO> createBranch(
            @Parameter(description = "Datos de la sucursal a crear", required = true)
            @Valid @RequestBody BranchDTO branchDTO) {
        try {
            log.info("Creando nueva sucursal con nombre: {}", branchDTO.getName());
            BranchDTO createdBranch = branchMapper.toDto(
                branchService.create(
                    branchMapper.toEntity(branchDTO)
                )
            );
            log.info("Sucursal creada con ID: {}", createdBranch.getId());
            return ResponseEntity.ok(createdBranch);
        } catch (InvalidBranchDataException e) {
            log.error("Error al crear la sucursal: datos inválidos", e);
            throw e;
        } catch (Exception e) {
            log.error("Error al crear la sucursal", e);
            throw e;
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener sucursal por ID", 
              description = "Retorna una sucursal bancaria específica basada en su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Sucursal encontrada exitosamente",
                content = @Content(schema = @Schema(implementation = BranchDTO.class)))
    })
    public ResponseEntity<BranchDTO> getBranchById(
            @Parameter(description = "ID de la sucursal", required = true)
            @PathVariable String id) {
        try {
            log.info("Buscando sucursal con ID: {}", id);
            BranchDTO branch = branchMapper.toDto(branchService.findById(id));
            log.info("Sucursal encontrada: {}", branch.getName());
            return ResponseEntity.ok(branch);
        } catch (BranchNotFoundException e) {
            log.error("Sucursal no encontrada con ID: {}", id);
            throw e;
        } catch (Exception e) {
            log.error("Error al buscar la sucursal con ID: {}", id, e);
            throw e;
        }
    }

    @PatchMapping("/{id}/phone")
    @Operation(summary = "Actualizar número de teléfono", 
              description = "Actualiza el número de teléfono de una sucursal específica")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Número de teléfono actualizado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Formato de teléfono inválido"),
        @ApiResponse(responseCode = "404", description = "Sucursal no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<BranchDTO> updatePhoneNumber(
            @Parameter(description = "ID de la sucursal", required = true)
            @PathVariable String id,
            @Parameter(description = "Nuevo número de teléfono", required = true)
            @Pattern(regexp = "^\\+?[0-9]{10,13}$", message = "El formato del número de teléfono no es válido")
            @RequestParam String phoneNumber) {
        try {
            log.info("Actualizando número de teléfono de la sucursal {}: {}", id, phoneNumber);
            BranchDTO updatedBranch = branchMapper.toDto(
                branchService.updatePhoneNumber(id, phoneNumber)
            );
            log.info("Número de teléfono actualizado para la sucursal: {}", id);
            return ResponseEntity.ok(updatedBranch);
        } catch (BranchNotFoundException | InvalidBranchDataException e) {
            log.error("Error al actualizar el teléfono de la sucursal {}: {}", id, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error inesperado al actualizar el teléfono de la sucursal {}", id, e);
            throw e;
        }
    }

    @PostMapping("/{id}/holidays")
    @Operation(summary = "Agregar feriado", 
              description = "Agrega un nuevo feriado a una sucursal específica")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Feriado agregado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos del feriado inválidos"),
        @ApiResponse(responseCode = "404", description = "Sucursal no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<BranchDTO> addHoliday(
            @Parameter(description = "ID de la sucursal", required = true)
            @PathVariable String id,
            @Parameter(description = "Datos del feriado", required = true)
            @Valid @RequestBody BranchHolidayDTO holidayDTO) {
        try {
            log.info("Agregando feriado {} a la sucursal {}", holidayDTO.getName(), id);
            BranchDTO updatedBranch = branchMapper.toDto(
                branchService.addHoliday(id, branchMapper.toEntity(holidayDTO))
            );
            log.info("Feriado agregado a la sucursal: {}", id);
            return ResponseEntity.ok(updatedBranch);
        } catch (BranchNotFoundException | HolidayOperationException e) {
            log.error("Error al agregar feriado a la sucursal {}: {}", id, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error inesperado al agregar feriado a la sucursal {}", id, e);
            throw e;
        }
    }

    @DeleteMapping("/{id}/holidays")
    @Operation(summary = "Eliminar feriados", 
              description = "Elimina todos los feriados de una sucursal específica")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Feriados eliminados exitosamente"),
        @ApiResponse(responseCode = "404", description = "Sucursal no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<BranchDTO> clearHolidays(
            @Parameter(description = "ID de la sucursal", required = true)
            @PathVariable String id) {
        try {
            log.info("Eliminando todos los feriados de la sucursal: {}", id);
            BranchDTO updatedBranch = branchMapper.toDto(branchService.clearHolidays(id));
            log.info("Feriados eliminados de la sucursal: {}", id);
            return ResponseEntity.ok(updatedBranch);
        } catch (BranchNotFoundException e) {
            log.error("Error al eliminar feriados: sucursal {} no encontrada", id);
            throw e;
        } catch (Exception e) {
            log.error("Error inesperado al eliminar feriados de la sucursal {}", id, e);
            throw e;
        }
    }

    @GetMapping("/{id}/holidays")
    @Operation(summary = "Obtener feriados", 
              description = "Obtiene todos los feriados de una sucursal específica")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de feriados obtenida exitosamente"),
        @ApiResponse(responseCode = "404", description = "Sucursal no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<BranchHolidayDTO>> getHolidays(
            @Parameter(description = "ID de la sucursal", required = true)
            @PathVariable String id) {
        try {
            log.info("Obteniendo feriados de la sucursal: {}", id);
            List<BranchHolidayDTO> holidays = branchService.getHolidays(id).stream()
                    .map(branchMapper::toDto)
                    .collect(Collectors.toList());
            log.info("Se encontraron {} feriados para la sucursal {}", holidays.size(), id);
            return ResponseEntity.ok(holidays);
        } catch (BranchNotFoundException e) {
            log.error("Error al obtener feriados: sucursal {} no encontrada", id);
            throw e;
        } catch (Exception e) {
            log.error("Error inesperado al obtener feriados de la sucursal {}", id, e);
            throw e;
        }
    }

    @GetMapping("/{id}/holidays/check")
    @Operation(summary = "Verificar feriado", 
              description = "Verifica si una fecha específica es feriado en una sucursal")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Verificación realizada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Sucursal no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Boolean> isHoliday(
            @Parameter(description = "ID de la sucursal", required = true)
            @PathVariable String id,
            @Parameter(description = "Fecha a verificar", required = true)
            @RequestParam LocalDateTime date) {
        try {
            log.info("Verificando si {} es feriado en la sucursal {}", date, id);
            boolean isHoliday = branchService.isHoliday(id, date);
            log.info("Resultado de la verificación para la sucursal {}: {}", id, isHoliday);
            return ResponseEntity.ok(isHoliday);
        } catch (BranchNotFoundException e) {
            log.error("Error al verificar feriado: sucursal {} no encontrada", id);
            throw e;
        } catch (Exception e) {
            log.error("Error inesperado al verificar feriado en la sucursal {}", id, e);
            throw e;
        }
    }

    @ExceptionHandler(BranchNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleBranchNotFoundException(BranchNotFoundException e) {
        ErrorResponse error = new ErrorResponse("NOT_FOUND", e.getMessage());
        return ResponseEntity.status(404).body(error);
    }

    @ExceptionHandler(InvalidBranchDataException.class)
    public ResponseEntity<ErrorResponse> handleInvalidBranchDataException(InvalidBranchDataException e) {
        ErrorResponse error = new ErrorResponse("BAD_REQUEST", e.getMessage());
        return ResponseEntity.status(400).body(error);
    }

    @ExceptionHandler(HolidayOperationException.class)
    public ResponseEntity<ErrorResponse> handleHolidayOperationException(HolidayOperationException e) {
        ErrorResponse error = new ErrorResponse("BAD_REQUEST", e.getMessage());
        return ResponseEntity.status(400).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception e) {
        ErrorResponse error = new ErrorResponse("INTERNAL_SERVER_ERROR", "Error interno del servidor");
        log.error("Error no manejado", e);
        return ResponseEntity.status(500).body(error);
    }
} 