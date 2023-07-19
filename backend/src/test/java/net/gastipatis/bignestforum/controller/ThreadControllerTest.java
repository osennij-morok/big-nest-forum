package net.gastipatis.bignestforum.controller;

import net.gastipatis.bignestforum.application.ApplicationExceptionHandler;
import net.gastipatis.bignestforum.application.service.ForumService;
import net.gastipatis.bignestforum.configuration.ApplicationSecurity;
import net.gastipatis.bignestforum.controller.configuration.TestSecurityConfigurer;
import net.gastipatis.bignestforum.domain.PostSender;
import net.gastipatis.bignestforum.dto.thread.ForumThreadDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Tag("integration")
@WebMvcTest(ThreadController.class)
@Import({ApplicationExceptionHandler.class})
@ContextConfiguration(classes = {ThreadController.class, TestSecurityConfigurer.class, ApplicationSecurity.class})
class ThreadControllerTest {

    @Autowired
    private MockMvc mockMvc;
//    @Autowired
//    private TestRestTemplate testRestTemplate;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private ForumService forumService;

    private static final String TEST_CORRECT_FORUM_ID = "dev";

    @BeforeEach
    void init() {
//        when(forumService.listPosts(eq(TEST_CORRECT_FORUM_ID), any()))
//                .thenAnswer(invocation -> List.of(
//                        ThreadPostDTO.builder()
//                                .id(1)
//                                .threadHeadPostId(invocation.getArgument(1))
//                                .text("First topic")
//                                .topic("First text")
//                                .build(),
//                        ThreadPostDTO.builder()
//                                .id(2)
//                                .threadHeadPostId(invocation.getArgument(1))
//                                .text("Second topic")
//                                .topic("Second text")
//                                .build()
//                ));
//        when(forumService.listPosts(
//                argThat(forumId -> !forumId.equals(TEST_CORRECT_FORUM_ID)),
//                anyLong()))
//                .thenThrow()
    }

    @Test
    void given_twoThreadsInDB_when_listThreads_expect_statusOkWithThreadsJsonList() throws Exception {
        String forumId = "dev";
        when(forumService.listThreads(eq(forumId))).thenReturn(List.of(
                ForumThreadDTO.builder()
                        .id(1)
                        .forumId(forumId)
//                        .senderId(-1L)
                        .sender(null)
                        .topic("First topic")
                        .text("First text")
                        .build(),
                ForumThreadDTO.builder()
                        .id(2)
                        .forumId(forumId)
//                        .senderId(-1L)
                        .sender(null)
                        .topic("Second topic")
                        .text("Second text")
                        .build()
        ));

        mockMvc.perform(get("/thread")
                .param("forumId", forumId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].forumId", is(forumId)))
                .andExpect(jsonPath("$[0].topic", is("First topic")))
                .andExpect(jsonPath("$[0].text", is("First text")))

                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].forumId", is(forumId)))
                .andExpect(jsonPath("$[1].topic", is("Second topic")))
                .andExpect(jsonPath("$[1].text", is("Second text")))
        ;
    }

//    @WithAnonymousUser
//    @Test
//    void g1() {
////        testRestTemplate.
//    }
//
//    @Test
//    void given_nullThreadIdWithSomePostId_when_listPosts_expect_thatPost() {
//
//    }
//
//    @Test
//    void given_nullPostIdWithSomeThreadId_when_listPosts_expect_postsFromTheThread() {
//
//    }
}