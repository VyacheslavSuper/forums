package net.thumbtack.school.forums.controllers.tests.valid;

import net.thumbtack.school.forums.controllers.tests.TestBaseMvc;
import net.thumbtack.school.forums.dto.request.users.LoginUserRequest;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TestSessionControllerValid extends TestBaseMvc {
    @Test
    public void testBadLogin() throws Exception {

        mockMvc.perform(post("/api/sessions")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(new LoginUserRequest("", "GoodPassword")))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/api/sessions")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(new LoginUserRequest(null, "GoodPassword")))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/api/sessions")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(new LoginUserRequest("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", "GoodPassword")))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/api/sessions")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(new LoginUserRequest("goodName", "")))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/api/sessions")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(new LoginUserRequest("goodName", null)))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/api/sessions")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(new LoginUserRequest("goodName", "BadPasswo")))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
        Mockito.verify(sessionService, Mockito.times(0)).login(anyString(), any());
    }
}
