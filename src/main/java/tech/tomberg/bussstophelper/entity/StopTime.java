package tech.tomberg.bussstophelper.entity;

import lombok.*;

import javax.persistence.*;
import java.sql.Time;

@Entity
@Table(name = "stop_times")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class StopTime implements Comparable<StopTime> {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "trip_id")
    private Trip trip;

    private Time arrivalTime;

    private Time departureTime;

    @Transient
    private boolean close;

    @OneToOne
    private BussStop bussStop;

    @Override
    public int compareTo(StopTime o) {
        return this.departureTime.compareTo(o.getDepartureTime());
    }

    public boolean isClose() {
        return close;
    }

    public void setClose(boolean close) {
        this.close = close;
    }
}