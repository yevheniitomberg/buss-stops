package tech.tomberg.bussstophelper.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "agencies")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Agency {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    private String agencyName;

    private String agencyUrl;

    private String agencyTimezone;

    private String agencyPhone;

    private String agencyLang;

    @OneToMany
    private Collection<Route> routes;

}