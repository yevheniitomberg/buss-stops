package tech.tomberg.bussstophelper.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.tomberg.bussstophelper.entity.BussStop;
import tech.tomberg.bussstophelper.entity.StopTime;
import tech.tomberg.bussstophelper.repository.*;
import tech.tomberg.bussstophelper.service.MainServiceInterface;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MainService implements MainServiceInterface {

    private final BussStopRepository bussStopRepository;
    private final AgencyRepository agencyRepository;
    private final RouteRepository routeRepository;
    private final TripRepository tripRepository;
    private final StopTimeRepository stopTimeRepository;
    private final ServiceRepository serviceRepository;
    private final AreaRepository areaRepository;

    public static final int COUNT_OF_NEXT_SELECTED_BUSSES = 5;

    @Override
    public Collection<BussStop> getStopsByAreaId(long id) {
        return bussStopRepository.findAllByStopArea(id);
    }

    @Override
    public Collection<String> getBussesNamesByBussStopId(long id) {
        List<StopTime> stopTimes = stopTimeRepository.findAllByBussStop(id).stream().toList();
        List<String> busses = new LinkedList<>();
        for (StopTime stopTime : stopTimes) {
            if (!busses.contains(stopTime.getTrip().getRoute().getRouteShortName())) {
                busses.add(stopTime.getTrip().getRoute().getRouteShortName());
            }
        }
        busses.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return extractInt(o1) - extractInt(o2);
            }

            int extractInt(String s) {
                String num = s.replaceAll("\\D", "");
                return num.isEmpty() ? 0 : Integer.parseInt(num);
            }
        });
        return busses;
    }

    @Override
    public Collection<String> getBussesNamesByMap(Map<String, Collection<BussStop>> map) {
        List<String> bussNumbers = new ArrayList<>();
        for (Map.Entry<String, Collection<BussStop>> entry : map.entrySet()) {
            bussNumbers.add(entry.getKey());
        }
        bussNumbers.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return extractInt(o1) - extractInt(o2);
            }

            int extractInt(String s) {
                String num = s.replaceAll("\\D", "");
                return num.isEmpty() ? 0 : Integer.parseInt(num);
            }
        });
        return bussNumbers;
    }

    @Override
    public Collection<Collection<StopTime>> getStopTimesByBussStopId(long id) {
        Collection<StopTime> allStopTimes = stopTimeRepository.findAllByBussStop(id).stream().toList();
        Collection<String> bussNames = getBussesNamesByBussStopId(id);
        Collection<Collection<StopTime>> stopTimeCollections = new HashSet<>();
        for (String bussName : bussNames) {
            List<StopTime> stopTimes = new ArrayList<>();
            for (StopTime time : allStopTimes) {
                if (time.getTrip().getRoute().getRouteShortName().equals(bussName)) {
                    stopTimes.add(time);
                }
            }
            Collections.sort(stopTimes);
            findClosestStopTimes(stopTimeCollections, stopTimes);
        }
        return stopTimeCollections;
    }

    @Override
    public Collection<Collection<StopTime>> getStopTimesBetweenTwoStops(Map<String, Collection<BussStop>> map, long id) {
        Collection<StopTime> allStopTimes = stopTimeRepository.findAllByBussStop(id).stream().toList();
        Collection<String> bussNames = getBussesNamesByMap(map);
        Collection<Collection<StopTime>> stopTimeCollections = new HashSet<>();
        for (String bussName : bussNames) {
            List<StopTime> stopTimes = new ArrayList<>();
            for (StopTime time : allStopTimes) {
                if (time.getTrip().getRoute().getRouteShortName().equals(bussName)) {
                    stopTimes.add(time);
                }
            }
            Collections.sort(stopTimes);
            findClosestStopTimes(stopTimeCollections, stopTimes);
        }
        return stopTimeCollections;
    }


    private void findClosestStopTimes(Collection<Collection<StopTime>> stopTimeCollections, List<StopTime> stopTimes) {
        int counter = 0;
        LocalTime localTime = LocalTime.now(ZoneId.of("Europe/Tallinn"));
        for (StopTime stopTime : stopTimes) {
            if (stopTime.getDepartureTime().toLocalTime().compareTo(localTime) > 0 && counter < COUNT_OF_NEXT_SELECTED_BUSSES) {
                stopTime.setClose(true);
                counter++;
            }
        }
        stopTimeCollections.add(stopTimes);
    }
}
