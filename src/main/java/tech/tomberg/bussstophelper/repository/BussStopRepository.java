package tech.tomberg.bussstophelper.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tech.tomberg.bussstophelper.entity.BussStop;
import tech.tomberg.bussstophelper.entity.Coordinate;

import java.util.Collection;

@Repository
public interface BussStopRepository extends JpaRepository<BussStop, Long> {
    @Query("select b from BussStop b where b.stopArea.id=:id order by b.stopName")
    Collection<BussStop> findAllByStopArea(@Param("id") long id);
    BussStop getBussStopByCoordinate(Coordinate coordinate);
}