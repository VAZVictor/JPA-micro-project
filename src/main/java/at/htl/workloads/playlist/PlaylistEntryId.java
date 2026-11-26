package at.htl.workloads.playlist;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class PlaylistEntryId implements Serializable {

    @Column(name = "playlist_id")
    private Long playlistId;

    @Column(name = "audio_item_id")
    private Long audioItemId;

    public PlaylistEntryId() {
    }

    public PlaylistEntryId(Long playlistId, Long audioItemId) {
        this.playlistId = playlistId;
        this.audioItemId = audioItemId;
    }

    public Long getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(Long playlistId) {
        this.playlistId = playlistId;
    }

    public Long getAudioItemId() {
        return audioItemId;
    }

    public void setAudioItemId(Long audioItemId) {
        this.audioItemId = audioItemId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlaylistEntryId other)) return false;
        return Objects.equals(playlistId, other.playlistId)
                && Objects.equals(audioItemId, other.audioItemId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playlistId, audioItemId);
    }
}