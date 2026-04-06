package com.inditex.infrastructure.in.exception;

import lombok.Builder;

@Builder
public record ExceptionDto(String code, String message) {
}
