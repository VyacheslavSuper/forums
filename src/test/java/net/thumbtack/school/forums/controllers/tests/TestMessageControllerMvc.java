package net.thumbtack.school.forums.controllers.tests;

import net.thumbtack.school.forums.dto.request.messages.*;
import net.thumbtack.school.forums.dto.response.ResponseBase;
import net.thumbtack.school.forums.dto.response.message.FullInfoMessageResponse;
import net.thumbtack.school.forums.dto.response.message.MessageResponse;
import net.thumbtack.school.forums.model.types.Decision;
import net.thumbtack.school.forums.model.types.MessagePriority;
import net.thumbtack.school.forums.model.types.MessageState;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TestMessageControllerMvc extends TestBaseMvc {

    @Test
    public void testCreateCommentMvc() throws Exception {
        MessageResponse messageResponse = new MessageResponse(6, MessageState.PUBLISHED);
        when(messageService.createComment(anyInt(), anyString(), any())).thenReturn(messageResponse);

        AddCommentRequest request = new AddCommentRequest("Body");

        mockMvc.perform(post("/api/messages/5")
                .cookie(addTestCookie())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(6))
                .andExpect(jsonPath("$.state").value("PUBLISHED"));

        Mockito.verify(messageService).createComment(anyInt(), anyString(), any());
    }

    @Test
    public void testChangePriorityMvc() throws Exception {
        when(messageService.changePriority(anyInt(), anyString(), any())).thenReturn(new ResponseBase());

        ChangePriorityRequest request = new ChangePriorityRequest(MessagePriority.HIGH);

        mockMvc.perform(post("/api/messages/5/priority")
                .cookie(addTestCookie())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());

        Mockito.verify(messageService).changePriority(anyInt(), anyString(), any());
    }

    @Test
    public void testDeleteMessageMvc() throws Exception {
        when(messageService.deleteMessageOrComment(anyInt(), anyString())).thenReturn(new ResponseBase());

        mockMvc.perform(delete("/api/messages/5")
                .cookie(addTestCookie())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());

        Mockito.verify(messageService).deleteMessageOrComment(anyInt(), anyString());
    }

    @Test
    public void testChangeMessageMvc() throws Exception {
        MessageResponse messageResponse = new MessageResponse(5, MessageState.PUBLISHED);
        when(messageService.changeMessage(anyInt(), anyString(), any())).thenReturn(messageResponse);

        ChangeMessageRequest request = new ChangeMessageRequest("Body");

        mockMvc.perform(put("/api/messages/5")
                .cookie(addTestCookie())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.state").value("PUBLISHED"));

        Mockito.verify(messageService).changeMessage(anyInt(), anyString(), any());
    }

    @Test
    public void testConvertMessageMvc() throws Exception {
        MessageResponse messageResponse = new MessageResponse(5, MessageState.PUBLISHED);
        when(messageService.convertCommentToMessage(anyInt(), anyString(), any())).thenReturn(messageResponse);

        CommentIntoMessageRequest request = new CommentIntoMessageRequest("Subject", null, null);

        mockMvc.perform(put("/api/messages/5/up")
                .cookie(addTestCookie())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.state").value("PUBLISHED"))
                .andExpect(status().isOk());

        Mockito.verify(messageService).convertCommentToMessage(anyInt(), anyString(), any());
    }


    @Test
    public void testPublishMessageMvc() throws Exception {
        when(messageService.publishMessage(anyInt(), anyString(), any())).thenReturn(new ResponseBase());

        PublishMessageRequest request = new PublishMessageRequest(Decision.YES);

        mockMvc.perform(put("/api/messages/5/publish")
                .cookie(addTestCookie())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());

        Mockito.verify(messageService).publishMessage(anyInt(), anyString(), any());
    }

    @Test
    public void testAddRatingMessageMvc() throws Exception {
        when(messageService.addRating(anyInt(), anyString(), any())).thenReturn(new ResponseBase());

        AddRatingOnMessageRequest request = new AddRatingOnMessageRequest(4);

        mockMvc.perform(post("/api/messages/5/rating")
                .cookie(addTestCookie())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());

        Mockito.verify(messageService).addRating(anyInt(), anyString(), any());
    }

    @Test
    public void testGetMessageMvc() throws Exception {
        List<String> body = new ArrayList<>();
        body.add("Body");
        FullInfoMessageResponse fullInfoMessageResponse = new FullInfoMessageResponse(5, "admin", "subject", body, MessagePriority.NORMAL, new ArrayList<>(), LocalDateTime.now(), 5.0, 1, new ArrayList<>());
        when(messageService.getMessage(anyInt(), anyString(), anyBoolean(), anyBoolean(), anyBoolean(), any())).thenReturn(fullInfoMessageResponse);
        MvcResult result = mockMvc.perform(get("/api/messages/5")
                .cookie(addTestCookie())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.creator").value("admin"))
                .andExpect(jsonPath("$.topic").value("subject"))
                .andReturn();
        Assert.assertNotNull(result.getResponse().getContentAsString());

        Mockito.verify(messageService).getMessage(anyInt(), anyString(), anyBoolean(), anyBoolean(), anyBoolean(), any());
    }
}
