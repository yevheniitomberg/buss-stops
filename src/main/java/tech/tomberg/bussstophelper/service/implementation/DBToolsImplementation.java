package tech.tomberg.bussstophelper.service.implementation;

import lombok.RequiredArgsConstructor;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tech.tomberg.bussstophelper.entity.*;
import tech.tomberg.bussstophelper.entity.embedd.ShapeId;
import tech.tomberg.bussstophelper.repository.*;
import tech.tomberg.bussstophelper.service.DBToolsInterface;

import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.sql.Time;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class DBToolsImplementation implements DBToolsInterface {


    private final BussStopService bussStopService;

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
    public void clearDB() {
        bussStopRepository.deleteAll();
        logger.info("BussStops cleared");
        areaRepository.deleteAll();
        logger.info("Areas cleared");
        routeRepository.deleteAll();
        logger.info("Routes cleared");
        agencyRepository.deleteAll();
        logger.info("Agencies cleared");
        serviceRepository.deleteAll();
        logger.info("Services cleared");
        tripRepository.deleteAll();
        logger.info("Trips cleared");
        stopTimeRepository.deleteAll();
        logger.info("Stop times cleared");
        logger.info("DB cleared");
    }

    @Override
    public void parseAgencies() throws Exception {
        File file = new File("src/main/resources/static/text/agency.txt");
        BufferedReader bufferedReader = new BufferedReader(new FileReader((file)));

        LinkedList<List<String>> list = new LinkedList<>();

        String line = bufferedReader.readLine();

        while ((line = bufferedReader.readLine() ) != null) {
            list.add(Arrays.stream(line.strip().split(",")).toList());
        }

        for (List<String> strings : list) {
            if (Long.parseLong(strings.get(0)) == 0) {
                continue;
            }
            agencyRepository.save(Agency.builder()
                            .id(Long.valueOf(strings.get(0)))
                            .agencyName(strings.get(1))
                            .agencyUrl(strings.get(2))
                            .agencyTimezone(strings.get(3))
                            .agencyPhone(strings.get(4))
                            .agencyLang(strings.get(5))
                            .build());
        }
        logger.info("Agency loading is successfully done");
    }

    @Override
    public void parseRoutes() throws Exception {
        File file = new File("src/main/resources/static/text/routes.txt");
        BufferedReader bufferedReader = new BufferedReader(new FileReader((file)));

        LinkedList<List<String>> list = new LinkedList<>();

        String line = bufferedReader.readLine();

        while ((line = bufferedReader.readLine() ) != null) {
            list.add(Arrays.stream(line.strip().split(",")).toList());
        }

        for (List<String> strings : list) {
            routeRepository.save(Route.builder()
                            .id(strings.get(0))
                            .agency(agencyRepository.getById(Long.valueOf(strings.get(1))))
                            .routeShortName(strings.get(2))
                            .routeLongName(strings.get(3))
                            .routeType(strings.get(4))
                            .routeColor(strings.get(5))
                            .competentAuthority(strings.get(6))
                            .build());
        }
        logger.info("Route loading is successfully done");
    }

    @Override
    public void parseServices() throws Exception {
        File file = new File("src/main/resources/static/text/calendar.txt");
        BufferedReader bufferedReader = new BufferedReader(new FileReader((file)));

        LinkedList<List<String>> list = new LinkedList<>();

        String line = bufferedReader.readLine();

        while ((line = bufferedReader.readLine() ) != null) {
            list.add(Arrays.stream(line.strip().split(",")).toList());
        }

        for (List<String> strings : list) {
            serviceRepository.save(tech.tomberg.bussstophelper.entity.Service.builder()
                            .id(Long.valueOf(strings.get(0)))
                            .monday(Integer.parseInt(strings.get(1)))
                            .tuesday(Integer.parseInt(strings.get(2)))
                            .wednesday(Integer.parseInt(strings.get(3)))
                            .thursday(Integer.parseInt(strings.get(4)))
                            .friday(Integer.parseInt(strings.get(5)))
                            .saturday(Integer.parseInt(strings.get(6)))
                            .sunday(Integer.parseInt(strings.get(7)))
                            .startDate(LocalDate.parse(strings.get(8), DateTimeFormatter.ofPattern("yyyyMMdd")))
                            .endDate(LocalDate.parse(strings.get(9), DateTimeFormatter.ofPattern("yyyyMMdd")))
                            .build());

        }
        logger.info("Services loading is successfully done");
    }

    @Override
    public void parseTrips() throws Exception {
        File file = new File("src/main/resources/static/text/trips.txt");
        BufferedReader bufferedReader = new BufferedReader(new FileReader((file)));

        LinkedList<List<String>> list = new LinkedList<>();

        String line = bufferedReader.readLine();

        while ((line = bufferedReader.readLine() ) != null) {
            list.add(Arrays.stream(line.strip().split(",")).toList());
        }

        for (List<String> strings : list) {
            tripRepository.save(Trip.builder()
                            .route(routeRepository.getById(strings.get(0)))
                            .service(serviceRepository.getById(Long.valueOf(strings.get(1))))
                            .id(Long.valueOf(strings.get(2)))
                            .tripHeadSign(strings.get(3))
                            .tripLongName(strings.get(4))
                            .directionCode(strings.get(5))
                            .build());
        }
        logger.info("Trip loading is successfully done");
    }

    @Override
    public void parseStopTimes() throws Exception {
        File file = new File("src/main/resources/static/text/stop_times.txt");
        BufferedReader bufferedReader = new BufferedReader(new FileReader((file)));

        LinkedList<List<String>> list = new LinkedList<>();

        String line = bufferedReader.readLine();

        while ((line = bufferedReader.readLine() ) != null) {
            list.add(Arrays.stream(line.strip().split(",")).toList());
        }

        for (List<String> strings : list) {
            stopTimeRepository.save(StopTime.builder()
                            .id(0L)
                            .trip(tripRepository.getById(Long.valueOf(strings.get(0))))
                            .arrivalTime(Time.valueOf(strings.get(1)))
                            .departureTime(Time.valueOf(strings.get(2)))
                            .bussStop(bussStopRepository.getById(Long.valueOf(strings.get(3))))
                    .build());
        }
        logger.info("StopTimes loading is successfully done");
    }

    @Override
    public void loadTXTFilesFromURL() {
        try (InputStream input = new FileInputStream("src/main/resources/application.properties")) {

            Properties prop = new Properties();
            prop.load(input);

            new FileOutputStream(prop.getProperty("zip.name")).getChannel().transferFrom(Channels.newChannel(new URL(prop.getProperty("zip.url")).openStream()), 0, Long.MAX_VALUE);

            try {
                ZipFile zipFile = new ZipFile(prop.getProperty("zip.name"));
                zipFile.extractAll( "src/main/resources/static/text");
            } catch (ZipException e) {
                e.printStackTrace();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void parseBussStops() throws Exception{
        File file = new File("src/main/resources/static/text/stops.txt");
        BufferedReader bufferedReader = new BufferedReader(new FileReader((file)));

        LinkedList<List<String>> list = new LinkedList<>();

        String line = bufferedReader.readLine();

        while ((line = bufferedReader.readLine() ) != null) {
            list.add(Arrays.stream(line.strip().split(",")).toList());
        }

        for (List<String> strings : list) {
            bussStopRepository.saveAndFlush(BussStop.builder()
                    .stopId(Long.valueOf(strings.get(0)))
                    .stopCode(strings.get(1))
                    .stopName(strings.get(2))
/*                    .coordinate(
                            Coordinate.builder()
                                    .lat(Double.parseDouble(strings.get(3)))
                                    .lon(Double.parseDouble(strings.get(4)))
                                    .build())*/
                    .zoneId(strings.get(5))
                    .alias(strings.get(6))
                    .stopArea(areaRepository.existsAreaByAreaName(strings.get(7).strip()) ? areaRepository.findAreaByAreaName(strings.get(7).strip()) :  areaRepository.save(new Area(0L, strings.get(7).strip())))
                    .stopDesc(strings.get(8))
                    .lestX(strings.get(9))
                    .lestY(strings.get(10))
                    .build());
        }
        logger.info("Buss stops loading is successfully done");
    }

    @Override
    public void fillCoordinates() throws Exception {
        for (BussStop bussStop : bussStopRepository.findAll()) {
            try {
                bussStop.setCoordinate(coordinateRepository.save(Coordinate.builder()
                        .lat(Double.parseDouble(bussStop.getStopLat()))
                        .lon(Double.parseDouble(bussStop.getStopLon()))
                        .build()));
                bussStopRepository.save(bussStop);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        logger.info("Coordinates were successfully filled");
    }
}
