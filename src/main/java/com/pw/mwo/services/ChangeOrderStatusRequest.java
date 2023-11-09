package com.pw.mwo.services;

import com.pw.mwo.domain.OrderStatus;

public record ChangeOrderStatusRequest(
        Long id,
        OrderStatus orderStatus
) {
}
