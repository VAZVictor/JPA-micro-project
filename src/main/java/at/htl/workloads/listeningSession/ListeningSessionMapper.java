package at.htl.workloads.listeningSession;

import at.htl.model.listeningSession.ListeningSessionDTO;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ListeningSessionMapper {

    public ListeningSessionDTO toDto(ListeningSession session) {
        return new ListeningSessionDTO(
                session.getId(),
                session.getUser().getId(),
                session.getDevice().getId(),
                session.getDevice().getName(),
                session.getAudioItem().getId(),
                session.getAudioItem().getTitle(),
                session.getStartedAt(),
                session.getListenedSeconds()
        );
    }
}
