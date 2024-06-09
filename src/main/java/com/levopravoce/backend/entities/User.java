package com.levopravoce.backend.entities;

import com.levopravoce.backend.services.authenticate.dto.UserDTO;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuario")
public class User implements UserDetails {

  public User(Long id) {
    this.id = id;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "senha")
  private String password;

  @Column(name = "email")
  private String email;

  @Column(name = "nome")
  private String name;

  @Column(name = "cpf")
  private String cpf;

  @Column(name = "cnh")
  private String cnh;

  @Enumerated(EnumType.STRING)
  private Status status = Status.INACTIVE;

  @Column(name = "tipo")
  @Enumerated(EnumType.STRING)
  private UserType userType;

  @Column(name = "ativo")
  private Boolean active = false;

  @Column(name = "data_criacao")
  private LocalDateTime creationDate;
  @Column(name = "contato")
  private String contact;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JoinColumn(name = "id_usuario")
  private List<Address> addresses;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "perfil_usuario", joinColumns = @JoinColumn(name = "id_usuario"), inverseJoinColumns = @JoinColumn(name = "id_perfil"))
  private List<Profile> profiles;

  @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
  @JoinColumn(name = "id_usuario")
  private List<Vehicle> vehicles;

  @OneToMany
  @JoinColumn(name = "id_usuario")
  private List<Rating> ratings;

  @Transient
  private Date expirationDate;

  @Column(name = "foto")
  private byte[] profilePicture;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Optional.ofNullable(this.profiles).map(profiles -> profiles.stream().map(
            profile -> profile.getPermissions().stream().map(Permission::getName)
                .map(SimpleGrantedAuthority::new).toList()).flatMap(Collection::stream)
        .collect(Collectors.toSet())).orElse(Collections.emptySet());
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
    if (this.expirationDate == null) {
      return true;
    }

    return this.expirationDate.after(new Date());
  }

  @Override
  public boolean isEnabled() {
    return this.isUserActive();
  }

  @Override
  public String getUsername() {
    return this.email;
  }

  @Override
  public String getPassword() {
    return this.password;
  }

  private boolean isUserActive() {
    return Optional.ofNullable(this.status).map(status -> status.equals(Status.ACTIVE))
        .orElse(false);
  }

  public List<Vehicle> getVehicles() {
    if (this.vehicles != null) {
      return this.vehicles.stream().filter(Vehicle::isActive).toList();
    }
    return this.vehicles;
  }

  public UserDTO toDTO() {
    Address address = this.getAddresses().stream().findFirst().orElse(null);

    return UserDTO.builder().id(this.getId()).email(this.getEmail()).name(this.getName())
        .zipCode(Optional.ofNullable(address).map(Address::getZipCode).orElse(null))
        .city(Optional.ofNullable(address).map(Address::getCity).orElse(null))
        .complement(Optional.ofNullable(address).map(Address::getComplement).orElse(null))
        .phone(this.getContact()).vehicle(
            Optional.ofNullable(this.getVehicles()).orElse(List.of()).stream().findFirst()
                .orElse(null)).status(this.getStatus().name()).userType(this.getUserType()).street(
            Optional.ofNullable(this.getAddresses()).orElse(List.of()).stream().findFirst()
                .map(Address::getStreet).orElse(null)).build();
  }
}
