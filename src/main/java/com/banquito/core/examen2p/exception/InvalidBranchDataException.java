package com.banquito.core.examen2p.exception;

public class InvalidBranchDataException extends RuntimeException {
    private final String field;
    private final String value;

    public InvalidBranchDataException(String field, String value) {
        super();
        this.field = field;
        this.value = value;
    }

    @Override
    public String getMessage() {
        return "Datos inválidos para el campo " + field + ": " + value;
    }
} 