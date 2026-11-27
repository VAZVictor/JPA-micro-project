package at.htl.workloads.listeningSession;

import java.util.List;

public interface ListeningSessionRepository {

    List<ListeningSession> findByUser(Long userId);

    void add(ListeningSession session);
}
