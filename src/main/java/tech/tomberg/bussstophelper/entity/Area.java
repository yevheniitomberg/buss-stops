package tech.tomberg.bussstophelper.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "areas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Area implements Comparable<Area>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(unique = true)
    private String areaName;

    @Override
    public int compareTo(Area o) {
        return this.areaName.compareTo(o.areaName);
    }
}