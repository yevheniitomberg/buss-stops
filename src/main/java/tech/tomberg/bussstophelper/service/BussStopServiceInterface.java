package tech.tomberg.bussstophelper.service;

import tech.tomberg.bussstophelper.entity.Area;
import tech.tomberg.bussstophelper.entity.BussStop;

import java.util.Collection;
import java.util.Map;

public interface BussStopServiceInterface {
    BussStop saveBussStop(BussStop bussStop);
    Collection<Area> getAllAreas();
    Map<String, Collection<BussStop>> getRouteStopsBySpecificStop(BussStop bussStop);
    Map<String, Collection<BussStop>> getRouteStopsBetweenTwoStops(BussStop userStop, BussStop targetStop);
}
