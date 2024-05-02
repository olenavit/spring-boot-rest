package ua.com.vitkovska.model;

import io.hypersistence.utils.hibernate.type.array.ListArrayType;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Type;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "player")
@ToString
@Entity
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "year_of_birth")
    private Integer yearOfBirth;

    @Column(name = "position", columnDefinition = "text[]")
    @Type(ListArrayType.class)
    private List<String> position;

    @ManyToOne
    @Nullable
    @JoinColumn(name = "team", referencedColumnName = "id")
    private Team team;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(id, player.id) && Objects.equals(name, player.name)
                && Objects.equals(surname, player.surname)
                && Objects.equals(yearOfBirth, player.yearOfBirth)
                && Objects.equals(position, player.position)
                && Objects.equals(team.getName(), player.team.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, surname, yearOfBirth, position, team);
    }
}
