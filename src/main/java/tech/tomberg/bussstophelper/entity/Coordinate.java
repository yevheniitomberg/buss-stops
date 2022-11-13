package tech.tomberg.bussstophelper.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "coordiantes")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
@ToString
@Builder
public class Coordinate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private double lat;

    private double lon;

    public Coordinate(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public Coordinate findClosestCoordinate(Collection<Coordinate> coordinates) {
        double range = 100;
        Coordinate closest = new Coordinate();
        for (Coordinate coordinate : coordinates) {
            double currentRange = getRange(this, coordinate);
            if (currentRange < range) {
                range = currentRange;
                closest = coordinate;
            }
        }
        return closest;
    }

    public double getRange(Coordinate from, Coordinate to) {
        return Math.sqrt(Math.pow((from.lat - to.lat), 2)+Math.pow((from.lon - to.lon), 2));
    }
}