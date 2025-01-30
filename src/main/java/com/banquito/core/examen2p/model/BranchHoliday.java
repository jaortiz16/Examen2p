package com.banquito.core.examen2p.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class BranchHoliday {
    private LocalDateTime date;
    private String name;
} 