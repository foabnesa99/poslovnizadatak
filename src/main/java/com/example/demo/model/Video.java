package com.example.demo.model;


import lombok.*;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private String id;

    @Column
    private String name;

    @ToString.Exclude
    @OneToMany
    List<VideoPlaylist> videoPlaylists;

    @ManyToMany
    @ToString.Exclude
    private Set<Category> categories;

    @Column
    private String url;

    @Column
    private String videoUriId;

    public Video(String name, String url, Set<Category> categories) {
        this.url = url;
        this.name = name;
        this.categories = categories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Video video = (Video) o;
        return id != null && Objects.equals(id, video.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
