package com.levopravoce.backend.entities;

import com.levopravoce.backend.services.authenticate.dto.UserDTO;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuario")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)    
    private Long id;

    @Column(name = "senha")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "primeiro_nome")
    private String firstName;

    @Column(name = "sobrenome")
    private String lastName;

    @Column(name = "cpf")
    private String cpf;

    @Enumerated(EnumType.STRING)
    private Status status = Status.INACTIVE;

    @Column(name = "tipo")
    @Enumerated(EnumType.STRING)
    private UserType userType;

    @Column(name = "ativo")
    private Boolean active = false;

    @Column(name = "data_criacao")

    private LocalDate creationDate;
    @Column(name = "contato")
    private String contact;

    @OneToMany
    @JoinColumn(name = "id_usuario")
    private List<Address> addresses;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "perfil_usuario",
            joinColumns = @JoinColumn(name = "id_usuario"),
            inverseJoinColumns = @JoinColumn(name = "id_perfil")
    )
    private List<Profile> profiles;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "usuario_grupo",
            joinColumns = @JoinColumn(name = "id_usuario"),
            inverseJoinColumns = @JoinColumn(name = "id_grupo")
    )
    private List<Group> groups;

    @OneToMany
    @JoinColumn(name = "id_usuario")
    private List<Vehicle> vehicles;

    @OneToMany
    @JoinColumn(name = "id_usuario")
    private List<Rating> ratings;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Optional.ofNullable
                (this.profiles).map(profiles -> profiles.stream()
                .map(
                    profile -> profile.getPermissions().stream()
                        .map(Permission::getName)
                        .map(SimpleGrantedAuthority::new)
                        .toList()
                ).flatMap(Collection::stream)
            .collect(Collectors.toSet())
        ).orElse(Collections.emptySet());
    }
    @Override
    public boolean isAccountNonExpired() {
        return this.isUserActive();
    }
    @Override
    public boolean isAccountNonLocked() {
        return this.isUserActive();
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return this.isUserActive();
    }
    
    @Override
    public boolean isEnabled() {
        return this.isUserActive();
    }
    @Override
    public String getUsername() {
        return this.email;
    }

    private boolean isUserActive() {
        return Optional.ofNullable(this.status).map(status -> status.equals(Status.ACTIVE)).orElse(false);
    }

    public UserDTO toDTO() {
        return UserDTO.builder()
                .email(this.email)
                .firstName(this.firstName)
                .lastName(this.lastName)
                .cpf(this.cpf)
                .contact(this.contact)
                .build();
    }
}
