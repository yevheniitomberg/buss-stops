package tech.tomberg.bussstophelper.entity;

import lombok.*;
import javax.persistence.*;

@Entity
@Table(name = "buss_stops")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class BussStop {

    @Id
    @Column(name = "stop_id", nullable = false)
    private Long stopId;

    private String stopCode;

    private String stopName;

    private String stopLat;

    private String stopLon;

    @OneToOne
    private Coordinate coordinate;

    private String zoneId;

    private String alias;

    @OneToOne
    private Area stopArea;
    
    private String stopDesc;

    private String lestX;

    private String lestY;

}