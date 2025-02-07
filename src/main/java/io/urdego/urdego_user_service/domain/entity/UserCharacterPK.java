package io.urdego.urdego_user_service.domain.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@Getter
@AllArgsConstructor(staticName = "of")
@EqualsAndHashCode
public class UserCharacterPK implements Serializable {
    private Long userId;
    private Long characterId;
}
