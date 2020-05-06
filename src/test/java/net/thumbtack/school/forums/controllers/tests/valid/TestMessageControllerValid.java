package net.thumbtack.school.forums.controllers.tests.valid;

import net.thumbtack.school.forums.controllers.tests.TestBaseMvc;
import net.thumbtack.school.forums.dto.request.messages.*;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TestMessageControllerValid extends TestBaseMvc {

    @Test
    public void testBadPublishMessage() throws Exception {
        mockMvc.perform(put("/api/messages/5/publish")
                .cookie(addTestCookie())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(new PublishMessageRequest(null)))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());

        Mockito.verify(messageService, times(0)).publishMessage(anyInt(), anyString(), any());
    }

    @Test
    public void testBadAddComment() throws Exception {
        mockMvc.perform(post("/api/messages/5")
                .cookie(addTestCookie())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(new AddCommentRequest("")))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/api/messages/5")
                .cookie(addTestCookie())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(new AddCommentRequest(null)))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());

        Mockito.verify(messageService, times(0)).createComment(anyInt(), anyString(), any());
    }

    @Test
    public void testBadAddRatingMessageMvc() throws Exception {

        mockMvc.perform(post("/api/messages/5/rating")
                .cookie(addTestCookie())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(new AddRatingOnMessageRequest(0)))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/api/messages/5/rating")
                .cookie(addTestCookie())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(new AddRatingOnMessageRequest(6)))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());

        Mockito.verify(messageService, times(0)).addRating(anyInt(), anyString(), any());
    }

    @Test
    public void testBadChangeMessageMvc() throws Exception {
        mockMvc.perform(put("/api/messages/5")
                .cookie(addTestCookie())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(new ChangeMessageRequest(null)))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());

        mockMvc.perform(put("/api/messages/5")
                .cookie(addTestCookie())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(new ChangeMessageRequest("")))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());

        Mockito.verify(messageService, times(0)).changeMessage(anyInt(), anyString(), any());
    }

    @Test
    public void testBadChangePriorityMvc() throws Exception {
        mockMvc.perform(post("/api/messages/5/priority")
                .cookie(addTestCookie())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(new ChangePriorityRequest(null)))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());

        Mockito.verify(messageService, times(0)).changePriority(anyInt(), anyString(), any());
    }

    @Test
    public void testBadConvertMessageMvc() throws Exception {
        mockMvc.perform(put("/api/messages/5/up")
                .cookie(addTestCookie())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(new CommentIntoMessageRequest("")))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());

        mockMvc.perform(put("/api/messages/5/up")
                .cookie(addTestCookie())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(new CommentIntoMessageRequest(null)))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());

        Mockito.verify(messageService, times(0)).convertCommentToMessage(anyInt(), anyString(), any());
    }

}
