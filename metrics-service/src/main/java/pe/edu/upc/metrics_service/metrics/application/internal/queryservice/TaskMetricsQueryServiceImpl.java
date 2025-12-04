package pe.edu.upc.metrics_service.metrics.application.internal.queryservice;

import org.springframework.stereotype.Service;
import pe.edu.upc.metrics_service.metrics.application.clients.groups.GroupsServiceClient;
import pe.edu.upc.metrics_service.metrics.application.clients.iam.IamServiceClient;
import pe.edu.upc.metrics_service.metrics.application.clients.tasks.TasksServiceClient;
import pe.edu.upc.metrics_service.metrics.application.clients.tasks.resources.TaskSummaryResource;
import pe.edu.upc.metrics_service.metrics.domain.model.queries.*;
import pe.edu.upc.metrics_service.metrics.domain.services.TaskMetricsQueryService;
import pe.edu.upc.metrics_service.metrics.interfaces.rest.resources.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskMetricsQueryServiceImpl implements TaskMetricsQueryService {

    private final TasksServiceClient tasksServiceClient;
    private final IamServiceClient iamServiceClient;
    private final GroupsServiceClient groupsServiceClient;

    public TaskMetricsQueryServiceImpl(TasksServiceClient tasksServiceClient, IamServiceClient iamServiceClient, GroupsServiceClient groupsServiceClient) {
        this.tasksServiceClient = tasksServiceClient;
        this.iamServiceClient = iamServiceClient;
        this.groupsServiceClient = groupsServiceClient;
    }

    @Override
    public TaskTimePassedResource handle(GetTaskTimePassedQuery query, String authorizationHeader) {
        //List<Task> memberTasks = taskRepository.findByMember_Id(query.memberId());
        List<TaskSummaryResource> memberTasks = tasksServiceClient.fetchTasksByMemberId(query.memberId(), authorizationHeader);

        double avgTimePassed = memberTasks.isEmpty() ? 0 :
            memberTasks.stream().mapToLong(TaskSummaryResource::timePassed)
                    .average().orElse(0);

        return new TaskTimePassedResource(query.memberId(), (long) avgTimePassed);
    }

    @Override
    public AvgCompletionTimeResource handle(GetAvgCompletionTimeQuery query, String authorizationHeader) {
        //var groupOpt = groupQueryService.handle(new GetGroupByLeaderIdQuery(query.leaderId()));
        var groupOpt = groupsServiceClient.fetchGroupByLeaderId(query.leaderId(), authorizationHeader);

        if (groupOpt.isEmpty()) {
            return new AvgCompletionTimeResource(
                    "AVG_COMPLETION_TIME",
                    0,
                    Map.of("completedTasks", 0)
            );
        }
        Long groupId = groupOpt.get().id();

        List<TaskSummaryResource> groupTasks = tasksServiceClient.fetchTasksByGroupId(groupId,authorizationHeader);

        List<TaskSummaryResource> completedTasks = groupTasks.stream()
                .filter(task -> "DONE".equals(task.status()))
                //.filter(task -> task.getStatus() == TaskStatus.DONE)
                .collect(Collectors.toList());

        double avg = completedTasks.stream()
                //.mapToLong(Task::getTimePassed)
                .mapToLong(TaskSummaryResource::timePassed)
                .average()
                .orElse(0);

        return new AvgCompletionTimeResource(
                "AVG_COMPLETION_TIME",
                avg / (1000 * 60 * 60 * 24),
                Map.of("completedTasks", completedTasks.size())
        );
    }

    @Override
    public RescheduledTasksResource handle(GetRescheduledTasksQuery query, String authorizationHeader) {
        //List<Task> groupTasks = taskRepository.findByGroup_Id(query.groupId());
        List<TaskSummaryResource> groupTasks = tasksServiceClient.fetchTasksByGroupId(query.groupId(), authorizationHeader);


        long totalRescheduledTimes = groupTasks.stream()
                .mapToLong(TaskSummaryResource::timesRearranged)
                .sum();

        Map<String, Integer> details = Map.of(
                "total", groupTasks.size(),
                "rescheduled", (int) totalRescheduledTimes
        );

        List<Long> rescheduledMemberIds = groupTasks.stream()
                .filter(task -> task.timesRearranged() > 0
                        && task.memberId() != null)
                .map(TaskSummaryResource::memberId)
                .distinct()
                .collect(Collectors.toList());

        return new RescheduledTasksResource("RESCHEDULED_TASKS", totalRescheduledTimes, details, rescheduledMemberIds);
    }

    @Override
    public TaskDistributionResource handle(GetTaskDistributionQuery query, String authorizationHeader) {
        //List<Task> groupTasks = taskRepository.findByGroup_Id(query.groupId());

        List<TaskSummaryResource> groupTasks = tasksServiceClient.fetchTasksByGroupId(query.groupId(), authorizationHeader);

        // Obtiene tareas por id de miembro
        Map<Long, List<TaskSummaryResource>> tasksByMemberId = groupTasks.stream()
                .filter(task -> task.memberId() != null)
                .collect(Collectors.groupingBy(TaskSummaryResource::memberId));

        // Construye el mapa de detalles
        Map<String, MemberTaskInfo> details = new HashMap<>();

        // Rellena los detalles con nombre y conteo de tareas
        for (Map.Entry<Long, List<TaskSummaryResource>> entry : tasksByMemberId.entrySet()) {
            Long memberId = entry.getKey();
            int taskCount = entry.getValue().size();
            var member = iamServiceClient.fetchUserByMemberId(memberId, authorizationHeader);
            String memberName = "Desconocido";
            if (member.isPresent()) {
                memberName = member.get().name() + " " + member.get().surname();
            }

            details.put(memberId.toString(), new MemberTaskInfo(memberName, taskCount));
        }

        return new TaskDistributionResource("TASK_DISTRIBUTION", groupTasks.size(), details);
    }

    @Override
    public TaskOverviewResource handle(GetTaskOverviewQuery query, String authorizationHeader) {
        List<TaskSummaryResource> groupTasks = tasksServiceClient.fetchTasksByGroupId(query.groupId(), authorizationHeader);

        Map<String, Long> overview = groupTasks.stream()
                .collect(Collectors.groupingBy(TaskSummaryResource::status, Collectors.counting()));

        Map<String, Integer> converted = overview.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().intValue()
                ));

        return new TaskOverviewResource("TASK_OVERVIEW", groupTasks.size(), converted);
    }

    @Override
    public TaskOverviewResource handle(GetTaskOverviewForMemberQuery query, String authorizationHeader) {
        List<TaskSummaryResource> memberTasks = tasksServiceClient.fetchTasksByMemberId(query.memberId(), authorizationHeader);
        Map<String, Long> overview = memberTasks.stream()
                .collect(Collectors.groupingBy(TaskSummaryResource::status, Collectors.counting()));
        Map<String, Integer> converted = overview.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().intValue()
                ));
        return new TaskOverviewResource("TASK_OVERVIEW_MEMBER", memberTasks.size(), converted);
    }

    @Override
    public TaskDistributionResource handle(GetTaskDistributionForMemberQuery query, String authorizationHeader) {
        List<TaskSummaryResource> memberTasks = tasksServiceClient.fetchTasksByMemberId(query.memberId(), authorizationHeader);

        String memberName = "Desconocido";
        var userMember = iamServiceClient.fetchUserByMemberId(query.memberId(), authorizationHeader);
        if (userMember.isPresent()) {
            memberName = userMember.get().name() + " " + userMember.get().surname();
        }

        Map<String, MemberTaskInfo> details = Map.of(
                query.memberId().toString(), new MemberTaskInfo(memberName, memberTasks.size())
        );
        return new TaskDistributionResource("TASK_DISTRIBUTION_MEMBER", memberTasks.size(), details);
    }

    @Override
    public RescheduledTasksResource handle(GetRescheduledTasksForMemberQuery query, String authorizationHeader) {
        List<TaskSummaryResource> memberTasks = tasksServiceClient.fetchTasksByMemberId(query.memberId(), authorizationHeader);
        long totalRescheduledTimes = memberTasks.stream()
                .mapToLong(TaskSummaryResource::timesRearranged)
                .sum();
        Map<String, Integer> details = Map.of(
                "total", memberTasks.size(),
                "rescheduled", (int) totalRescheduledTimes
        );
        List<Long> rescheduledMemberIds = totalRescheduledTimes > 0 ? List.of(query.memberId()) : List.of();
        return new RescheduledTasksResource("RESCHEDULED_TASKS_MEMBER", totalRescheduledTimes, details, rescheduledMemberIds);
    }

    @Override
    public AvgCompletionTimeResource handle(GetAvgCompletionTimeForMemberQuery query, String authorizationHeader) {
        List<TaskSummaryResource> memberTasks = tasksServiceClient.fetchTasksByMemberId(query.memberId(), authorizationHeader);

        List<TaskSummaryResource> completedTasks = memberTasks.stream()
                .filter(task -> "DONE".equals(task.status())) // Cambiado a DONE
                .collect(Collectors.toList());
        double avg = completedTasks.stream()
                .mapToLong(TaskSummaryResource::timePassed)
                .average()
                .orElse(0);
        return new AvgCompletionTimeResource(
                "AVG_COMPLETION_TIME_MEMBER",
                avg / (1000 * 60 * 60 * 24),
                Map.of("completedTasks", completedTasks.size())
        );
    }
}