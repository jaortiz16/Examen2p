package com.banquito.core.examen2p.service;

import com.banquito.core.examen2p.model.Branch;
import com.banquito.core.examen2p.model.BranchHoliday;
import com.banquito.core.examen2p.repository.BranchRepository;
import org.springframework.http.HttpStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Branch not found with id: " + id));
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
    public Branch clearHolidays(String id) {
        Branch branch = findById(id);
        branch.setBranchHolidays(new ArrayList<>());
        branch.setLastModifiedDate(LocalDateTime.now());
        return branchRepository.save(branch);
    }

    public List<BranchHoliday> getHolidays(String id) {
        Branch branch = findById(id);
        return branch.getBranchHolidays();
    }

    public boolean isHoliday(String id, LocalDateTime date) {
        Branch branch = findById(id);
        return branch.getBranchHolidays().stream()
                .anyMatch(holiday -> holiday.getDate().toLocalDate().equals(date.toLocalDate()));
    }
} 
