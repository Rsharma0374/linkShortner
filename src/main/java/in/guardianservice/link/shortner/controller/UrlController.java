package in.guardianservice.link.shortner.controller;

import in.guardianservice.link.shortner.constants.Constant;
import in.guardianservice.link.shortner.model.UrlShortener;
import in.guardianservice.link.shortner.response.BaseResponse;
import in.guardianservice.link.shortner.service.UrlService;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;

@RestController
public class UrlController {

    private static final Logger logger = LoggerFactory.getLogger(UrlController.class);

    @Autowired
    private UrlService urlService;

    @GetMapping("/welcome")
    public String welcome() {
        return "********** Welcome to link shortner **********";
    }

    @PostMapping("/shorten")
    public ResponseEntity<BaseResponse> shortenUrl(@RequestParam String originalUrl, @RequestParam int days) {
        logger.info(Constant.CONTROLLER_STARTED, "shorten");
        return new ResponseEntity<>(urlService.createShortUrl(originalUrl, days), HttpStatus.OK);
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
}
