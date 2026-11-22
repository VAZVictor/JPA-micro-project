import { Component, input, output } from '@angular/core';
import { Playlist, Track, View } from '../../models/music.models';
import { PlaylistCollection } from '../../components/playlist-collection/playlist-collection';
import { TrackList } from '../../components/track-list/track-list';

@Component({
  selector: 'app-overview-page',
  imports: [PlaylistCollection, TrackList],
  templateUrl: './overview-page.html',
  styleUrl: './overview-page.css',
})
export class OverviewPage {
  readonly tracks = input.required<Track[]>();
  readonly playlists = input.required<Playlist[]>();
  readonly totalTrackCount = input.required<number>();
  readonly totalPlaylistCount = input.required<number>();
  readonly favoriteCount = input.required<number>();
  readonly navigate = output<View>();
}
