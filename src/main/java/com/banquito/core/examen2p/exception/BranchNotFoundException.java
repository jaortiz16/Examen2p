package com.banquito.core.examen2p.exception;

public class BranchNotFoundException extends RuntimeException {
    private final String branchId;

    public BranchNotFoundException(String branchId) {
        super();
        this.branchId = branchId;
    }

    @Override
    public String getMessage() {
        return "No se encontró ninguna sucursal con el ID: " + branchId;
    }
} 