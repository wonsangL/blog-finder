package com.example.blogfinder.infra;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "keyword")
@NoArgsConstructor
public class KeywordEntity {
    @Id
    @Getter
    private String title;

    @Column
    @Getter
    @Setter
    private Integer useCount;

    @Version
    private Integer version;

    public KeywordEntity(String keyword) {
        this.title = keyword;
        this.useCount = 0;
    }
}
