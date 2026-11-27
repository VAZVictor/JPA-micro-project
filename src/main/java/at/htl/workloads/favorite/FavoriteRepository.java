package at.htl.workloads.favorite;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository {

    List<Favorite> findByUser(Long userId);

    Optional<Favorite> findByUserAndAudioItem(Long userId, Long audioItemId);

    void add(Favorite favorite);

    void remove(Favorite favorite);
}
