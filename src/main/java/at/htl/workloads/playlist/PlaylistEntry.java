package at.htl.workloads.playlist;

import jakarta.persistence.Entity;

@Entity
public class PlaylistEntry {

    // TODO (you): map this composite key with your chosen JPA key annotations
    private PlaylistEntryId id;

    public PlaylistEntryId getId() {
        return id;
    }

    public void setId(PlaylistEntryId id) {
        this.id = id;
    }
}
