package com.example.demo.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Table
@Entity
public class Playlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private String id;

    @Column
    private String name;

    @JsonIgnore
    @ManyToOne
    private User user;

    public Playlist(String name, User user, List<Video> videoList, List<Category> categories) {
        this.name = name;
        this.user = user;
        this.videoList = videoList;
        this.categories = categories;
    }

    @OneToMany
    @ToString.Exclude
    private List<Video> videoList;

    @OneToMany
    @ToString.Exclude
    private List<Category> categories;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Playlist playlist = (Playlist) o;
        return id != null && Objects.equals(id, playlist.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
