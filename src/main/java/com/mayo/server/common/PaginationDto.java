package com.mayo.server.common;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

public record PaginationDto (

        @NotNull(message = "page is not null")
        @NotBlank(message = "page is not blank")
        Integer page,

        @NotNull(message = "pageSize is not null")
        @NotBlank(message = "pageSize is not blank")
        Integer pageSize,

        @NotNull(message = "sort is not null")
        @NotBlank(message = "sort is not blank")
        String sort
) {

        public Pageable getPageRequest() {

                return PageRequest.of(this.page - 1, this.pageSize, Sort.by(Sort.Direction.DESC, this.sort));
        }
}
