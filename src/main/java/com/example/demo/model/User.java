package com.example.demo.model;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private String id;

    @Column
    private String name;

    @Column//(unique = true)
    private String username;

    @Column
    private String password;

    @Column
    private UserRoles roles;



    @ToString.Exclude
    @OneToOne
    Channel channel;

    public User(String name, String username, String password, UserRoles roles) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return id != null && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
