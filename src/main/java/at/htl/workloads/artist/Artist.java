package at.htl.workloads.artist;

import at.htl.workloads.audioItem.Track;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Artist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @OneToMany(mappedBy = "artist", fetch = FetchType.LAZY)
    private List<Track> tracks = new ArrayList<>();
}
