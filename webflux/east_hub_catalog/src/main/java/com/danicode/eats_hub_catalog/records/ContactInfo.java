package com.danicode.eats_hub_catalog.records;

import lombok.Builder;

@Builder
public record ContactInfo(String phone, String email, String website) {
}
