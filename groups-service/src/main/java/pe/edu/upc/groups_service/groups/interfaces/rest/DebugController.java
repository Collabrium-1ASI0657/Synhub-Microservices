package pe.edu.upc.groups_service.groups.interfaces.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class DebugController {
  @GetMapping("/debug/headers")
  public Map<String, String> debugHeaders(@RequestHeader Map<String, String> headers) {
    return headers;
  }
  @GetMapping("/me")
  public ResponseEntity<String> getMyData(@RequestHeader("X-User-Id") String userId,
                                          @RequestHeader("X-Username") String username,
                                          @RequestHeader("X-Roles") String roles,
                                          @RequestHeader("Authorization") String authorizationHeader) {
    return ResponseEntity.ok(authorizationHeader);
  }
}