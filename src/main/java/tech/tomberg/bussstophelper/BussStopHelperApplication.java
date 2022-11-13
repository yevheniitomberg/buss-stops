package tech.tomberg.bussstophelper;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import tech.tomberg.bussstophelper.repository.BussStopRepository;
import tech.tomberg.bussstophelper.repository.RouteRepository;
import tech.tomberg.bussstophelper.service.implementation.BussStopService;
import tech.tomberg.bussstophelper.service.implementation.DBToolsImplementation;

@SpringBootApplication
public class BussStopHelperApplication {

    public static void main(String[] args) {
        SpringApplication.run(BussStopHelperApplication.class, args);
    }

    @Bean
    CommandLineRunner run(DBToolsImplementation dbToolsImplementation,
                          BussStopService bussStopService,
                          BussStopRepository bussStopRepository) throws Exception{
        return args -> {

/*
            dbToolsImplementation.clearDB();

            dbToolsImplementation.loadTXTFilesFromURL();

            dbToolsImplementation.parseAgencies();
            dbToolsImplementation.parseBussStops();
            dbToolsImplementation.parseServices();
            dbToolsImplementation.parseRoutes();*/
            //dbToolsImplementation.parseTrips();
            //dbToolsImplementation.parseStopTimes();

            //dbToolsImplementation.fillCoordinates();
        };
    }
}
