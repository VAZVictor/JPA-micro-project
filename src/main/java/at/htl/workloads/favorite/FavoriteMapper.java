package at.htl.workloads.favorite;

import at.htl.model.favorite.FavoriteDTO;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FavoriteMapper {

    public FavoriteDTO toDto(Favorite favorite) {
        var audioItem = favorite.getAudioItem();
        return new FavoriteDTO(
                favorite.getId(),
                favorite.getUser().getId(),
                audioItem.getId(),
                audioItem.getTitle(),
                audioItem.getArtist().getName(),
                favorite.getCreatedAt()
        );
    }
}
