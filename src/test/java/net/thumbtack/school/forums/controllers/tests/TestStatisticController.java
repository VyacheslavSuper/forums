package net.thumbtack.school.forums.controllers.tests;

import net.thumbtack.school.forums.dto.response.statistics.ListCountMessagesResponse;
import net.thumbtack.school.forums.dto.response.statistics.ListRatingMessagesResponse;
import net.thumbtack.school.forums.dto.response.statistics.ListRatingUserResponse;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TestStatisticController extends TestBaseMvc {
    @Test
    public void testGetForumsByCount() throws Exception {
        when(statisticsService.getCountMessages(anyString(), any(), anyInt(), anyInt())).thenReturn(new ListCountMessagesResponse(new ArrayList<>()));

        MvcResult result = mockMvc.perform(get("/api/statistics/forums/count")
                .cookie(addTestCookie())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();
        Assert.assertNotNull(result.getResponse().getContentAsString());

        Mockito.verify(statisticsService).getCountMessages(anyString(), any(), anyInt(), anyInt());
    }

    @Test
    public void testGetMessagesByRating() throws Exception {

        when(statisticsService.getListMessagesByRating(anyString(), any(), anyBoolean(), anyInt(), anyInt())).thenReturn(new ListRatingMessagesResponse(new ArrayList<>()));

        MvcResult result = mockMvc.perform(get("/api/statistics/messages/rating")
                .cookie(addTestCookie())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();
        Assert.assertNotNull(result.getResponse().getContentAsString());

        Mockito.verify(statisticsService).getListMessagesByRating(anyString(), any(), anyBoolean(), anyInt(), anyInt());
    }

    @Test
    public void testGetUsersByRating() throws Exception {

        when(statisticsService.getListUsersByRating(anyString(), any(), anyInt(), anyInt())).thenReturn(new ListRatingUserResponse(new ArrayList<>()));

        MvcResult result = mockMvc.perform(get("/api/statistics/users/rating")
                .cookie(addTestCookie())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();
        Assert.assertNotNull(result.getResponse().getContentAsString());

        Mockito.verify(statisticsService).getListUsersByRating(anyString(), any(), anyInt(), anyInt());
    }


}
