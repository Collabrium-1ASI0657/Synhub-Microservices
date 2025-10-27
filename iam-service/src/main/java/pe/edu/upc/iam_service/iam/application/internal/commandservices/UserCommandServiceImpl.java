package pe.edu.upc.iam_service.iam.application.internal.commandservices;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import pe.edu.upc.iam_service.iam.application.internal.outboundservices.events.LeaderPublisher;
import pe.edu.upc.iam_service.iam.application.internal.outboundservices.hashing.HashingService;
import pe.edu.upc.iam_service.iam.application.internal.outboundservices.tokens.TokenService;
import pe.edu.upc.iam_service.iam.domain.model.aggregates.User;
import pe.edu.upc.iam_service.iam.domain.model.commands.CreateUserLeaderCommand;
import pe.edu.upc.iam_service.iam.domain.model.commands.CreateUserMemberCommand;
import pe.edu.upc.iam_service.iam.domain.model.commands.SignInCommand;
import pe.edu.upc.iam_service.iam.domain.model.commands.SignUpCommand;
import pe.edu.upc.iam_service.iam.domain.services.UserCommandService;
import pe.edu.upc.iam_service.iam.infrastructure.authorization.sfs.model.UserDetailsImpl;
import pe.edu.upc.iam_service.iam.infrastructure.persistence.jpa.repositories.RoleRepository;
import pe.edu.upc.iam_service.iam.infrastructure.persistence.jpa.repositories.UserRepository;

import java.util.Optional;

/**
 * User command service implementation
 * <p>
 *     This class implements the {@link UserCommandService} interface and provides the implementation for the
 *     {@link SignInCommand} and {@link SignUpCommand} commands.
 * </p>
 */
@Service
public class UserCommandServiceImpl implements UserCommandService {
  private final RoleRepository roleRepository;
  private final UserRepository userRepository;
  private final HashingService hashingService;
  private final TokenService tokenService;
  private final LeaderPublisher leaderPublisher;

  public UserCommandServiceImpl(RoleRepository roleRepository,
                                UserRepository userRepository,
                                HashingService hashingService,
                                TokenService tokenService,
                                LeaderPublisher leaderPublisher) {
    this.roleRepository = roleRepository;
    this.userRepository = userRepository;
    this.hashingService = hashingService;
    this.tokenService = tokenService;
    this.leaderPublisher = leaderPublisher;
  }

  /**
   * Handle the sign-in command
   * <p>
   *     This method handles the {@link SignInCommand} command and returns the user and the token.
   * </p>
   * @param command the sign-in command containing the username and password
   * @return and optional containing the user matching the username and the generated token
   * @throws RuntimeException if the user is not found or the password is invalid
   */
  @Override
  public Optional<ImmutablePair<User, String>> handle(SignInCommand command) {
    var user = userRepository.findByUsername(command.username());
    if (user.isEmpty())
      throw new RuntimeException("User not found");
    if (!hashingService.matches(command.password(), user.get().getPassword()))
      throw new RuntimeException("Invalid password");

    // 1. Crear el objeto UserDetails
    UserDetailsImpl userDetails = UserDetailsImpl.build(user.get());

    // 2. Crear el objeto Authentication. Esto simula el login exitoso.
    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
        userDetails,
        null,
        userDetails.getAuthorities()
    );

    // 3. Generar el token usando el objeto Authentication (ahora con roles)
    var token = tokenService.generateToken(authentication);

    return Optional.of(ImmutablePair.of(user.get(), token));
  }

  /**
   * Handle the sign-up command
   * <p>
   *     This method handles the {@link SignUpCommand} command and returns the user.
   * </p>
   * @param command the sign-up command containing the username and password
   * @return the created user
   */
  @Override
  public Optional<User> handle(SignUpCommand command) {
    if (userRepository.existsByUsername(command.username()))
      throw new RuntimeException("Username already exists");
    if(userRepository.existsByEmail(command.username()))
      throw new RuntimeException("User with this email already exists");
    var roles = command.roles().stream()
        .map(role ->
            roleRepository.findByName(role.getName())
                .orElseThrow(() -> new RuntimeException("Role name not found")))
        .toList();
    var user = new User(
        command.username(),
        command.name(),
        command.surname(),
        command.imgUrl(),
        command.email(),
        hashingService.encode(command.password()),
        roles);
    userRepository.save(user);
    return userRepository.findByUsername(command.username());
  }

  @Override
  public Optional<User> handle(CreateUserLeaderCommand command) {
    var userId = command.userId();
    if(userRepository.findById(userId).isEmpty()){
      throw new RuntimeException("User not found");
    }
    var user = userRepository.findById(userId);
    try {
      leaderPublisher.publishLeaderCreated();
    } catch (Exception e) {
      throw new RuntimeException("Error while creating leader: " + e.getMessage());
    }

    return user;
  }

  @Override
  public Optional<User> handle(CreateUserMemberCommand command) {
    return Optional.empty();
  }
}