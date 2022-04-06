package com.example.demo.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ChannelPlaylistOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private String id;

    @OneToOne
    private Channel channel;

    @OneToOne
    private Playlist playlist;

    private Integer orderNumber;

}
