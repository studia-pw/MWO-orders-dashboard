package com.pw.mwo.domain;

import jakarta.persistence.Embeddable;

@Embeddable
public enum OrderStatus {
    NEW,
    IN_PROGRESS,
    COMPLETED
}
