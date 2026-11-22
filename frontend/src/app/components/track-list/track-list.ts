import { Component, input } from '@angular/core';
import { Track } from '../../models/music.models';

@Component({
  selector: 'app-track-list',
  templateUrl: './track-list.html',
  styleUrl: './track-list.css',
})
export class TrackList {
  readonly tracks = input.required<Track[]>();
}
