package com.pw.mwo.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum OrderStatus {
    @JsonProperty("new")
    NEW,

    @JsonProperty("inProgress")
    IN_PROGRESS,

    @JsonProperty("completed")
    COMPLETED;
}
