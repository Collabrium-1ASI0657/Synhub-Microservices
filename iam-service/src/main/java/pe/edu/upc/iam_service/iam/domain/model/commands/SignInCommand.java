package pe.edu.upc.iam_service.iam.domain.model.commands;

public record SignInCommand(
    String username,
    String password) {
}
