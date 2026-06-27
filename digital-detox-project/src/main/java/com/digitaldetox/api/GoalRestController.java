package com.digitaldetox.api;

import com.digitaldetox.core.exceptions.EntityNotFoundException;
import com.digitaldetox.core.exceptions.ValidationException;
import com.digitaldetox.dto.goal.GoalInsertDTO;
import com.digitaldetox.dto.goal.GoalReadOnlyDTO;
import com.digitaldetox.service.IGoalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/plans/{planUuid}/goals")
@RequiredArgsConstructor
public class GoalRestController {

    private final IGoalService goalService;

    @PostMapping
    public ResponseEntity<GoalReadOnlyDTO> addGoal(@PathVariable UUID planUuid,
                                                   @Valid @RequestBody GoalInsertDTO dto,
                                                   BindingResult bindingResult)
            throws ValidationException, EntityNotFoundException {

        if (bindingResult.hasErrors()) {
            throw new ValidationException("Goal", "Validation failed", bindingResult);
        }

        GoalReadOnlyDTO created = goalService.addGoal(planUuid, dto);
        return ResponseEntity
                .created(URI.create("/api/v1/plans/" + planUuid + "/goals/" + created.uuid()))
                .body(created);
    }

    @GetMapping
    public ResponseEntity<List<GoalReadOnlyDTO>> getGoals(@PathVariable UUID planUuid)
            throws EntityNotFoundException {
        return ResponseEntity.ok(goalService.getGoalsByPlan(planUuid));
    }
}
