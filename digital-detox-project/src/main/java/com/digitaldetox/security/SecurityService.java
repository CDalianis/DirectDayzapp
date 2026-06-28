package com.digitaldetox.security;

import com.digitaldetox.model.DetoxPlan;
import com.digitaldetox.model.User;
import com.digitaldetox.repository.CoachProfileRepository;
import com.digitaldetox.repository.DailyCheckInRepository;
import com.digitaldetox.repository.DetoxPlanRepository;
import com.digitaldetox.repository.MemberProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SecurityService {

    private final DetoxPlanRepository detoxPlanRepository;
    private final MemberProfileRepository memberProfileRepository;
    private final CoachProfileRepository coachProfileRepository;
    private final DailyCheckInRepository dailyCheckInRepository;

    public boolean isOwnMemberProfile(UUID memberUuid, Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof User user)) {
            return false;
        }
        return memberProfileRepository.findByUuidAndDeletedFalse(memberUuid)
                .map(profile -> profile.getUser().getId().equals(user.getId()))
                .orElse(false);
    }

    public boolean isAssignedCoachForPlan(UUID planUuid, Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof User user)) {
            return false;
        }
        return detoxPlanRepository.findByUuidAndDeletedFalse(planUuid)
                .map(plan -> plan.getCoachProfile().getUser().getId().equals(user.getId()))
                .orElse(false);
    }

    public boolean isOwnPlanMember(UUID planUuid, Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof User user)) {
            return false;
        }
        return detoxPlanRepository.findByUuidAndDeletedFalse(planUuid)
                .map(plan -> plan.getMemberProfile().getUser().getId().equals(user.getId()))
                .orElse(false);
    }

    public boolean isPlanParticipant(UUID planUuid, Authentication authentication) {
        return isOwnPlanMember(planUuid, authentication) || isAssignedCoachForPlan(planUuid, authentication);
    }

    public boolean isOwnCheckInMember(UUID checkInUuid, Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof User user)) {
            return false;
        }
        return dailyCheckInRepository.findByUuidAndDeletedFalse(checkInUuid)
                .map(checkIn -> checkIn.getDetoxPlan().getMemberProfile().getUser().getId().equals(user.getId()))
                .orElse(false);
    }

    public boolean isApprovedCoach(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof User user)) {
            return false;
        }
        return coachProfileRepository.findByUserUsernameAndDeletedFalse(user.getUsername())
                .map(profile -> profile.isApproved())
                .orElse(false);
    }
}
