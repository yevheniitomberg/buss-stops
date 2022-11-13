package tech.tomberg.bussstophelper.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.tomberg.bussstophelper.entity.Service;

public interface ServiceRepository extends JpaRepository<Service, Long> {
}