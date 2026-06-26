package com.digitaldetox.service;

import com.digitaldetox.core.exceptions.EntityAlreadyExistsException;
import com.digitaldetox.core.exceptions.EntityInvalidArgumentException;
import com.digitaldetox.core.exceptions.EntityNotFoundException;
import com.digitaldetox.dto.member.MemberReadOnlyDTO;
import com.digitaldetox.dto.member.MemberRegisterDTO;
import com.digitaldetox.dto.member.MemberUpdateDTO;

import java.util.List;
import java.util.UUID;

public interface IMemberService {

    MemberReadOnlyDTO register(MemberRegisterDTO dto)
            throws EntityAlreadyExistsException, EntityInvalidArgumentException;

    List<MemberReadOnlyDTO> listMembers();

    MemberReadOnlyDTO getByUuid(UUID uuid) throws EntityNotFoundException;

    MemberReadOnlyDTO getCurrentMember(String username) throws EntityNotFoundException;

    MemberReadOnlyDTO updateCurrentMember(String username, MemberUpdateDTO dto) throws EntityNotFoundException;
}
