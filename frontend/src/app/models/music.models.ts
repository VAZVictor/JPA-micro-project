export type View = 'overview' | 'tracks' | 'playlists';

export interface Track {
  id: number;
  title: string;
  artist: string;
  album: string;
  duration: string;
  color: string;
  symbol: string;
  favorite: boolean;
}

export interface Playlist {
  id: number;
  name: string;
  description: string;
  trackCount: number;
  color: string;
  symbol: string;
}
