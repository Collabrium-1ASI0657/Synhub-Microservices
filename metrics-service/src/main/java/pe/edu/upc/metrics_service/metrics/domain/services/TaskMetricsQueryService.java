package pe.edu.upc.metrics_service.metrics.domain.services;

import pe.edu.upc.metrics_service.metrics.domain.model.queries.*;
import pe.edu.upc.metrics_service.metrics.interfaces.rest.resources.*;

public interface TaskMetricsQueryService {
    TaskTimePassedResource handle(GetTaskTimePassedQuery query, String authenticationHeader);
    AvgCompletionTimeResource handle(GetAvgCompletionTimeQuery query, String authenticationHeader);
    RescheduledTasksResource handle(GetRescheduledTasksQuery query, String authenticationHeader);
    TaskDistributionResource handle(GetTaskDistributionQuery query, String authenticationHeader);
    TaskOverviewResource handle(GetTaskOverviewQuery query, String authenticationHeader);
    TaskOverviewResource handle(GetTaskOverviewForMemberQuery query, String authenticationHeader);
    TaskDistributionResource handle(GetTaskDistributionForMemberQuery query, String authenticationHeader);
    RescheduledTasksResource handle(GetRescheduledTasksForMemberQuery query, String authenticationHeader);
    AvgCompletionTimeResource handle(GetAvgCompletionTimeForMemberQuery query, String authenticationHeader);
}