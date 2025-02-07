package io.urdego.urdego_user_service.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "user_characters", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "character_id"})
}) // 중복 방지
public class UserCharacter{

    @EmbeddedId
    private UserCharacterPK userCharacterPK = new UserCharacterPK();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_id")
    @MapsId("characterId")
    private GameCharacter character;

    public UserCharacter(User user, GameCharacter character) {
        this.user = user;
        this.character = character;
    }
}
