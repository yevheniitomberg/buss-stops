package tech.tomberg.bussstophelper.service;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DBToolsInterface {
    void clearDB();
    void loadTXTFilesFromURL();
    void parseBussStops() throws Exception;
    void parseAgencies() throws Exception;
    void parseRoutes() throws Exception;
    void parseServices() throws Exception;
    void parseTrips() throws Exception;
    void parseStopTimes() throws Exception;
    void fillCoordinates() throws Exception;
}
