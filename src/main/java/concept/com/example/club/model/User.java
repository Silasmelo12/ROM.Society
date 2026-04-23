package concept.com.example.club.model;

import concept.com.example.club.enumeration.Plan;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // Controla o equals e hashCode
@ToString(exclude = {"password", "hobbies"}) // Evita expor dados sensíveis e lazy loading
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    @EqualsAndHashCode.Include // Usa o email para equals/hashCode, pois é um identificador de negócio único
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Column(nullable = false)
    private String password;

    // Enumeração para o plano do usuário, armazenada como String no banco
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Plan plan;

    @Column(nullable = false)
    private String avatar;

    @Column(nullable = false)
    private Boolean active;

    // Timestamps de auditoria
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // Preferências do usuário (pode ser um JSON ou um campo de texto simples)
    @Column(nullable = false)
    private String preference;

    // Relacionamento Many-to-Many com Hobby
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_hobbies", // Convenção em inglês
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "hobby_id")
    )
    private Set<Hobby> hobbies = new HashSet<>();

    // Métodos de ciclo de vida para automatizar timestamps
    @PrePersist
    protected void onCreate() {
        createdAt = updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Métodos auxiliares para gerenciar o relacionamento bidirecional com Hobby
    public void addHobby(Hobby hobby) {
        this.hobbies.add(hobby);
        hobby.getUsers().add(this);
    }

    public void removeHobby(Hobby hobby) {
        this.hobbies.remove(hobby);
        hobby.getUsers().remove(this);
    }
}
