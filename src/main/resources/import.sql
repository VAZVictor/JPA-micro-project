insert into soundvault.artist (id, name, country) values (1, 'Bilderbuch', 'Austria');
insert into soundvault.artist (id, name, country) values (2, 'Wanda', 'Austria');
insert into soundvault.artist (id, name, country) values (3, 'Daft Punk', 'France');
insert into soundvault.artist (id, name, country) values (4, 'Lena Hofer', 'Austria');
insert into soundvault.artist (id, name, country) values (5, 'Leere Band', 'Germany');

insert into soundvault.album (id, title, release_year, artist_id) values (1, 'Schick Schock', 2015, 1);
insert into soundvault.album (id, title, release_year, artist_id) values (2, 'Amore', 2014, 2);
insert into soundvault.album (id, title, release_year, artist_id) values (3, 'Random Access Memories', 2013, 3);

insert into soundvault.genre (id, name) values (1, 'Pop');
insert into soundvault.genre (id, name) values (2, 'Rock');
insert into soundvault.genre (id, name) values (3, 'Electronic');
insert into soundvault.genre (id, name) values (4, 'Austropop');
insert into soundvault.genre (id, name) values (5, 'Talk');

insert into soundvault.audio_item (id, title, duration_seconds, artist_id) values (1, 'Bungalow', 227, 1);
insert into soundvault.audio_item (id, title, duration_seconds, artist_id) values (2, 'Maschin', 236, 1);
insert into soundvault.audio_item (id, title, duration_seconds, artist_id) values (3, 'Bologna', 205, 2);
insert into soundvault.audio_item (id, title, duration_seconds, artist_id) values (4, 'Luzia', 224, 2);
insert into soundvault.audio_item (id, title, duration_seconds, artist_id) values (5, 'Get Lucky', 369, 3);
insert into soundvault.audio_item (id, title, duration_seconds, artist_id) values (6, 'Instant Crush', 337, 3);
insert into soundvault.audio_item (id, title, duration_seconds, artist_id) values (7, 'One More Time', 320, 3);
insert into soundvault.audio_item (id, title, duration_seconds, artist_id) values (8, 'Warum JPA?', 1800, 4);
insert into soundvault.audio_item (id, title, duration_seconds, artist_id) values (9, 'Joins verstehen', 2100, 4);

insert into soundvault.track (id, album_id, track_number) values (1, 1, 3);
insert into soundvault.track (id, album_id, track_number) values (2, 1, 1);
insert into soundvault.track (id, album_id, track_number) values (3, 2, 1);
insert into soundvault.track (id, album_id, track_number) values (4, 2, 2);
insert into soundvault.track (id, album_id, track_number) values (5, 3, 8);
insert into soundvault.track (id, album_id, track_number) values (6, 3, 5);
insert into soundvault.track (id, album_id, track_number) values (7, null, null);

insert into soundvault.podcast_episode (id, show_name, episode_number, published_at)
values (8, 'Datenbank-Talk', 1, DATE '2026-10-01');
insert into soundvault.podcast_episode (id, show_name, episode_number, published_at)
values (9, 'Datenbank-Talk', 2, DATE '2026-10-15');

insert into soundvault.audio_item_genre (audio_item_id, genre_id) values (1, 1);
insert into soundvault.audio_item_genre (audio_item_id, genre_id) values (1, 4);
insert into soundvault.audio_item_genre (audio_item_id, genre_id) values (2, 4);
insert into soundvault.audio_item_genre (audio_item_id, genre_id) values (3, 2);
insert into soundvault.audio_item_genre (audio_item_id, genre_id) values (3, 4);
insert into soundvault.audio_item_genre (audio_item_id, genre_id) values (4, 4);
insert into soundvault.audio_item_genre (audio_item_id, genre_id) values (5, 3);
insert into soundvault.audio_item_genre (audio_item_id, genre_id) values (6, 3);
insert into soundvault.audio_item_genre (audio_item_id, genre_id) values (7, 3);
insert into soundvault.audio_item_genre (audio_item_id, genre_id) values (8, 5);
insert into soundvault.audio_item_genre (audio_item_id, genre_id) values (9, 5);

insert into soundvault.app_user (id, username, email, created_at)
values (1, 'elena', 'elena@soundvault.at', TIMESTAMP '2026-10-01 10:00:00');
insert into soundvault.app_user (id, username, email, created_at)
values (2, 'victor', 'victor@soundvault.at', TIMESTAMP '2026-10-01 10:05:00');
insert into soundvault.app_user (id, username, email, created_at)
values (3, 'annika', 'annika@soundvault.at', TIMESTAMP '2026-10-02 09:00:00');

insert into soundvault.device (id, name, type, user_id) values (1, 'Elenas Handy', 'PHONE', 1);
insert into soundvault.device (id, name, type, user_id) values (2, 'Elenas Smartwatch', 'WATCH', 1);
insert into soundvault.device (id, name, type, user_id) values (3, 'Victors Laptop', 'DESKTOP', 2);

insert into soundvault.listening_session (id, started_at, listened_seconds, user_id, device_id, audio_item_id)
values (1, TIMESTAMP '2026-11-01 08:00:00', 227, 1, 1, 1);
insert into soundvault.listening_session (id, started_at, listened_seconds, user_id, device_id, audio_item_id)
values (2, TIMESTAMP '2026-11-01 08:05:00', 236, 1, 1, 2);
insert into soundvault.listening_session (id, started_at, listened_seconds, user_id, device_id, audio_item_id)
values (3, TIMESTAMP '2026-11-02 07:30:00', 200, 1, 2, 1);
insert into soundvault.listening_session (id, started_at, listened_seconds, user_id, device_id, audio_item_id)
values (4, TIMESTAMP '2026-11-03 18:00:00', 205, 2, 3, 3);
insert into soundvault.listening_session (id, started_at, listened_seconds, user_id, device_id, audio_item_id)
values (5, TIMESTAMP '2026-11-03 18:10:00', 369, 2, 3, 5);
insert into soundvault.listening_session (id, started_at, listened_seconds, user_id, device_id, audio_item_id)
values (6, TIMESTAMP '2026-11-04 20:00:00', 120, 2, 3, 6);
insert into soundvault.listening_session (id, started_at, listened_seconds, user_id, device_id, audio_item_id)
values (7, TIMESTAMP '2026-11-05 12:00:00', 1800, 1, 1, 8);

insert into soundvault.playlist (id, name, description, created_at, user_id)
values (1, 'Morgenroutine', 'Zum Aufwachen', TIMESTAMP '2026-10-10 07:00:00', 1);
insert into soundvault.playlist (id, name, description, created_at, user_id)
values (2, 'Fokus', 'Zum Lernen', TIMESTAMP '2026-10-11 15:00:00', 2);
insert into soundvault.playlist (id, name, description, created_at, user_id)
values (3, 'Leer', null, TIMESTAMP '2026-10-12 12:00:00', 3);

insert into soundvault.playlist_entry (playlist_id, position, audio_item_id, added_at)
values (1, 1, 1, TIMESTAMP '2026-10-10 07:01:00');
insert into soundvault.playlist_entry (playlist_id, position, audio_item_id, added_at)
values (1, 2, 3, TIMESTAMP '2026-10-10 07:02:00');
insert into soundvault.playlist_entry (playlist_id, position, audio_item_id, added_at)
values (1, 3, 4, TIMESTAMP '2026-10-10 07:03:00');
insert into soundvault.playlist_entry (playlist_id, position, audio_item_id, added_at)
values (2, 1, 5, TIMESTAMP '2026-10-11 15:01:00');
insert into soundvault.playlist_entry (playlist_id, position, audio_item_id, added_at)
values (2, 2, 6, TIMESTAMP '2026-10-11 15:02:00');
insert into soundvault.playlist_entry (playlist_id, position, audio_item_id, added_at)
values (2, 3, 5, TIMESTAMP '2026-10-11 15:03:00');

insert into soundvault.favorite (id, user_id, audio_item_id, created_at)
values (1, 1, 1, TIMESTAMP '2026-11-01 09:00:00');
insert into soundvault.favorite (id, user_id, audio_item_id, created_at)
values (2, 1, 8, TIMESTAMP '2026-11-05 13:00:00');
insert into soundvault.favorite (id, user_id, audio_item_id, created_at)
values (3, 2, 5, TIMESTAMP '2026-11-03 19:00:00');

select setval(pg_get_serial_sequence('soundvault.artist', 'id'), (select max(id) from soundvault.artist));
select setval(pg_get_serial_sequence('soundvault.album', 'id'), (select max(id) from soundvault.album));
select setval(pg_get_serial_sequence('soundvault.genre', 'id'), (select max(id) from soundvault.genre));
select setval(pg_get_serial_sequence('soundvault.audio_item', 'id'), (select max(id) from soundvault.audio_item));
select setval(pg_get_serial_sequence('soundvault.app_user', 'id'), (select max(id) from soundvault.app_user));
select setval(pg_get_serial_sequence('soundvault.device', 'id'), (select max(id) from soundvault.device));
select setval(pg_get_serial_sequence('soundvault.listening_session', 'id'), (select max(id) from soundvault.listening_session));
select setval(pg_get_serial_sequence('soundvault.playlist', 'id'), (select max(id) from soundvault.playlist));
select setval(pg_get_serial_sequence('soundvault.favorite', 'id'), (select max(id) from soundvault.favorite));
