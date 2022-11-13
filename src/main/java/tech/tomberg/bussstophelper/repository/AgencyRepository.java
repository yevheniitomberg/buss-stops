package tech.tomberg.bussstophelper.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.tomberg.bussstophelper.entity.Agency;

public interface AgencyRepository extends JpaRepository<Agency, Long> {
}