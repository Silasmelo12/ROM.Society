package concept.com.example.club.model;

import concept.com.example.club.enumeration.Plan;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Plan plan;

    @Column(nullable = false)
    private String avatar;

    @Column(nullable = false)
    private Boolean active;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private String preference;

    //@Column(nullable = false)
    //private String hobby;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "usuarios_hobbies", // Nome da 3ª tabela que será criada no banco
            joinColumns = @JoinColumn(name = "usuario_id"), // A coluna que aponta para o Usuário
            inverseJoinColumns = @JoinColumn(name = "hobby_id") // A coluna que aponta para o Hobby
    )
    private Set<Hobby> hobbies = new HashSet<>();

    public void addHobby(Hobby hobby) {
        this.hobbies.add(hobby);
        hobby.getUsers().add(this);
    }
    // Atividades favoritas
}
