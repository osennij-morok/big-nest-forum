package net.gastipatis.bignestforum.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity(name = "post_sender")
@Table(name = "account")
public class PostSender {

    @Id
    private Long id;

    @Column(name = "username")
    private String username;

}
