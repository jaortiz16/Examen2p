package com.banquito.core.examen2p.controller.mapper;

import com.banquito.core.examen2p.controller.dto.BranchDTO;
import com.banquito.core.examen2p.controller.dto.BranchHolidayDTO;
import com.banquito.core.examen2p.model.Branch;
import com.banquito.core.examen2p.model.BranchHoliday;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class BranchMapper {

    public BranchDTO toDto(Branch branch) {
        if (branch == null) {
            return null;
        }

        return BranchDTO.builder()
                .id(branch.getId())
                .emailAddress(branch.getEmailAddress())
                .name(branch.getName())
                .phoneNumber(branch.getPhoneNumber())
                .state(branch.getState())
                .creationDate(branch.getCreationDate())
                .lastModifiedDate(branch.getLastModifiedDate())
                .branchHolidays(mapHolidaysToDto(branch.getBranchHolidays()))
                .build();
    }

    public Branch toEntity(BranchDTO dto) {
        if (dto == null) {
            return null;
        }

        Branch branch = new Branch();
        branch.setId(dto.getId());
        branch.setEmailAddress(dto.getEmailAddress());
        branch.setName(dto.getName());
        branch.setPhoneNumber(dto.getPhoneNumber());
        branch.setState(dto.getState());
        branch.setCreationDate(dto.getCreationDate());
        branch.setLastModifiedDate(dto.getLastModifiedDate());
        branch.setBranchHolidays(mapHolidaysToEntity(dto.getBranchHolidays()));
        return branch;
    }

    public BranchHolidayDTO toDto(BranchHoliday holiday) {
        if (holiday == null) {
            return null;
        }

        return BranchHolidayDTO.builder()
                .date(holiday.getDate())
                .name(holiday.getName())
                .build();
    }

    public BranchHoliday toEntity(BranchHolidayDTO dto) {
        if (dto == null) {
            return null;
        }

        BranchHoliday holiday = new BranchHoliday();
        holiday.setDate(dto.getDate());
        holiday.setName(dto.getName());
        return holiday;
    }

    private List<BranchHolidayDTO> mapHolidaysToDto(List<BranchHoliday> holidays) {
        if (holidays == null) {
            return new ArrayList<>();
        }
        return holidays.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private List<BranchHoliday> mapHolidaysToEntity(List<BranchHolidayDTO> dtos) {
        if (dtos == null) {
            return new ArrayList<>();
        }
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
} 