package com.digitaldetox.dto.member;

import com.digitaldetox.dto.user.UserInsertDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MemberRegisterDTO(
        @NotNull @Valid UserInsertDTO user,
        @NotBlank String displayName,
        String timezone,
        String mainGoal
) {
}
