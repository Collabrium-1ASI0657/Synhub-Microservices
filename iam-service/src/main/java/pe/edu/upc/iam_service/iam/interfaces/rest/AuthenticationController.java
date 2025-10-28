package pe.edu.upc.iam_service.iam.interfaces.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.upc.iam_service.iam.domain.model.commands.CreateUserLeaderCommand;
import pe.edu.upc.iam_service.iam.domain.model.queries.GetUserByIdQuery;
import pe.edu.upc.iam_service.iam.domain.services.UserCommandService;
import pe.edu.upc.iam_service.iam.domain.services.UserQueryService;
import pe.edu.upc.iam_service.iam.interfaces.rest.resources.AuthenticatedUserResource;
import pe.edu.upc.iam_service.iam.interfaces.rest.resources.SignInResource;
import pe.edu.upc.iam_service.iam.interfaces.rest.resources.SignUpResource;
import pe.edu.upc.iam_service.iam.interfaces.rest.resources.UserResource;
import pe.edu.upc.iam_service.iam.interfaces.rest.transform.*;

/**
 * AuthenticationController
 * <p>
 *     This controller is responsible for handling authentication requests.
 *     It exposes two endpoints:
 *     <ul>
 *         <li>POST /api/v1/auth/sign-in</li>
 *         <li>POST /api/v1/auth/sign-up</li>
 *     </ul>
 * </p>
 */
@RestController
@RequestMapping(value = "/api/v1/authentication", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Authentication", description = "Authentication Endpoints")
public class AuthenticationController {
  private final UserQueryService userQueryService;
  private final UserCommandService userCommandService;

  public AuthenticationController(UserCommandService userCommandService,
                                  UserQueryService userQueryService) {
    this.userCommandService = userCommandService;
    this.userQueryService = userQueryService;
  }

  /**
   * Handles the sign-in request.
   * @param signInResource the sign-in request body.
   * @return the authenticated user resource.
   */
  @PostMapping("/sign-in")
  public ResponseEntity<AuthenticatedUserResource> signIn(
      @RequestBody SignInResource signInResource) {

    var signInCommand = SignInCommandFromResourceAssembler
        .toCommandFromResource(signInResource);
    var authenticatedUser = userCommandService.handle(signInCommand);
    if (authenticatedUser.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    var authenticatedUserResource = AuthenticatedUserResourceFromEntityAssembler
        .toResourceFromEntity(
            authenticatedUser.get().getLeft(), authenticatedUser.get().getRight());
    return ResponseEntity.ok(authenticatedUserResource);
  }

  /**
   * Handles the sign-up request.
   * @param signUpResource the sign-up request body.
   * @return the created user resource.
   */
  @PostMapping("/sign-up")
  public ResponseEntity<UserResource> signUp(@RequestBody SignUpResource signUpResource) {
    var signUpCommand = SignUpCommandFromResourceAssembler
        .toCommandFromResource(signUpResource);
    var user = userCommandService.handle(signUpCommand);
    if (user.isEmpty()) {
      return ResponseEntity.badRequest().build();
    }

    var role = user.get().getRoles().stream().findFirst().get().getName().toString();

    if(role.equals("ROLE_LEADER")){
      var createUserLeaderCommand = CreateUserLeaderCommandFromResourceAssembler.toCommandFromResource(user.get().getId());
      user = userCommandService.handle(createUserLeaderCommand);
      if (user.isEmpty()) {
        return ResponseEntity.badRequest().build();
      }
      System.out.println("Soy un LEADER");
    }

    if(role.equals("ROLE_MEMBER")){
      System.out.println("Soy un MEMBER");
    }

    var userResource = UserResourceFromEntityAssembler.toResourceFromEntity(user.get());
    return new ResponseEntity<>(userResource, HttpStatus.CREATED);
  }
}