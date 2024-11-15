package com.mayo.server.party.domain.model;

import static jakarta.persistence.FetchType.LAZY;

import com.mayo.server.chef.domain.model.Chef;
import com.mayo.server.common.BaseTimeEntity;
import com.mayo.server.common.utility.DateUtility;
import com.mayo.server.customer.domain.model.Customer;
import com.mayo.server.customer.domain.model.Kitchen;
import com.mayo.server.party.domain.enums.HomePartyStatus;
import com.mayo.server.payment.domain.models.PayHomeParty;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class CustomerHomeParty extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerHomePartyId;

    @Enumerated(EnumType.STRING)
    private HomePartyStatus partyStatus;

    private String partyInfo;

    private BigDecimal budget;

    private LocalDateTime partySchedule;

    private Integer adultCount;

    private Integer childCount;

    private String partyComment;

    private LocalDateTime deletedAt;

    @CreatedDate
    private LocalDateTime createdAt;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "chef_id")
    private Chef chef;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "kitchen_id")
    private Kitchen kitchen;

    @OneToMany(mappedBy = "customerHomeParty", cascade = CascadeType.ALL)
    private List<CustomerHomePartyServices> partyServices = new ArrayList<>();

    @OneToMany(mappedBy = "customerHomeParty", cascade = CascadeType.ALL)
    private List<PartySchedule>  partySchedules = new ArrayList<>();

    @OneToMany(mappedBy = "customerHomeParty", cascade = CascadeType.ALL)
    private List<PayHomeParty>  payHomeParties = new ArrayList<>();


    @Builder
    public CustomerHomeParty(final HomePartyStatus partyStatus, final String partyInfo, final BigDecimal budget, final LocalDateTime partySchedule,
                             final Integer adultCount,
                             final Integer childCount,
                             final String partyComment, final Customer customer) {
        this.partyStatus = partyStatus;
        this.partyInfo = partyInfo;
        this.budget = budget;
        this.partySchedule = partySchedule;
        this.adultCount = adultCount;
        this.childCount = childCount;
        this.partyComment = partyComment;
        this.customer = customer;
    }

    public void addHomePartyServices(List<CustomerHomePartyServices> partyServices) {
        this.partyServices = partyServices;
    }

    public Integer getNumberOfPeople(

    ) {

        int sum = 0;
        // 정규식 패턴: 0부터 9까지의 숫자
        Pattern pattern = Pattern.compile("[0-9]");
        Matcher matcher = pattern.matcher(String.valueOf(this.adultCount+this.childCount));

        while (matcher.find()) {
            // 찾은 숫자를 정수로 변환하여 합산
            sum += Integer.parseInt(matcher.group());
        }

        return sum;
    }

    public List<String> getServiceList() {

        List<String> list = new ArrayList<>();

        this.partyServices.forEach(v -> {

            list.add(String.valueOf(v.getServiceName()));
        });

        return list;
    }

    public void addChef(final Chef chef) {
        this.chef = chef;
    }

    public void addKitchen(final Kitchen kitchen) {
        this.kitchen = kitchen;
    }

    public String getPlainSchedule() {

        return DateUtility.replacePlainTimeFromLocaleDate(this.partySchedule);
    }

    public String getPlainCreatedAt() {

        return DateUtility.replacePlainTimeFromLocaleDate(this.createdAt);

    }

    public void changeAssignChef(final PartySchedule partySchedule) {
        addChef(partySchedule.getChef());
        this.partyStatus = HomePartyStatus.CHEF_ACCEPTED;
        partySchedule.customerMatched();
    }

    public Integer getPartySchedulesCount() {
        return this.partySchedules.size();
    }
}
