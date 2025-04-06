package in.guardianservice.link.shortner.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "AUTH-SERVICE")
public interface AuthService {
    @GetMapping("/auth/get-email-by-username/{sUsername}/{sProductName}")
    String getEmailByUsername(@PathVariable("sUsername") String username,
                                          @PathVariable("sProductName") String productName);
}
