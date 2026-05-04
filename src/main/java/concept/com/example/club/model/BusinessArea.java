package concept.com.example.club.model;

import jakarta.persistence.*;

@Entity
@Table(name = "business_area")
public class BusinessArea {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String name;
}
