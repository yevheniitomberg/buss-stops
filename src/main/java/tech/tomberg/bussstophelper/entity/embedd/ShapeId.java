package tech.tomberg.bussstophelper.entity.embedd;

import lombok.*;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Embeddable
@Builder
public class ShapeId implements Serializable {
    private Long shapeId;
    private int shapeSequence;
}
