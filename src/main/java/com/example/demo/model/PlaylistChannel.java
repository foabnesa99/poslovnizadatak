package com.example.demo.model;


import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table
public class PlaylistChannel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private String id;



    @ManyToOne
    private Channel channel;

    @ManyToOne
    private Playlist playlist;

    private Integer orderNumber;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlaylistChannel)) return false;
        PlaylistChannel that = (PlaylistChannel) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getChannel(), that.getChannel()) && Objects.equals(getPlaylist(), that.getPlaylist()) && Objects.equals(getOrderNumber(), that.getOrderNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getChannel(), getPlaylist(), getOrderNumber());
    }
}
