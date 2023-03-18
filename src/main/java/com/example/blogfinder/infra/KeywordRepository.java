package com.example.blogfinder.infra;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KeywordRepository extends JpaRepository<KeywordEntity, String> {
    @Query("SELECT keyword FROM KeywordEntity as keyword ORDER BY keyword.useCount DESC, keyword.title LIMIT 10")
    List<KeywordEntity> findTop10Keywords();
}
