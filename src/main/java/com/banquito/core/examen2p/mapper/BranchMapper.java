package com.banquito.core.examen2p.mapper;

import com.banquito.core.examen2p.dto.BranchDTO;
import com.banquito.core.examen2p.dto.BranchHolidayDTO;
import com.banquito.core.examen2p.model.Branch;
import com.banquito.core.examen2p.model.BranchHoliday;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BranchMapper {
    BranchDTO toDto(Branch branch);
    Branch toEntity(BranchDTO branchDTO);
    BranchHolidayDTO toDto(BranchHoliday branchHoliday);
    BranchHoliday toEntity(BranchHolidayDTO branchHolidayDTO);
} 