package tech.tomberg.bussstophelper.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.tomberg.bussstophelper.entity.Coordinate;

public interface CoordinateRepository extends JpaRepository<Coordinate, Long> {
    Coordinate findAllByLatAndLon(double lat, double lon);
}