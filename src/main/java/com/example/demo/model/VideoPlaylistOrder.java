package com.example.demo.model;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class VideoPlaylistOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private String id;

    @ManyToOne
    private Video video;

    @ManyToOne
    private Playlist playlist;

    private Integer orderNumber;

}
