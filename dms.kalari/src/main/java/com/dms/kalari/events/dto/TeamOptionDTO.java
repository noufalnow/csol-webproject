package com.dms.kalari.events.dto;

public class TeamOptionDTO {

    private String code;
    private int currentSize;
    private int maxSize;

    public TeamOptionDTO(String code, int currentSize, int maxSize) {
        this.code = code;
        this.currentSize = currentSize;
        this.maxSize = maxSize;
    }

    public String getCode() {
        return code;
    }

    public int getCurrentSize() {
        return currentSize;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public String getLabel() {
        return code + " (" + currentSize + "/" + maxSize + ")";
    }

    public boolean isFull() {
        return currentSize >= maxSize;
    }
}