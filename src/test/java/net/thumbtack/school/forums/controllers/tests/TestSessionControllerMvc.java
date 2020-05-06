package net.thumbtack.school.forums.controllers.tests;

import net.thumbtack.school.forums.dto.request.users.LoginUserRequest;
import net.thumbtack.school.forums.dto.response.ResponseBase;
import net.thumbtack.school.forums.dto.response.users.UserResponse;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class TestSessionControllerMvc extends TestBaseMvc {

    @Test
    public void testLogin() throws Exception {
        UserResponse userResponse = new UserResponse(2, "MyLogin123", "Mylogin123@gmail.com");
        when(sessionService.login(anyString(), any())).thenReturn(userResponse);
        when(cookieService.createCookie()).thenReturn(addTestCookie());
        LoginUserRequest loginUserRequest = new LoginUserRequest("MyLogin123", "MypasswordMylogin");

        MvcResult result = mockMvc.perform(post("/api/sessions")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(loginUserRequest))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(cookie().value("JAVASESSIONID", "my_cookie"))
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.login").value("MyLogin123"))
                .andExpect(jsonPath("$.email").value("Mylogin123@gmail.com"))
                .andReturn();

        Mockito.verify(sessionService).login(anyString(), any());
    }

    @Test
    public void testLogout() throws Exception {
        when(sessionService.logout(anyString())).thenReturn(new ResponseBase());

        when(cookieService.deleteCookie()).thenReturn(delTestCookie());
        mockMvc.perform(delete("/api/sessions")
                .cookie(addTestCookie())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(cookie().value("JAVASESSIONID", (String) null));

        Mockito.verify(sessionService).logout(anyString());
    }
}
