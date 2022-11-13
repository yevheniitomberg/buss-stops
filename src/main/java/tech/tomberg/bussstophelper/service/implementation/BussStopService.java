package tech.tomberg.bussstophelper.service.implementation;

import lombok.RequiredArgsConstructor;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tech.tomberg.bussstophelper.entity.Area;
import tech.tomberg.bussstophelper.entity.BussStop;
import tech.tomberg.bussstophelper.entity.StopTime;
import tech.tomberg.bussstophelper.repository.*;
import tech.tomberg.bussstophelper.service.BussStopServiceInterface;

import java.util.*;


@Service
@RequiredArgsConstructor
public class BussStopService implements BussStopServiceInterface {

    private final BussStopRepository bussStopRepository;
    private final AgencyRepository agencyRepository;
    private final RouteRepository routeRepository;
    private final TripRepository tripRepository;
    private final StopTimeRepository stopTimeRepository;
    private final ServiceRepository serviceRepository;
    private final AreaRepository areaRepository;
    private final CoordinateRepository coordinateRepository;

    private final Logger logger = LoggerFactory.getLogger(BussStopService.class);

    @Override
    public BussStop saveBussStop(BussStop bussStop) {
        bussStopRepository.save(bussStop);
        return bussStop;
    }

    @Override
    public Collection<Area> getAllAreas() {
        List<Area> list = new ArrayList<>();
        for (Area area : areaRepository.findAll()) {
            if (!area.getAreaName().equals("")) {
                list.add(area);
            }
        }
        Collections.sort(list);
        return list;
    }

    @Override
    public HashMap<String, Collection<BussStop>> getRouteStopsBySpecificStop(BussStop bussStop) {
        HashMap<String, Collection<BussStop>> map = new HashMap<>();
        Collection<String> bussNumbers = new ArrayList<>();
        for (StopTime stopTime : stopTimeRepository.findAllByBussStop(bussStop).stream().toList()) {
            ArrayList<StopTime> stopTimes = (ArrayList<StopTime>) stopTimeRepository.findAllByTrip(stopTime.getTrip());
            String tempBussNumber = stopTimes.get(0).getTrip().getRoute().getRouteShortName();
            if (!bussNumbers.contains(stopTimes.get(0).getTrip().getRoute().getRouteShortName())) {
                Collection<BussStop> bussStops = new ArrayList<>();
                for (StopTime stopTime1 : stopTimes) {
                    bussStops.add(stopTime1.getBussStop());
                }
                map.put(tempBussNumber, bussStops);
            }
            bussNumbers.add(tempBussNumber);
        }
        return map;
    }

    @Override
    public Map<String, Collection<BussStop>> getRouteStopsBetweenTwoStops(BussStop userStop, BussStop targetStop) {
        HashMap<String, Collection<BussStop>> map = new HashMap<>();
        for (StopTime stopTime : stopTimeRepository.findAllByBussStop(userStop).stream().toList()) {
            List<StopTime> stopTimes = stopTime.getTrip().getStopTime().stream().toList();
            Collection<BussStop> bussStops = new ArrayList<>();
            String tempBussNumber = stopTime.getTrip().getRoute().getRouteShortName();
            boolean tempUserStop = false;
            boolean tempTargetStop = false;
            boolean targetAfterUser = false;
            for (StopTime stopTimeInTrip : stopTimes) {
                if (stopTimeInTrip.getBussStop().equals(userStop)) {
                    tempUserStop = true;
                }
                if (stopTimeInTrip.getBussStop().equals(targetStop)) {
                    tempTargetStop = true;
                }
                if (!targetAfterUser) {
                    if (tempUserStop && !tempTargetStop) {
                        targetAfterUser = true;
                    }
                }
                bussStops.add(stopTimeInTrip.getBussStop());
            }
            if (tempTargetStop && tempUserStop && targetAfterUser) {
                map.put(tempBussNumber, bussStops);
            }
        }
        return map;
    }
}
