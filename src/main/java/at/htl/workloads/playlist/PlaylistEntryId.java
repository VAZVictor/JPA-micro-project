package at.htl.workloads.playlist;

import java.io.Serializable;
import java.util.Objects;

// TODO (you): add the annotation required by your chosen composite-key mapping
public class PlaylistEntryId implements Serializable {

    private Long playlistId;
    private Integer position;

    public PlaylistEntryId() {
    }

    public PlaylistEntryId(Long playlistId, Integer position) {
        this.playlistId = playlistId;
        this.position = position;
    }

    public Long getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(Long playlistId) {
        this.playlistId = playlistId;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PlaylistEntryId that)) {
            return false;
        }
        return Objects.equals(playlistId, that.playlistId)
                && Objects.equals(position, that.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playlistId, position);
    }
}
