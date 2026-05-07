package concept.com.example.club.core.user.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "revenue_ranges")
public class RevenueRange {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String label; // Ex: "R$ 100k a R$ 500k"

    @Column(precision = 19, scale = 2)
    private BigDecimal minValue;

    @Column(precision = 19, scale = 2)
    private BigDecimal maxValue; // Pode ser null para a faixa "Acima de X"
}
