package uz.pdp.linkedin.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import uz.pdp.linkedin.Entity.BaseEntity.BaseEntity;

import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User extends BaseEntity implements UserDetails {
   private String fullName;
   @JsonIgnore
   private String password;
   private String email;
   private String phone;
   private String address;
   @ManyToOne
   private Attachment photo;
   @ManyToMany
   private List<Role>roles;
   @ManyToMany
   private List<Skill> skills;
   @OneToMany
   private List<Work> works;
   @OneToMany
   private List<Education> educations;
   @ManyToMany
   private List<User>connections;
   @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL)
   private List<Post> posts;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles;
    }

    @Override
    public String getUsername() {
        return email;
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
