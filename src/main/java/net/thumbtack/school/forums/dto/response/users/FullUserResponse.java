package net.thumbtack.school.forums.dto.response.users;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thumbtack.school.forums.dto.response.ResponseBase;
import net.thumbtack.school.forums.model.types.RestrictionType;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FullUserResponse extends ResponseBase {
    private int id;
    private String name;
    private String userName;
    private String email;
    private LocalDate timeRegistered;
    private boolean online;
    private boolean deleted;
    private Boolean superuser;
    private RestrictionType status;
    private LocalDate banTimeExit;
    private int banCount;

    public FullUserResponse(int id, String name, String userName, LocalDate timeRegistered, Boolean online, Boolean deleted, RestrictionType status, LocalDate banTimeExit, int banCount) {
        this.id = id;
        this.name = name;
        this.userName = userName;
        this.timeRegistered = timeRegistered;
        this.online = online;
        this.deleted = deleted;
        this.status = status;
        this.banTimeExit = banTimeExit;
        this.banCount = banCount;
    }
}
