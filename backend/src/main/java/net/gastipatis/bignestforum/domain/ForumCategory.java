package net.gastipatis.bignestforum.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity(name = "forum_category")
public class ForumCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

//    public ForumCategory(Long id, String name) {
//        assertIdIsValid(id);
//        assertNameIsValid(name);
//        this.id = id;
//        this.name = name;
//    }

//    private void assertIdIsValid(Long id) {
//        if (id == null) {
//            throw EntityValidationException.nullField("id");
//        }
//    }
//
//    private void assertNameIsValid(String name) {
//        if (name.trim().isBlank()) {
//            throw EntityValidationException.blankField("name");
//        }
//    }
}
