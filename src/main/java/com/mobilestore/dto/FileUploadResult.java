package com.mobilestore.dto;

/**
 * DTO chứa kết quả upload file
 */
public class FileUploadResult {
    private final boolean success;
    private final String filePath;
    private final String errorMessage;

    public FileUploadResult(boolean success, String filePath, String errorMessage) {
        this.success = success;
        this.filePath = filePath;
        this.errorMessage = errorMessage;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
