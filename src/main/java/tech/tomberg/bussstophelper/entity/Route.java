package tech.tomberg.bussstophelper.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "routes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Route {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @ManyToOne
    private Agency agency;

    private String routeShortName;

    private String routeLongName;

    private String routeType;

    private String routeColor;

    private String competentAuthority;

    public String getShortAndLongNames() {
        return this.routeShortName + "  -  " + this.routeLongName;
    }

}