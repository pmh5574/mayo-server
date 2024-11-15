package com.mayo.server.chef.domain.model;

import com.mayo.server.chef.app.port.in.TransformedImageDto;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@Table(name = "chef_portfolio")
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ChefPortfolio {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chef_id")
    private Long chefId;

    @Column(name = "`order`")
    private Integer order;

    @Column(name = "chef_portfolio_image")
    private String chefPortfolioImage;

    @Column(nullable = true, name = "deleted_at")
    private String deletedAt;

    public static List<TransformedImageDto> getPortFolios(
            List<ChefPortfolio> portfolio
    ) {

        return portfolio.stream().map(v -> {

            return new TransformedImageDto(
                    v.getOrder(),
                    v.getChefPortfolioImage()
            );
        }).toList();
    }

}
