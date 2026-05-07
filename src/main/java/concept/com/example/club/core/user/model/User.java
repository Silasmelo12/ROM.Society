package concept.com.example.club.core.user.model;

import concept.com.example.club.core.user.enumeration.Plan;
import concept.com.example.club.core.salon.Salon;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // Controla o equals e hashCode
@ToString(exclude = {"password", "hobbies", "homeSalon", "adress","preferences"}) // Evita expor dados sensíveis e lazy loading
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    @EqualsAndHashCode.Include // Usa o email para equals/hashCode, pois é um identificador de negócio único
    private String email;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private LocalDate birthDate;

    @OneToOne
    @JoinColumn(name = "adress_id")
    private Adress adress;

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

    // Relacionamento Many-to-Many com Preference
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_preferences",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "preference_id")
    )

    private Set<Preference> preferences = new HashSet<>();

    // Relacionamento Many-to-Many com Hobby
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_hobbies", // Nome da tabela intermediária
            joinColumns = @JoinColumn(name = "user_id"), // nome da chave estrangeira da tabela onde você está
            inverseJoinColumns = @JoinColumn(name = "hobby_id") // nome da chave estrangeira da tabela relacionada
    )
    private Set<Hobby> hobbies = new HashSet<>();

    // Relacionamento para indicar o salão principal do usuário
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "salon_id")
    private Salon homeSalon;

    @ManyToOne
    @JoinColumn(name = "profession_id")
    private Profession profession;

    @ManyToOne
    @JoinColumn(name = "business_area_id")
    private BusinessArea businessArea;

    @ManyToOne
    @JoinColumn(name = "revenue_range_id")
    private RevenueRange revenueRange;

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

    public void addPreference(Preference preference){
        this.preferences.add(preference);
        preference.getUsers().add(this);
    }

    public void removeHobby(Hobby hobby) {
        this.hobbies.remove(hobby);
        hobby.getUsers().remove(this);    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_"+this.plan));
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return this.active;
    }

    public void removePreference(Preference preference){
        this.preferences.remove(preference);
        preference.getUsers().remove(this);
    }
}
