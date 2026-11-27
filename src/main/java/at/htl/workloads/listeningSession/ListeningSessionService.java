package at.htl.workloads.listeningSession;

import at.htl.model.listeningSession.CreateListeningSessionRequest;
import at.htl.model.listeningSession.ListeningSessionDTO;

import java.util.List;

public interface ListeningSessionService {

    ListeningSessionDTO recordSession(CreateListeningSessionRequest request);

    List<ListeningSessionDTO> getSessions(Long userId);
}
