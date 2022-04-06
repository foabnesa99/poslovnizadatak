package com.example.demo.model;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;


@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table
public class Channel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private String id;

    @OneToMany
    @ToString.Exclude
    private List<Playlist> playlistList;



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Channel channel = (Channel) o;
        return id != null && Objects.equals(id, channel.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
