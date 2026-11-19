# JPA-micro-project
Contributors: Victor Ehrenmüller-Jensen, Elena Mayer, Annika Neuhauser, Paul Stolz


# SoundVault – Musik-Bibliothek & Playlist-Manager

Selbstgehostete Musikverwaltung mit JPA-Backend und generiertem Frontend.  
Verwalte Tracks, kuratiere Playlists und entdecke deine Hörgewohnheiten – ohne Streaming, nur Metadaten.

## Ziel (3-Wochen-Sprint)
- Vollständiges CRUD für Tracks, Artists, Genres, Playlists
- Generierte, sofort nutzbare UI aus OpenAPI-Spec
- Saubere JPA-Architektur mit Best Practices
- Optional: Smartwatch-Sync für "Now Playing"

## Tech Stack
- **Backend:** Spring Boot 3, Spring Data JPA, Flyway, MapStruct
- **DB:** PostgreSQL 16
- **API:** SpringDoc OpenAPI 3
- **Frontend:** [Euer Generator-Tool einfügen]
- **Testing:** Testcontainers, JUnit 5, AssertJ

##  Quick Start
```bash
# Datenbank starten
docker compose up -d postgres

# Backend starten
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev

# OpenAPI-Spec abrufen (für Frontend-Generator)
curl http://localhost:8080/v3/api-docs.yaml -o openapi.yaml

# Frontend gemäß Generator-Doku erzeugen und starten