package com.mayo.server.payment.domain.models;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pay")
@Entity
public class Pay {

    @Id
    @Column(name = "order_id", nullable = false, length = 256)
    private String orderId;

    @Column(name = "order_name", nullable = false, length = 256)
    private String orderName;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(name = "customer_name", nullable = false, length = 50)
    private String customerName;

    @Column(name = "customer_key", nullable = false, length = 256)
    private String customerKey;

    @Column(name = "pay_status", nullable = false, length = 50)
    private String payStatus;

    @Column(name = "pay_amount", nullable = false)
    private Long payAmount;

    @Column(name = "pay_key", nullable = false, length = 256)
    private String payKey;

    @Column(name = "pay_method", nullable = false, length = 128)
    private String payMethod;

    @Column(name = "pay_method_id", nullable = false, length = 256)
    private String payMethodId;

    @Column(name = "pay_mid", nullable = false, length = 256)
    private String payMid;

    @Column(name = "pay_version", nullable = false, length = 20)
    private String payVersion;

    @Column(name = "pay_requested_at", nullable = false)
    private String payRequestedAt;

    @Column(name = "pay_approved_at", nullable = false)
    private String payApprovedAt;

}