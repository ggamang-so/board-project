package com.ggamangso.boardproject.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@ToString
@Table(indexes = { //검색에 속도를 올리기 위해 indexing 하는 것
        @Index(columnList ="content"),
        @Index(columnList ="createAt"),
        @Index(columnList ="createBy")
})
@EntityListeners(AuditingEntityListener.class)
@Entity
public class ArticleComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter @ManyToOne(optional = false) private Article article; // 게시글(ID)
    @Setter @Column(nullable = false, length = 500) private String content; // 본문


    @CreatedDate @Column(nullable = false) private LocalDateTime createAt; //생성일지
    @CreatedBy @Column(nullable = false, length=100) private String createBy; // 생성자
    @CreatedDate @Column(nullable = false) private LocalDateTime modifiedAt; // 수정일시
    @CreatedBy @Column(nullable = false, length=100) private String modifiedBy; // 수정자

    protected ArticleComment() {
    }

    private ArticleComment(Article article, String content) {
        this.article = article;
        this.content = content;
    }

    public static ArticleComment of(Article article, String content){
        return new ArticleComment(article, content);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArticleComment that = (ArticleComment) o;
        return id!=null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

