package com.mobilestore.dto;

/**
 * DTO chứa kết quả validation
 */
public class ValidationResult {
    private final boolean valid;
    private final String errorCode;

    public ValidationResult(boolean valid, String errorCode) {
        this.valid = valid;
        this.errorCode = errorCode;
    }

    public boolean isValid() {
        return valid;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
