package in.guardianservice.link.shortner.controller;

import com.guardianservices.kafka.services.KafkaProducerService;
import in.guardianservice.link.shortner.constants.Constant;
import in.guardianservice.link.shortner.repository.EmployeeRepository;
import in.guardianservice.link.shortner.request.DashboardDetailsRequest;
import in.guardianservice.link.shortner.response.BaseResponse;
import in.guardianservice.link.shortner.service.UrlService;
import in.guardianservice.link.shortner.utility.ResponseUtility;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/url-service")
public class UrlController {

    private static final Logger logger = LoggerFactory.getLogger(UrlController.class);

    private final KafkaProducerService producerService;

    @Autowired
    private UrlService urlService;

    public UrlController(KafkaProducerService producerService) {
        this.producerService = producerService;
    }

    @GetMapping("/welcome")
    public String welcome() {
        return "********** Welcome to link shortener **********";
    }

    @PostMapping("/shorten")
    public ResponseEntity<BaseResponse> shortenUrl(@RequestParam String originalUrl, @RequestParam int days, @RequestParam String user) {
        logger.info(Constant.CONTROLLER_STARTED, "shorten");

//        producerService.sendUrlCreatedEvent(originalUrl, days, user);

        urlService.createShortUrl(originalUrl, days, user);
        return new ResponseEntity<>(ResponseUtility.getBaseResponse(HttpStatus.OK, "Your request in in progress"), HttpStatus.OK);
    }

    @GetMapping("/{shortCode}")
    public void redirect(@PathVariable String shortCode, HttpServletResponse response) throws IOException {
        logger.info(Constant.CONTROLLER_STARTED, "shortenCode");
        String longUrl = urlService.getOriginalUrl(shortCode);
        if (longUrl != null) {
            response.sendRedirect(longUrl);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, Constant.NO_LINK_FOUND_BY_SHORT_CODE);
        }
    }

    @PostMapping("/get-dashboard-details")
    public ResponseEntity<BaseResponse> dashboardDetails(@RequestBody DashboardDetailsRequest dashboardDetailsRequest) {
        logger.info(Constant.CONTROLLER_STARTED, "get-dashboard-details");

        return new ResponseEntity<>(urlService.getDashboardDetails(dashboardDetailsRequest), HttpStatus.OK);
    }

}
