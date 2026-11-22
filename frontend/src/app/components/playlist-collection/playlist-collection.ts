import { Component, input } from '@angular/core';
import { Playlist } from '../../models/music.models';

@Component({
  selector: 'app-playlist-collection',
  templateUrl: './playlist-collection.html',
  styleUrl: './playlist-collection.css',
})
export class PlaylistCollection {
  readonly playlists = input.required<Playlist[]>();
  readonly compact = input(false);
}
