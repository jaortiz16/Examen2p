package com.banquito.core.examen2p.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class BranchHolidayDTO {
    private LocalDateTime date;
    private String name;
} 