package com.raizesdonordeste.api.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.raizesdonordeste.api.dto.UsuarioUpdateDTO;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="usuario")
public class Usuario implements UserDetails {
    //private final BCryptPasswordEncoder codificador = new BCryptPasswordEncoder();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @NotBlank
    @Size(min = 11, max = 14)
    private String cpf;

    @Column(nullable = false)
    @NotBlank
    @Size(max = 150)
    private String nome;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @Column(nullable = false, unique = true)
    @NotBlank
    @Email
    @Size(max = 150)
    private String email;

    @Column
    @Size(max = 20)
    private String telefone;
    
    @Column(nullable = false)
    @NotBlank
    @Size(min = 6, max = 255)
    @JsonIgnore
    private String senha;

    @Column
    @Size(max = 30)
    private String genero;

    @Column(name = "pontos_fidelidade")
    private int pontosFidelidade;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "perfil_usuario", 
    joinColumns = @JoinColumn(name = "usuario_id"),
    inverseJoinColumns = @JoinColumn(name = "perfil_id"))
    @JsonIgnoreProperties("usuarios")
    private List<Perfil> perfis  = new ArrayList<>();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({"usuario","unidade"})
    private List<Endereco> enderecos  = new ArrayList<>();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ConsentimentoLgpd> consentimentos  = new ArrayList<>();

    @OneToMany(mappedBy = "usuario")
    @JsonIgnoreProperties({"usuario","unidade"})
    private List<Pedido> pedidos = new ArrayList<>();

    public void alterarInformacoes(UsuarioUpdateDTO dto, String senhaCriptografada) {
        if(dto.nome() != null && !dto.nome().isBlank()){
            this.setNome(dto.nome());
        }
        if(dto.dataNascimento() != null){
            this.setDataNascimento(dto.dataNascimento());
        }
        if(dto.email() != null){
            this.setEmail(dto.email());
        }
        if(dto.telefone() != null){
            this.setTelefone(dto.telefone());
        }
        if(dto.genero() != null){
            this.setGenero(dto.genero());
        }
        if(dto.senha() != null && !dto.senha().isBlank()){
            this.setSenha(senhaCriptografada);
        }
    }

    //Métodos obrigatórios por implementar a interface UserDetails do Spring Segurity
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return this.perfis.stream()
            .map(perfil -> new SimpleGrantedAuthority("ROLE_"+perfil.getNome()))
            .toList();
    }

    @Override
    public String getPassword(){
        return this.senha;
    }

    @Override
    public String getUsername(){
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired(){
        return true;
    }

    @Override
    public boolean isAccountNonLocked(){
        return true;
    }

    @Override
    public boolean isEnabled(){
        return true;
    }
}
