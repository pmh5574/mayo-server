package com.mayo.server.common;

import com.mayo.server.common.utility.DateUtility;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public record PaginationWithTimeDto (


        Integer page,

        Integer pageSize,

        String startAt,

        String endAt
) {

    public Pageable getPageRequest() {

        return PageRequest.of(this.page - 1, this.pageSize);
    }

    public Pageable getPageRequest(String sort) {

        return PageRequest.of(this.page, this.pageSize,  Sort.by(Sort.Direction.DESC, sort));
    }

    public String getUTCPlainStart() {

        String plainTime = DateUtility.replaceDateForT(this.startAt);
        return DateUtility.getUTCTransDateTime(plainTime, Constants.yyyy_MM_DD_HH_mm_ss);
    }

    public String getUTCPlainEnd() {
        String plainTime = DateUtility.replaceDateForT(this.endAt);
        return DateUtility.getUTCTransDateTime(plainTime, Constants.yyyy_MM_DD_HH_mm_ss);
    }

    public String getUTCLocaleStart() {

        String plainTime = DateUtility.replaceDateForT(this.startAt);
        return DateUtility.getUTCTransDateTime(plainTime, Constants.yyyy_MM_DD_HH_mm_ss);
    }

    public String getUTCLocaleEnd() {
        String plainTime = DateUtility.replaceDateForT(this.endAt);
        return DateUtility.getUTCTransDateTime(plainTime, Constants.yyyy_MM_DD_HH_mm_ss);
    }
}
