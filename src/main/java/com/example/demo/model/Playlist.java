package com.example.demo.model;


import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.Set;

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

    @ToString.Exclude
    @OneToMany
    private List<PlaylistChannel> playlistChannels;

    @ToString.Exclude
    @OneToMany
    List<VideoPlaylist> videoPlaylists;

    @Column
    private String imageSrc;

    public Playlist(String name, Set<Category> categories) {
        this.name = name;
        this.categories = categories;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @ToString.Exclude
    private Set<Category> categories;

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
