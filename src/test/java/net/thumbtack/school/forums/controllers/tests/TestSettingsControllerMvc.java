package net.thumbtack.school.forums.controllers.tests;

import net.thumbtack.school.forums.dto.response.settings.SettingsResponse;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TestSettingsControllerMvc extends TestBaseMvc {

    @Test
    public void testGetSettings() throws Exception {
        SettingsResponse settingsResponse = new SettingsResponse("10", "5");
        when(settingsService.getSettings("my_cookie")).thenReturn(settingsResponse);

        MvcResult result = mockMvc.perform(get("/api/settings")
                .cookie(addTestCookie())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.maxNameLength").value("10"))
                .andExpect(jsonPath("$.minPasswordLength").value("5"))
                .andReturn();
        Assert.assertNotNull(result.getResponse().getContentAsString());

        Mockito.verify(settingsService).getSettings("my_cookie");
    }

}
