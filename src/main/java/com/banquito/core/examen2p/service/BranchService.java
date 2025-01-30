package com.banquito.core.examen2p.service;

import com.banquito.core.examen2p.model.Branch;
import com.banquito.core.examen2p.model.BranchHoliday;
import com.banquito.core.examen2p.repository.BranchRepository;
import com.banquito.core.examen2p.exception.BranchNotFoundException;
import com.banquito.core.examen2p.exception.HolidayOperationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BranchService {
    private final BranchRepository branchRepository;

    public List<Branch> findAll() {
        return branchRepository.findAll();
    }

    public Branch findById(String id) {
        return branchRepository.findById(id)
                .orElseThrow(() -> new BranchNotFoundException(id));
    }

    @Transactional
    public Branch create(Branch branch) {
        branch.setCreationDate(LocalDateTime.now());
        branch.setLastModifiedDate(LocalDateTime.now());
        branch.setBranchHolidays(new ArrayList<>());
        return branchRepository.save(branch);
    }

    @Transactional
    public Branch updatePhoneNumber(String id, String phoneNumber) {
        Branch branch = findById(id);
        branch.setPhoneNumber(phoneNumber);
        branch.setLastModifiedDate(LocalDateTime.now());
        return branchRepository.save(branch);
    }

    @Transactional
    public Branch addHoliday(String id, BranchHoliday holiday) {
        Branch branch = findById(id);
        if (branch.getBranchHolidays() == null) {
            branch.setBranchHolidays(new ArrayList<>());
        }
        branch.getBranchHolidays().add(holiday);
        branch.setLastModifiedDate(LocalDateTime.now());
        return branchRepository.save(branch);
    }

    @Transactional
    public Branch deleteHoliday(String id, LocalDate date) {
        Branch branch = findById(id);
        if (branch.getBranchHolidays() == null || branch.getBranchHolidays().isEmpty()) {
            throw new HolidayOperationException("eliminar", id, "La sucursal no tiene feriados");
        }

        boolean removed = branch.getBranchHolidays().removeIf(holiday -> holiday.getDate().equals(date));
        if (!removed) {
            throw new HolidayOperationException("eliminar", id, "No se encontró un feriado para la fecha: " + date);
        }

        branch.setLastModifiedDate(LocalDateTime.now());
        return branchRepository.save(branch);
    }

    public List<BranchHoliday> getHolidays(String id) {
        Branch branch = findById(id);
        return branch.getBranchHolidays();
    }

    public boolean isHoliday(String id, LocalDate date) {
        Branch branch = findById(id);
        return branch.getBranchHolidays().stream()
                .anyMatch(holiday -> holiday.getDate().equals(date));
    }

    public void verifyHoliday(String id, LocalDate date) {
        Branch branch = findById(id);
        boolean isHoliday = branch.getBranchHolidays().stream()
                .anyMatch(holiday -> holiday.getDate().equals(date));
        if (!isHoliday) {
            throw new HolidayOperationException("verificar", id, "No existe un feriado para la fecha: " + date);
        }
    }
} 
