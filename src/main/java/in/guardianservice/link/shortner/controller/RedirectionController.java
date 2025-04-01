package in.guardianservice.link.shortner.controller;

import in.guardianservice.link.shortner.constants.Constant;
import in.guardianservice.link.shortner.service.UrlService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@Log4j2
public class RedirectionController {

    @Autowired
    private UrlService urlService;

    @GetMapping("/{shortCode}")
    public void redirect(@PathVariable String shortCode, HttpServletResponse response) throws IOException {
        log.info(Constant.CONTROLLER_STARTED, "shortenCode");
        String longUrl = urlService.getOriginalUrl(shortCode);
        if (longUrl != null) {
            response.sendRedirect(longUrl);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, Constant.NO_LINK_FOUND_BY_SHORT_CODE);
        }
    }
}
