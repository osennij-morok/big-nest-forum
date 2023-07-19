package net.gastipatis.bignestforum.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "forum")
public class ForumEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private Long categoryId;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

//    public ForumEntity(String id, Long categoryId, String name) {
//        assertIdIsValid(id);
//        assertCategoryIdIsValid(categoryId);
//        assertNameIsValid(name);
//        this.id = id;
//        this.categoryId = categoryId;
//        this.name = name;
//    }
//
//    private void assertIdIsValid(String id) {
//        if (id.trim().isBlank()) {
//            throw EntityValidationException.blankField("id");
//        }
//    }
//
//    private void assertCategoryIdIsValid(Long categoryId) {
//        if (categoryId == null) {
//            throw EntityValidationException.nullField("categoryId");
//        }
//    }
//
//    private void assertNameIsValid(String name) {
//        if (name.trim().isBlank()) {
//            throw EntityValidationException.blankField("name");
//        }
//    }
}
