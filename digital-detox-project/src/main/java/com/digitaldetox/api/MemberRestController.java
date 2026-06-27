package com.digitaldetox.api;

import com.digitaldetox.core.exceptions.*;
import com.digitaldetox.dto.member.MemberReadOnlyDTO;
import com.digitaldetox.dto.member.MemberRegisterDTO;
import com.digitaldetox.dto.member.MemberUpdateDTO;
import com.digitaldetox.model.User;
import com.digitaldetox.service.IMemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberRestController {

    private final IMemberService memberService;

    @PostMapping("/register")
    public ResponseEntity<MemberReadOnlyDTO> register(@Valid @RequestBody MemberRegisterDTO dto,
                                                      BindingResult bindingResult)
            throws ValidationException, EntityAlreadyExistsException, EntityInvalidArgumentException {

        if (bindingResult.hasErrors()) {
            throw new ValidationException("Member", "Validation failed", bindingResult);
        }

        MemberReadOnlyDTO created = memberService.register(dto);
        return ResponseEntity
                .created(URI.create("/api/v1/members/" + created.uuid()))
                .body(created);
    }

    @GetMapping
    public ResponseEntity<java.util.List<MemberReadOnlyDTO>> listMembers() {
        return ResponseEntity.ok(memberService.listMembers());
    }

    @GetMapping("/me")
    public ResponseEntity<MemberReadOnlyDTO> getCurrentMember(@AuthenticationPrincipal User user)
            throws EntityNotFoundException {
        return ResponseEntity.ok(memberService.getCurrentMember(user.getUsername()));
    }

    @PutMapping("/me")
    public ResponseEntity<MemberReadOnlyDTO> updateCurrentMember(@AuthenticationPrincipal User user,
                                                                 @Valid @RequestBody MemberUpdateDTO dto,
                                                                 BindingResult bindingResult)
            throws ValidationException, EntityNotFoundException {

        if (bindingResult.hasErrors()) {
            throw new ValidationException("Member", "Validation failed", bindingResult);
        }

        return ResponseEntity.ok(memberService.updateCurrentMember(user.getUsername(), dto));
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<MemberReadOnlyDTO> getByUuid(@PathVariable UUID uuid)
            throws EntityNotFoundException {
        return ResponseEntity.ok(memberService.getByUuid(uuid));
    }
}
