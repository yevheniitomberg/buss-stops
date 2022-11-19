package tech.tomberg.bussstophelper.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "services")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Service {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;
    private int monday;
    private int tuesday;
    private int wednesday;
    private int thursday;
    private int friday;
    private int saturday;
    private int sunday;
    private LocalDate startDate;
    private LocalDate endDate;

    public boolean isWorkingToday() {
        return switch (LocalDate.now().getDayOfWeek().getValue()) {
            case 1 -> monday == 1;
            case 2 -> tuesday == 1;
            case 3 -> wednesday == 1;
            case 4 -> thursday == 1;
            case 5 -> friday == 1;
            case 6 -> saturday == 1;
            case 7 -> sunday == 1;
            default -> false;
        };
    }
}