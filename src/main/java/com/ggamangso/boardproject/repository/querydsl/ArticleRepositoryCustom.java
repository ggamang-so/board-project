package com.ggamangso.boardproject.repository.querydsl;

import java.util.List;

public interface ArticleRepositoryCustom {
    List<String> findAllDistinctHashtags();
}
