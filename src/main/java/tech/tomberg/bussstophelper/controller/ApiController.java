package tech.tomberg.bussstophelper.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.tomberg.bussstophelper.entity.Area;
import tech.tomberg.bussstophelper.entity.BussStop;
import tech.tomberg.bussstophelper.repository.*;
import tech.tomberg.bussstophelper.service.implementation.MainService;
import tech.tomberg.bussstophelper.service.implementation.BussStopService;

import java.util.Collection;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiController {

    private final MainService mainService;

    private final BussStopService bussStopService;

    private final BussStopRepository bussStopRepository;
    private final AgencyRepository agencyRepository;
    private final RouteRepository routeRepository;
    private final TripRepository tripRepository;
    private final StopTimeRepository stopTimeRepository;
    private final ServiceRepository serviceRepository;
    private final AreaRepository areaRepository;

    @GetMapping("/stopsByArea/{id}")
    public Collection<BussStop> getStopsByAreaId(@PathVariable("id") long id) {
        return mainService.getStopsByAreaId(id);
    }

    @GetMapping("/getAllAreas")
    public Collection<Area> getAllAreas() {
        return bussStopService.getAllAreas();
    }

    @GetMapping("/getBussesByBussStopId/{id}")
    public Collection<String> getTimesById(@PathVariable("id") long id) {
        return mainService.getBussesNamesByBussStopId(id);
    }

}
