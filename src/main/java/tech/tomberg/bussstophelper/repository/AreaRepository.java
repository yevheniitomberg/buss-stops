package tech.tomberg.bussstophelper.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tech.tomberg.bussstophelper.entity.Area;

public interface AreaRepository extends JpaRepository<Area, Long> {
    @Query("select a from Area a where a.areaName=:areaName")
    Area findAreaByAreaName(@Param("areaName") String areaName);
    boolean existsAreaByAreaName(String areaName);
}