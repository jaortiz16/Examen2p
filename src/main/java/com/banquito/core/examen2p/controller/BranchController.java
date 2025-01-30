package com.banquito.core.examen2p.controller;

import com.banquito.core.examen2p.dto.BranchDTO;
import com.banquito.core.examen2p.dto.BranchHolidayDTO;
import com.banquito.core.examen2p.mapper.BranchMapper;
import com.banquito.core.examen2p.service.BranchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/branches")
@RequiredArgsConstructor
@Tag(name = "Branch", description = "API para gestionar sucursales bancarias y sus feriados")
public class BranchController {

    private final BranchService branchService;
    private final BranchMapper branchMapper;

    @GetMapping
    @Operation(summary = "Obtener todas las sucursales", description = "Retorna una lista de todas las sucursales bancarias")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de sucursales obtenida exitosamente",
                content = @Content(schema = @Schema(implementation = BranchDTO.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<BranchDTO>> getAllBranches() {
        return ResponseEntity.ok(
            branchService.findAll().stream()
                .map(branchMapper::toDto)
                .collect(Collectors.toList())
        );
    }

    @PostMapping
    @Operation(summary = "Crear una nueva sucursal", description = "Crea una nueva sucursal bancaria sin feriados")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Sucursal creada exitosamente",
                content = @Content(schema = @Schema(implementation = BranchDTO.class))),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<BranchDTO> createBranch(@RequestBody BranchDTO branchDTO) {
        return ResponseEntity.ok(
            branchMapper.toDto(
                branchService.create(
                    branchMapper.toEntity(branchDTO)
                )
            )
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener sucursal por ID", description = "Retorna una sucursal bancaria por su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Sucursal encontrada exitosamente",
                content = @Content(schema = @Schema(implementation = BranchDTO.class))),
        @ApiResponse(responseCode = "404", description = "Sucursal no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<BranchDTO> getBranchById(@PathVariable String id) {
        return ResponseEntity.ok(
            branchMapper.toDto(branchService.findById(id))
        );
    }

    @PatchMapping("/{id}/phone")
    public ResponseEntity<BranchDTO> updatePhoneNumber(
            @PathVariable String id,
            @RequestParam String phoneNumber) {
        return ResponseEntity.ok(
            branchMapper.toDto(
                branchService.updatePhoneNumber(id, phoneNumber)
            )
        );
    }

    @PostMapping("/{id}/holidays")
    public ResponseEntity<BranchDTO> addHoliday(
            @PathVariable String id,
            @RequestBody BranchHolidayDTO holidayDTO) {
        return ResponseEntity.ok(
            branchMapper.toDto(
                branchService.addHoliday(id, branchMapper.toEntity(holidayDTO))
            )
        );
    }

    @DeleteMapping("/{id}/holidays")
    public ResponseEntity<BranchDTO> clearHolidays(@PathVariable String id) {
        return ResponseEntity.ok(
            branchMapper.toDto(branchService.clearHolidays(id))
        );
    }

    @GetMapping("/{id}/holidays")
    public ResponseEntity<List<BranchHolidayDTO>> getHolidays(@PathVariable String id) {
        return ResponseEntity.ok(
            branchService.getHolidays(id).stream()
                .map(branchMapper::toDto)
                .collect(Collectors.toList())
        );
    }

    @GetMapping("/{id}/holidays/check")
    public ResponseEntity<Boolean> isHoliday(
            @PathVariable String id,
            @RequestParam LocalDateTime date) {
        return ResponseEntity.ok(branchService.isHoliday(id, date));
    }
} 