package com.aegis.autochess.dto;

public record ErrorResponseDto(int status, String error, String message, String timestamp) {
}
