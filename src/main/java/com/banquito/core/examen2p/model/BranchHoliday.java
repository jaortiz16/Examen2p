package com.banquito.core.examen2p.model;

import lombok.Data;
import java.time.LocalDate;

@Data
public class BranchHoliday {
    private LocalDate date;
    private String name;
} 