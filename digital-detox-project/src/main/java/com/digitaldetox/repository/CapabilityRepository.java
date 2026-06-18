package com.digitaldetox.repository;

import com.digitaldetox.model.Capability;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CapabilityRepository extends JpaRepository<Capability, Long> {

    Optional<Capability> findByName(String name);
}
