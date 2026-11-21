import { Component, computed, signal } from '@angular/core';
import { PageHeading } from './components/page-heading/page-heading';
import { PlaylistCollection } from './components/playlist-collection/playlist-collection';
import { Sidebar } from './components/sidebar/sidebar';
import { TrackList } from './components/track-list/track-list';
import { OverviewPage } from './pages/overview-page/overview-page';
import { Playlist, Track, View } from './models/music.models';

@Component({
  imports: [PageHeading, PlaylistCollection, Sidebar, TrackList, OverviewPage],
  selector: 'app-root',
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class App {
  protected readonly activeView = signal<View>('overview');
  protected readonly searchTerm = signal('');

  // Simple demo data until the backend exposes REST endpoints.
  protected readonly tracks = signal<Track[]>([
    { id: 1, title: 'Midnight City', artist: 'M83', album: 'Hurry Up, We’re Dreaming', duration: '4:03', color: 'lavender', symbol: '✳', favorite: true },
    { id: 2, title: 'Dreams', artist: 'Fleetwood Mac', album: 'Rumours', duration: '4:17', color: 'peach', symbol: '☼', favorite: true },
    { id: 3, title: 'Glue', artist: 'Bicep', album: 'Bicep', duration: '4:12', color: 'mint', symbol: '◒', favorite: false },
    { id: 4, title: 'Sunset Lover', artist: 'Petit Biscuit', album: 'Presence', duration: '3:57', color: 'butter', symbol: '◉', favorite: false },
    { id: 5, title: 'Redbone', artist: 'Childish Gambino', album: 'Awaken, My Love!', duration: '5:26', color: 'rose', symbol: '✺', favorite: true },
  ]);

  protected readonly playlists = signal<Playlist[]>([
    { id: 1, name: 'Focus Mode', description: 'Ruhig bleiben, weiterdenken.', trackCount: 24, color: 'playlist-green', symbol: '◌' },
    { id: 2, name: 'Late Night Drive', description: 'Für leere Straßen und klare Gedanken.', trackCount: 18, color: 'playlist-blue', symbol: '☾' },
    { id: 3, name: 'Sunday Morning', description: 'Langsam in den Tag starten.', trackCount: 32, color: 'playlist-orange', symbol: '☀' },
  ]);

  protected readonly filteredTracks = computed(() => {
    const term = this.searchTerm().trim().toLocaleLowerCase();
    if (!term) return this.tracks();

    return this.tracks().filter((track) =>
      `${track.title} ${track.artist} ${track.album}`.toLocaleLowerCase().includes(term),
    );
  });

  protected readonly filteredPlaylists = computed(() => {
    const term = this.searchTerm().trim().toLocaleLowerCase();
    if (!term) return this.playlists();

    return this.playlists().filter((playlist) =>
      `${playlist.name} ${playlist.description}`.toLocaleLowerCase().includes(term),
    );
  });

  protected readonly favoriteCount = computed(
    () => this.tracks().filter((track) => track.favorite).length,
  );

  protected readonly viewLabel = computed(() => {
    switch (this.activeView()) {
      case 'tracks': return 'Tracks';
      case 'playlists': return 'Playlists';
      default: return 'Übersicht';
    }
  });

  protected readonly pageTitle = computed(() => {
    switch (this.activeView()) {
      case 'tracks': return 'Alle Tracks';
      case 'playlists': return 'Deine Playlists';
      default: return 'Deine Musik. Auf einen Blick.';
    }
  });

  protected readonly pageDescription = computed(() => {
    switch (this.activeView()) {
      case 'tracks': return 'Ein Platz für jeden Song, den du gerne hörst.';
      case 'playlists': return 'Musik für jede Stimmung und jeden Moment.';
      default: return 'Deine Sammlung, deine Playlists – alles an einem Ort.';
    }
  });

  protected setView(view: View): void {
    this.activeView.set(view);
  }

  protected updateSearch(term: string): void {
    this.searchTerm.set(term);
  }
}
