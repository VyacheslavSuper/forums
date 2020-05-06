package net.thumbtack.school.forums.controllers.tests;

import net.thumbtack.school.forums.dto.request.forums.CreateForumRequest;
import net.thumbtack.school.forums.dto.request.messages.CreateMessageRequest;
import net.thumbtack.school.forums.dto.response.ResponseBase;
import net.thumbtack.school.forums.dto.response.forum.ForumResponse;
import net.thumbtack.school.forums.dto.response.forum.FullInfoForumResponse;
import net.thumbtack.school.forums.dto.response.message.FullInfoMessageResponse;
import net.thumbtack.school.forums.dto.response.message.ListFullInfoMessageResponse;
import net.thumbtack.school.forums.dto.response.message.MessageResponse;
import net.thumbtack.school.forums.model.types.ForumType;
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

public class TestForumControllerMvc extends TestBaseMvc {

    @Test
    public void testСreateForumMvc() throws Exception {
        ForumResponse forumResponse = new ForumResponse(2, "MyForum", ForumType.MODERATED);
        when(forumService.createForum(anyString(), any())).thenReturn(forumResponse);

        CreateForumRequest createForumRequest = new CreateForumRequest("MyForum", ForumType.MODERATED);

        mockMvc.perform(post("/api/forums")
                .cookie(addTestCookie())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(createForumRequest))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.topic").value("MyForum"))
                .andExpect(jsonPath("$.type").value("MODERATED"));

        Mockito.verify(forumService).createForum(anyString(), any());
    }

    @Test
    public void testDeleteForumMvc() throws Exception {
        when(forumService.deleteForum(anyString(), anyInt())).thenReturn(new ResponseBase());

        mockMvc.perform(delete("/api/forums/2")
                .cookie(addTestCookie())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());

        Mockito.verify(forumService).deleteForum(anyString(), anyInt());
    }

    @Test
    public void testGetForumMvc() throws Exception {
        FullInfoForumResponse fullInfoForumResponse = new FullInfoForumResponse(5, "subject", ForumType.MODERATED, "admin", false, 1, 0);
        when(forumService.getForum(anyInt(), anyString())).thenReturn(fullInfoForumResponse);

        mockMvc.perform(get("/api/forums/5")
                .cookie(addTestCookie())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.topic").value("subject"))
                .andExpect(jsonPath("$.creator").value("admin"))
                .andExpect(jsonPath("$.countMessages").value(1))
                .andExpect(jsonPath("$.countComments").value(0));

        Mockito.verify(forumService).getForum(anyInt(), anyString());
    }

    @Test
    public void testCreateMessageMvc() throws Exception {
        MessageResponse messageResponse = new MessageResponse(5, MessageState.PUBLISHED);
        when(messageService.createMessage(anyInt(), anyString(), any())).thenReturn(messageResponse);

        CreateMessageRequest request = new CreateMessageRequest("Subject", "Body");

        mockMvc.perform(post("/api/forums/2/messages")
                .cookie(addTestCookie())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.state").value("PUBLISHED"))
                .andExpect(status().isOk());

        Mockito.verify(messageService).createMessage(anyInt(), anyString(), any());
    }

    @Test
    public void testGetMessagesOnForumMvc() throws Exception {
        List<String> body = new ArrayList<>();
        body.add("Body");
        FullInfoMessageResponse fullInfoMessageResponse = new FullInfoMessageResponse(5, "admin", "subject", body, MessagePriority.NORMAL, new ArrayList<>(), LocalDateTime.now(), 5.0, 1, new ArrayList<>());
        List<FullInfoMessageResponse> list = new ArrayList<>();
        list.add(fullInfoMessageResponse);

        ListFullInfoMessageResponse listFullInfoMessageResponse = new ListFullInfoMessageResponse(list);
        when(forumService.getForumMessages(anyInt(), anyString(), anyBoolean(), anyBoolean(), anyBoolean(), any(), anyInt(), anyInt())).thenReturn(listFullInfoMessageResponse);
        MvcResult result = mockMvc.perform(get("/api/forums/5/messages")
                .cookie(addTestCookie())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();
        Assert.assertNotNull(result.getResponse().getContentAsString());

        Mockito.verify(forumService).getForumMessages(anyInt(), anyString(), anyBoolean(), anyBoolean(), anyBoolean(), any(), anyInt(), anyInt());
    }

}
