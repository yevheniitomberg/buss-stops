package tech.tomberg.bussstophelper.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.tomberg.bussstophelper.entity.Trip;

public interface TripRepository extends JpaRepository<Trip, Long> {
}