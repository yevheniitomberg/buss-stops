package tech.tomberg.bussstophelper.service;

import tech.tomberg.bussstophelper.entity.BussStop;
import tech.tomberg.bussstophelper.entity.StopTime;

import java.util.Collection;
import java.util.Map;


public interface MainServiceInterface {
    Collection<BussStop> getStopsByAreaId(long id);
    Collection<String> getBussesNamesByBussStopId(long id);
    Collection<String> getBussesNamesByMap(Map<String, Collection<BussStop>> map);
    Collection<Collection<StopTime>> getStopTimesByBussStopId(long id);
    Collection<Collection<StopTime>> getStopTimesBetweenTwoStops(Map<String, Collection<BussStop>> map, long id);

}
