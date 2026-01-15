package ro.facultate.pos.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "bon_produse")
@Getter
@Setter
public class BonProdus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "bon_id")
    private Bon bon;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "produs_id")
    private Produs produs;

    @NotNull
    @Positive
    private Integer cantitate;

    @NotNull
    @Positive
    private BigDecimal pretUnitar;

    public BonProdus() {}
}
