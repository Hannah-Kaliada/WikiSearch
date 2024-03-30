package com.search.wiki.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

/** The type Country. */
@Getter
@Setter
@Entity
@Table(name = "countries")
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
public class Country {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(nullable = false, unique = true)
  private String name;

  @OneToMany(mappedBy = "country", fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
  @JsonManagedReference
  private List<User> users = new ArrayList<>();

  /**
   * Gets users.
   *
   * @return the users
   */
  public List<User> getUsers() {
    return users;
  }

  /**
   * Sets users.
   *
   * @param users the users
   */
  public void setUsers(List<User> users) {
    this.users = users;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Country country = (Country) o;
    return id == country.getId();
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
