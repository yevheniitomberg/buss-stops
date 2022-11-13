package tech.tomberg.bussstophelper.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "trips")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Trip {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    private Route route;

    @ManyToOne
    private Service service;

    @OneToMany(mappedBy = "trip")
    private Collection<StopTime> stopTime;

    private String tripHeadSign;

    private String tripLongName;

    private String directionCode;
}