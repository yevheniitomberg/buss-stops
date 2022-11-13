package tech.tomberg.bussstophelper.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;
import tech.tomberg.bussstophelper.entity.BussStop;
import tech.tomberg.bussstophelper.entity.Coordinate;
import tech.tomberg.bussstophelper.entity.StopTime;
import tech.tomberg.bussstophelper.repository.BussStopRepository;
import tech.tomberg.bussstophelper.repository.CoordinateRepository;
import tech.tomberg.bussstophelper.repository.StopTimeRepository;
import tech.tomberg.bussstophelper.service.implementation.MainService;
import tech.tomberg.bussstophelper.service.implementation.BussStopService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@RequestMapping("/")
@RequiredArgsConstructor
@Controller
public class MainController {

    private final BussStopService bussStopService;
    private final MainService mainService;
    private final StopTimeRepository stopTimeRepository;
    private final BussStopRepository bussStopRepository;
    private final CoordinateRepository coordinateRepository;
    public static Logger logger = LoggerFactory.getLogger(MainController.class);

    @GetMapping("")
    public String mainView(HttpServletResponse response,
                           Model model,
                           @CookieValue(name = "control", defaultValue = "0") String control,
                           @CookieValue(name = "user_lat", defaultValue = "0", required = false) String user_lat,
                           @CookieValue(name = "user_lon", defaultValue = "0", required = false) String user_lon
    ) {
        logger.info(user_lat + " " + user_lon);
        if (control.equals("0")) {
            response.addCookie(new Cookie("control", "0"));
        }
        Coordinate bussStopCoordinate = new Coordinate();

        if (!user_lon.equals("0") && !user_lat.equals("0")) {
            Coordinate coordinate = new Coordinate(Double.parseDouble(user_lat), Double.parseDouble(user_lon));
            bussStopCoordinate = coordinate.findClosestCoordinate(coordinateRepository.findAll());
            response.addCookie(new Cookie("buss_stop_lat", Double.toString(bussStopCoordinate.getLat())));
            response.addCookie(new Cookie("buss_stop_lon", Double.toString(bussStopCoordinate.getLon())));
            response.addCookie(new Cookie("buss_stop_cord_id", Long.toString(bussStopCoordinate.getId())));
        } else {
            model.addAttribute("areas", bussStopService.getAllAreas());
            return "mainView";
        }

        BussStop userBussStop = bussStopRepository.getBussStopByCoordinate(bussStopCoordinate);

        model.addAttribute("areas", bussStopService.getAllAreas());
        model.addAttribute("user_area", userBussStop.getStopArea());
        model.addAttribute("user_stop", userBussStop);
        return "redirect:/?region=" + bussStopRepository.getBussStopByCoordinate(bussStopCoordinate).getStopArea().getId();
    }

    @GetMapping(value = "", params = {"stopId", "search_mode"})
    public String stopView(@RequestParam("stopId") long stopId,
                           @RequestParam("search_mode") String searchMode,
                           HttpServletResponse response,
                           Model model,
                           HttpServletRequest request
    ) {
        Collection<BussStop> bussStops = new ArrayList<>();
        if (searchMode.equals("userLocation")) {
            Map<String, Collection<BussStop>> map = bussStopService.getRouteStopsBetweenTwoStops(bussStopRepository.getBussStopByCoordinate(coordinateRepository.getById(Long.parseLong(getCookieByName(request, "buss_stop_cord_id")))), bussStopRepository.getById(stopId));
            model.addAttribute("bussNumbers", mainService.getBussesNamesByMap(map));
            model.addAttribute("allStopTimes", mainService.getStopTimesBetweenTwoStops(map, (bussStopRepository.getBussStopByCoordinate(coordinateRepository.getById(Long.parseLong(getCookieByName(request, "buss_stop_cord_id")))).getStopId())));
            model.addAttribute("routes", map);
            model.addAttribute("foundCondition", map.isEmpty());
        } else if (searchMode.equals("checkSchedule")) {
            model.addAttribute("bussNumbers", mainService.getBussesNamesByBussStopId(stopId));
            model.addAttribute("allStopTimes", mainService.getStopTimesByBussStopId(stopId));
            model.addAttribute("routes", bussStopService.getRouteStopsBySpecificStop(bussStopRepository.getById(stopId)));
            model.addAttribute("foundCondition", false);
        }

        bussStops = mainService.getStopsByAreaId(bussStopRepository.getById(stopId).getStopArea().getId());
        bussStops.removeIf(bussStop -> mainService.getBussesNamesByBussStopId(bussStop.getStopId()).isEmpty());

        setUpUserLocationData(model, request);
        model.addAttribute("idForSelectedArea", bussStopRepository.getById(stopId).getStopArea().getId());
        model.addAttribute("idForSelectedStop", stopId);
        model.addAttribute("areas", bussStopService.getAllAreas());
        model.addAttribute("stops", bussStops);
        return "mainView";
    }

    @GetMapping(value = "/", params = {"region"})
    public String chooseStopAfterArea(@RequestParam("region") long areaId,
                                      HttpServletResponse response,
                                      Model model,
                                      HttpServletRequest request
    ){
        Collection<BussStop> bussStops = mainService.getStopsByAreaId(areaId);
        bussStops.removeIf(bussStop -> mainService.getBussesNamesByBussStopId(bussStop.getStopId()).isEmpty());
        setUpUserLocationData(model, request);
        model.addAttribute("idForSelectedArea", areaId);
        model.addAttribute("areas", bussStopService.getAllAreas());
        model.addAttribute("stops", bussStops);
        return "mainView";
    }

    @GetMapping("notgranted")
    public String notGrantedLocation(Model model) {
        return "notGranted";
    }

    public void setUpUserLocationData(Model model, HttpServletRequest request) {
        BussStop userBussStop = bussStopRepository.getBussStopByCoordinate(coordinateRepository.getById(Long.parseLong(getCookieByName(request,"buss_stop_cord_id"))));
        model.addAttribute("user_area", userBussStop.getStopArea());
        model.addAttribute("user_stop", userBussStop);
    }

    public String getCookieByName(HttpServletRequest request, String name) {
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals(name)) {
                return cookie.getValue();
            }
        }
        throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Cookie was not found");
    }
}
