package com.banquito.core.examen2p.exception;

public class HolidayOperationException extends RuntimeException {
    private final String operation;
    private final String branchId;
    private final String detail;

    public HolidayOperationException(String operation, String branchId, String detail) {
        super();
        this.operation = operation;
        this.branchId = branchId;
        this.detail = detail;
    }

    @Override
    public String getMessage() {
        return "Error en operación " + operation + " de feriados para la sucursal " + branchId + ": " + detail;
    }
} 