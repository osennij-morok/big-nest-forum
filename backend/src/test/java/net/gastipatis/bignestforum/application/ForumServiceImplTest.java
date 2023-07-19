package net.gastipatis.bignestforum.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

//@SpringBootTest
@Deprecated
@Tag("unit")
class ForumServiceImplTest {

    public static final String INCORRECT_FORUM_ID = "s";
//    @Autowired
//    private ForumServiceImpl forumService;

//    @MockBean(name = "default")
//    private ForumDAO forumDAO;
//    @MockBean
//    private PostDAO postDAO;

    @BeforeEach
    void init() {
//        forumService = new ForumServiceImpl()
    }

    @Test
    void sample1() {
        try {
//            ThreadPostDTO post = forumService.getPost(INCORRECT_FORUM_ID, 1L);
        } catch (Exception e) {
//            throw e;
        }
    }
}