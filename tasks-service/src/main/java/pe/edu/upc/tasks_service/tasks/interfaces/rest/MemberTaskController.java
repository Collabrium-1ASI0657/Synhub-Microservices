package pe.edu.upc.tasks_service.tasks.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.tasks_service.tasks.application.clients.iam.IamServiceClient;
import pe.edu.upc.tasks_service.tasks.domain.model.commands.CreateTaskCommand;
import pe.edu.upc.tasks_service.tasks.domain.model.queries.GetAllTasksByMemberId;
import pe.edu.upc.tasks_service.tasks.domain.services.TaskCommandService;
import pe.edu.upc.tasks_service.tasks.domain.services.TaskQueryService;
import pe.edu.upc.tasks_service.tasks.interfaces.rest.resources.CreateTaskResource;
import pe.edu.upc.tasks_service.tasks.interfaces.rest.resources.TaskResource;
import pe.edu.upc.tasks_service.tasks.interfaces.rest.transform.CreateTaskCommandFromResourceAssembler;
import pe.edu.upc.tasks_service.tasks.interfaces.rest.transform.TaskResourceFromEntityAssembler;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/members")
@Tag(name = "Tasks Member ", description = "Tasks Member endpoints")
@CrossOrigin(origins = "*", methods = { RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE })
public class MemberTaskController {
  private final TaskCommandService taskCommandService;
  private final TaskQueryService taskQueryService;
  private final IamServiceClient iamServiceClient;

  public MemberTaskController(TaskCommandService taskCommandService,
                              TaskQueryService taskQueryService,
                              IamServiceClient iamServiceClient) {
    this.taskCommandService = taskCommandService;
    this.taskQueryService = taskQueryService;
    this.iamServiceClient = iamServiceClient;
  }

  @PostMapping("/{memberId}/tasks")
  @Operation(summary = "Create a new task", description = "Creates a new task")
  public ResponseEntity<TaskResource> createTask(@PathVariable Long memberId,
                                                 @RequestBody CreateTaskResource resource,
                                                 @RequestHeader("Authorization") String authorizationHeader) {
    var createTaskCommand = CreateTaskCommandFromResourceAssembler.toCommandFromResource(resource, memberId);
    var task = taskCommandService.handle(createTaskCommand);
    if(task.isEmpty()) return ResponseEntity.badRequest().build();

    var userResource = iamServiceClient.fetchUserByMemberId(memberId, authorizationHeader);
    if (userResource.isEmpty()) return ResponseEntity.notFound().build();

    var taskResource = TaskResourceFromEntityAssembler.toResourceFromEntity(task.get(), userResource.get());

    return new ResponseEntity<>(taskResource, HttpStatus.CREATED);
  }

  @GetMapping("/{memberId}/tasks")
  @Operation(summary = "Get all tasks by member id", description = "Get all tasks by member id")
  public ResponseEntity<List<TaskResource>> getAllTasksByMemberId(@PathVariable Long memberId,
                                                                  @RequestHeader("Authorization") String authorizationHeader) {
    // Obtener tareas del servicio de consulta
    var getAllTasksByMemberId = new GetAllTasksByMemberId(memberId);
    var tasks = taskQueryService.handle(getAllTasksByMemberId);
    if (tasks.isEmpty()) return ResponseEntity.ok(List.of());

    // Obtener usuario desde IAM (necesario para construir TaskResource)
    var userResource = iamServiceClient.fetchUserByMemberId(memberId, authorizationHeader);
    if (userResource.isEmpty()) return ResponseEntity.notFound().build();

    // Convertir cada entidad Task a TaskResource
    var taskResources = tasks.stream()
        .map(task -> TaskResourceFromEntityAssembler.toResourceFromEntity(task, userResource.get()))
        .toList();
    return ResponseEntity.ok(taskResources);
  }

}
