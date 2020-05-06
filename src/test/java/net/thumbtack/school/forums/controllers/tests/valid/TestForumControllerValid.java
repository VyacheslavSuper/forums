package net.thumbtack.school.forums.controllers.tests.valid;

import net.thumbtack.school.forums.controllers.tests.TestBaseMvc;
import net.thumbtack.school.forums.dto.request.forums.CreateForumRequest;
import net.thumbtack.school.forums.dto.request.messages.CreateMessageRequest;
import net.thumbtack.school.forums.model.types.ForumType;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TestForumControllerValid extends TestBaseMvc {
    @Test
    public void testBadCreateForum() throws Exception {
        mockMvc.perform(post("/api/forums")
                .cookie(addTestCookie())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(new CreateForumRequest("", ForumType.UNMODERATED)))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/api/forums")
                .cookie(addTestCookie())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(new CreateForumRequest(null, ForumType.UNMODERATED)))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/api/forums")
                .cookie(addTestCookie())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(new CreateForumRequest("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", ForumType.UNMODERATED)))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
        Mockito.verify(forumService, Mockito.times(0)).createForum(anyString(), any());
    }

    @Test
    public void testBadCreateMessageMvc() throws Exception {
        mockMvc.perform(post("/api/forums/2/messages")
                .cookie(addTestCookie())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(new CreateMessageRequest("", "Body")))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/api/forums/2/messages")
                .cookie(addTestCookie())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(new CreateMessageRequest(null, "Body")))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/api/forums/2/messages")
                .cookie(addTestCookie())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(new CreateMessageRequest("Subject", "")))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/api/forums/2/messages")
                .cookie(addTestCookie())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(new CreateMessageRequest("Subject", null)))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());

        Mockito.verify(messageService, times(0)).createMessage(anyInt(), anyString(), any());
    }
}
