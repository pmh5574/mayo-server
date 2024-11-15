package com.mayo.server.customer.adapter.out.persistence;

import com.mayo.server.common.enums.UserType;
import java.time.LocalDateTime;

public record CustomerVerifyIdByPhone(
        String customerUsername,
        UserType role,
        LocalDateTime createdAt
) {
}