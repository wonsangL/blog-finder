package com.example.blogfinder.infra;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "keyword")
public class KeywordEntity {
    @Id
    @Getter
    private String title;

    @Column
    @Getter
    private Integer useCount;

    @Version
    private Integer version;
}
