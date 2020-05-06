package net.thumbtack.school.forums.dto.response.settings;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.thumbtack.school.forums.ApplicationProperties;
import net.thumbtack.school.forums.dto.response.ResponseBase;

@Data
@AllArgsConstructor
public class SettingsResponse extends ResponseBase {
    private String banTime;
    private String maxBanCount;
    private String maxNameLength;
    private String minPasswordLength;

    public SettingsResponse(String maxNameLength, String minPasswordLength) {
        this.maxNameLength = maxNameLength;
        this.minPasswordLength = minPasswordLength;
    }

    //Пока не придумал как выбор в MapStruct сделать
    public static SettingsResponse toDto(ApplicationProperties properties, Boolean superuser) {
        if (superuser) {
            return new SettingsResponse(properties.getBanTimeString(), properties.getBanCountString(), properties.getMaxNameLengthString(), properties.getMinPasswordLengthString());
        } else {
            return new SettingsResponse(properties.getMaxNameLengthString(), properties.getMinPasswordLengthString());
        }
    }
}
