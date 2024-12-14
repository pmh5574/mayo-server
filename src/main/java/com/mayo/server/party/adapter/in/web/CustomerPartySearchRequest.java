package com.mayo.server.party.adapter.in.web;

import java.util.List;
import java.util.Objects;

public record CustomerPartySearchRequest(

        List<String> categories,
        List<String> services,
        List<String> areas
) {
        public CustomerPartySearchRequest(final List<String> categories, final List<String> services,
                                          final List<String> areas) {
                this.categories = Objects.requireNonNullElse(categories, List.of());
                this.services = Objects.requireNonNullElse(services, List.of());
                this.areas = Objects.requireNonNullElse(areas, List.of());
        }
}
