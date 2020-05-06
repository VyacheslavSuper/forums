package net.thumbtack.school.forums.controllers.tests;

import net.thumbtack.school.forums.dto.request.RequestBase;
import net.thumbtack.school.forums.dto.response.ResponseBase;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TestDebugControllerMvc extends TestBaseMvc {

    @Test
    public void testClear() throws Exception {
        when(debugService.clear(any())).thenReturn(new ResponseBase());

        RequestBase request = new RequestBase();
        mockMvc.perform(post("/api/debug/clear")
                .cookie(addTestCookie())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());

        Mockito.verify(debugService).clear(any());
    }
}
