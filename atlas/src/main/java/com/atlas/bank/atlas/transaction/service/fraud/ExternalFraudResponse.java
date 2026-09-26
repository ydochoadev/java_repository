package com.atlas.bank.atlas.transaction.service.fraud;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExternalFraudResponse {
    private String riskLevel; // LOW, MEDIUM, HIGH
    private double score; // 0.0, 1.0
    private String recommendation; // ALLOW, REVIEW, BLOCK
}
