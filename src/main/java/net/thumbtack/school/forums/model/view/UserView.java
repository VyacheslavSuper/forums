package net.thumbtack.school.forums.model.view;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.thumbtack.school.forums.model.types.RestrictionType;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class UserView {
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
}
