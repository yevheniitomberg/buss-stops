package tech.tomberg.bussstophelper.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.tomberg.bussstophelper.entity.Route;

public interface RouteRepository extends JpaRepository<Route, String> {
}