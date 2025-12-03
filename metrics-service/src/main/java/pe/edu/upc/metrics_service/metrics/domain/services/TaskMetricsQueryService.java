package pe.edu.upc.metrics_service.metrics.domain.services;

import pe.edu.upc.metrics_service.metrics.domain.model.queries.*;
import pe.edu.upc.metrics_service.metrics.interfaces.rest.resources.*;

public interface TaskMetricsQueryService {
    TaskTimePassedResource handle(GetTaskTimePassedQuery query);
    AvgCompletionTimeResource handle(GetAvgCompletionTimeQuery query);
    RescheduledTasksResource handle(GetRescheduledTasksQuery query);
    TaskDistributionResource handle(GetTaskDistributionQuery query, String authenticationHeader);
    TaskOverviewResource handle(GetTaskOverviewQuery query);
    TaskOverviewResource handle(GetTaskOverviewForMemberQuery query);
    TaskDistributionResource handle(GetTaskDistributionForMemberQuery query);
    RescheduledTasksResource handle(GetRescheduledTasksForMemberQuery query);
    AvgCompletionTimeResource handle(GetAvgCompletionTimeForMemberQuery query);
}