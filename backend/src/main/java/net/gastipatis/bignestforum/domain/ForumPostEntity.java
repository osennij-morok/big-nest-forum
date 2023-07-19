package net.gastipatis.bignestforum.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.gastipatis.bignestforum.domain.account.Account;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ForumPostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "parent_id", nullable = true)
    private Long parentId;

//    @Column(name = "sender_id", nullable = true)
//    private Long senderId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sender_id")
    private PostSender sender;

    @Column(name = "forum_id")
    private String forumId;

    @Column(name = "topic", nullable = false)
    private String topic;

    @Column(name = "text", nullable = false)
    private String text;

    @Column(name = "publish_time", nullable = false)
    private LocalDateTime publishTime;

//    public ForumPostEntity(Long id, Long parentId, Long senderId, String forumId,
//                           String topic, String text, LocalDateTime publishTime) {
//
//        this.id = id;
//        this.parentId = parentId;
//        this.senderId = senderId;
//        this.forumId = forumId;
//        this.topic = topic;
//        this.text = text;
//        this.publishTime = publishTime;
//    }

}
