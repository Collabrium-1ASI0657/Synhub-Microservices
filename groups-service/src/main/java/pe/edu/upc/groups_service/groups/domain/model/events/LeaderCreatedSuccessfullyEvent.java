package pe.edu.upc.groups_service.groups.domain.model.events;

import java.sql.Time;

public record LeaderCreatedSuccessfullyEvent(Long userId,
                                             Long leaderId,
                                             Time averageSolutionTime,
                                             Integer solvedRequests) {
}
