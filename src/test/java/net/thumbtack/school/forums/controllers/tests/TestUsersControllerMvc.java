package net.thumbtack.school.forums.controllers.tests;


import net.thumbtack.school.forums.dto.request.users.ChangePasswordRequest;
import net.thumbtack.school.forums.dto.request.users.RegisterUserRequest;
import net.thumbtack.school.forums.dto.response.ResponseBase;
import net.thumbtack.school.forums.dto.response.users.FullUserResponse;
import net.thumbtack.school.forums.dto.response.users.ListFullUserResponse;
import net.thumbtack.school.forums.dto.response.users.UserResponse;
import net.thumbtack.school.forums.model.types.RestrictionType;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class TestUsersControllerMvc extends TestBaseMvc {
    @Test
    public void testRegisterMvc() throws Exception {
        UserResponse userResponse = new UserResponse(2, "MyLogin123", "Mylogin123@gmail.com");
        when(userService.register(any(), any())).thenReturn(userResponse);

        when(cookieService.createCookie()).thenReturn(addTestCookie());
        RegisterUserRequest registerUserRequest = new RegisterUserRequest("MyLogin123", "Mylogin123@gmail.com", "MypasswordMylogin");

        MvcResult result = mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(registerUserRequest))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(cookie().value("JAVASESSIONID", "my_cookie"))
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.login").value("MyLogin123"))
                .andExpect(jsonPath("$.email").value("Mylogin123@gmail.com"))
                .andReturn();

        Mockito.verify(userService).register(anyString(), any());
    }

    @Test
    public void testDeleteMvc() throws Exception {
        when(userService.deleteUser(anyString())).thenReturn(new ResponseBase());

        when(cookieService.deleteCookie()).thenReturn(delTestCookie());
        mockMvc.perform(delete("/api/users")
                .cookie(addTestCookie())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(cookie().value("JAVASESSIONID", (String) null));

        Mockito.verify(userService).deleteUser("my_cookie");
    }

    @Test
    public void testChangePasswordMvc() throws Exception {
        UserResponse userResponse = new UserResponse(2, "MyLogin123", "Mylogin123@gmail.com");
        when(userService.changePassword(anyString(), any())).thenReturn(userResponse);

        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest("MyLogin123", "MypasswordMylogin", "MypasswordMyloginNew");

        mockMvc.perform(put("/api/users")
                .cookie(addTestCookie())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(changePasswordRequest))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.login").value("MyLogin123"))
                .andExpect(jsonPath("$.email").value("Mylogin123@gmail.com"));

        Mockito.verify(userService).changePassword(anyString(), any());
    }

    @Test
    public void testSetSuperMvc() throws Exception {
        when(userService.setSuperUser("my_cookie", 3)).thenReturn(new ResponseBase());

        mockMvc.perform(put("/api/users/3/super")
                .cookie(addTestCookie())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());

        Mockito.verify(userService).setSuperUser("my_cookie", 3);
    }

    @Test
    public void testGetUsersMvc() throws Exception {
        List<FullUserResponse> list = new ArrayList<>();

        list.add(new FullUserResponse(1, "Admin", "admin", LocalDate.now(), true, false, RestrictionType.FULL, null, 10));
        ListFullUserResponse listFullUserResponse = new ListFullUserResponse(list);
        when(userService.getUsers("my_cookie")).thenReturn(listFullUserResponse);

        MvcResult result = mockMvc.perform(get("/api/users")
                .cookie(addTestCookie())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();
        Assert.assertNotNull(result.getResponse().getContentAsString());

        Mockito.verify(userService).getUsers("my_cookie");
    }

    @Test
    public void testBanUserMvc() throws Exception {
        when(userService.banUser(anyString(), anyInt())).thenReturn(new ResponseBase());

        mockMvc.perform(post("/api/users/3/restrict")
                .cookie(addTestCookie())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());

        Mockito.verify(userService).banUser("my_cookie", 3);
    }


}
