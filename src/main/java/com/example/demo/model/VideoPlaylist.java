package com.example.demo.model;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
@Table(indexes = @Index(columnList = "orderNumber"))
public class VideoPlaylist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private String id;

    @ManyToOne
    private Video video;

    @ManyToOne
    private Playlist playlist;

    @Column(name = "orderNumber")
    private Integer orderNumber;

}
