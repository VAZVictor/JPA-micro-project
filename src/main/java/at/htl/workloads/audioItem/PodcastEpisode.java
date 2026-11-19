package at.htl.workloads.audioItem;

import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;

@Entity

@Inheritance(strategy = InheritanceType.JOINED)
public class PodcastEpisode extends AudioItem {
}
