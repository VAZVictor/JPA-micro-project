package at.htl.workloads.audioItem;

import at.htl.workloads.artist.Artist;
import jakarta.persistence.*;

@Entity
public class Track extends AudioItem {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_id")
    private Artist artist;

    public Track() {
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }
}