package com.mayo.server.chef.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@Table(name = "chef_hash_tag")
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ChefHashTag {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long chefId;

    private String chefHashTag;

    public static List<String> getHashTags(
            List<ChefHashTag> tags
    ) {

        return tags.stream().map(v -> v.chefHashTag).toList();
    }
}
