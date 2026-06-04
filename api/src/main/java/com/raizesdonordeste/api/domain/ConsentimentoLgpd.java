package com.raizesdonordeste.api.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "consentimento_lgpd")
public class ConsentimentoLgpd {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    @NotNull
    private boolean aceito;
    @Column(name = "data_aceite")
    private LocalDateTime dataAceite;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    public void fornecerConsentimento(boolean aceito) {
        this.aceito = aceito;
        if (this.aceito){
            this.dataAceite = LocalDateTime.now();
        } else {
            this.dataAceite = null;
        }
    }
}
