package tech.tomberg.bussstophelper.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tech.tomberg.bussstophelper.entity.BussStop;
import tech.tomberg.bussstophelper.entity.StopTime;
import tech.tomberg.bussstophelper.entity.Trip;

import java.util.Collection;

public interface StopTimeRepository extends JpaRepository<StopTime, Long> {
    @Query("select s from StopTime s where s.bussStop.stopId=:id")
    Collection<StopTime> findAllByBussStop(@Param("id") long id);
    Collection<StopTime> findAllByBussStop(BussStop bussStop);
    Collection<StopTime> findAllByTrip(Trip trip);
}