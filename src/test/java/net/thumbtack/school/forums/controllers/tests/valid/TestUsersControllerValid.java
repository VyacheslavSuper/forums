package net.thumbtack.school.forums.controllers.tests.valid;

import net.thumbtack.school.forums.controllers.tests.TestBaseMvc;
import net.thumbtack.school.forums.dto.request.users.ChangePasswordRequest;
import net.thumbtack.school.forums.dto.request.users.RegisterUserRequest;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TestUsersControllerValid extends TestBaseMvc {

    @Test
    public void testBadResgister() throws Exception {

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(new RegisterUserRequest("", "", "")))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(new RegisterUserRequest("", "Bsdsd@mail.ru", "GoodPassword")))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(new RegisterUserRequest(null, "Bsdsd@mail.ru", "GoodPassword")))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(new RegisterUserRequest("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", "Bsdsd@mail.ru", "GoodPassword")))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(new RegisterUserRequest("goodName", "badEmail", "GoodPassword")))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(new RegisterUserRequest("goodName", null, "GoodPassword")))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(new RegisterUserRequest("goodName", "GoodEmail@mail.ru", "")))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(new RegisterUserRequest("goodName", "GoodEmail@mail.ru", null)))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());

        Mockito.verify(userService, times(0)).register(anyString(), any());
    }

    @Test
    public void testBadPasswordChange() throws Exception {

        mockMvc.perform(put("/api/users")
                .cookie(addTestCookie())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(new ChangePasswordRequest(null, "GoodPassword", "GoodPasswordNew")))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());

        mockMvc.perform(put("/api/users")
                .cookie(addTestCookie())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(new ChangePasswordRequest("", "GoodPassword", "GoodPasswordNew")))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());

        mockMvc.perform(put("/api/users")
                .cookie(addTestCookie())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(new ChangePasswordRequest("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", "GoodPassword", "GoodPasswordNew")))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());

        mockMvc.perform(put("/api/users")
                .cookie(addTestCookie())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(new ChangePasswordRequest("goodName", "", "GoodPasswordNew")))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());

        mockMvc.perform(put("/api/users")
                .cookie(addTestCookie())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(new ChangePasswordRequest("goodName", null, "GoodPasswordNew")))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());

        mockMvc.perform(put("/api/users")
                .cookie(addTestCookie())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(new ChangePasswordRequest("goodName", "GoodPassword", "")))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());

        mockMvc.perform(put("/api/users")
                .cookie(addTestCookie())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(new ChangePasswordRequest("goodName", "GoodPasswordNew", null)))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());

        Mockito.verify(userService, times(0)).changePassword(anyString(), any());
    }
}
