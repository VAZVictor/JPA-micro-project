package at.htl.workloads.playlist;

import at.htl.workloads.audioItem.AudioItem;
import jakarta.persistence.*;

@Entity
@Table(name = "playlist_entry")
public class PlaylistEntry {

    @EmbeddedId
    private PlaylistEntryId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("playlistId")
    @JoinColumn(name = "playlist_id")
    private Playlist playlist;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("audioItemId")
    @JoinColumn(name = "audio_item_id")
    private AudioItem audioItem;

    private int position;

    public PlaylistEntry() {
    }

    public PlaylistEntry(Playlist playlist, AudioItem audioItem, int position) {
        this.playlist = playlist;
        this.audioItem = audioItem;
        this.position = position;
        this.id = new PlaylistEntryId(playlist.getId(), audioItem.getId());
    }

    public PlaylistEntryId getId() {
        return id;
    }

    public void setId(PlaylistEntryId id) {
        this.id = id;
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }

    public AudioItem getAudioItem() {
        return audioItem;
    }

    public void setAudioItem(AudioItem audioItem) {
        this.audioItem = audioItem;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlaylistEntry other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return PlaylistEntry.class.hashCode();
    }
}