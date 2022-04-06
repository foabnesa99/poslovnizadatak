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
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private String id;

    @Column
    private String name;

    @OneToMany
    @ToString.Exclude
    private List<Category> categories;

    public Video(String name, List<Category> categories) {
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
