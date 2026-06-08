package com.raizesdonordeste.api.domain;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
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
    @Column(nullable = false, name = "tipo_consentimento")
    @NotBlank
    private String tipoConsentimento;
    @Column(name = "versao_consentimento")
    private String versaoConsentimento;
    @Column(nullable = false)
    @NotNull
    private boolean aceito;
    @Column(name = "data_acao")
    private LocalDateTime dataAcao;
    @Column
    private String observacoes;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    @JsonBackReference
    private Usuario usuario;

    public void fornecerConsentimento(String tipoConsentimento, boolean aceito) {
        this.tipoConsentimento = tipoConsentimento;
        this.aceito = aceito;
        this.dataAcao = LocalDateTime.now();
    }
}
