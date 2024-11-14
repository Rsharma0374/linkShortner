package in.guardianservice.link.shortner.service;

import in.guardianservice.link.shortner.model.UrlShortener;
import in.guardianservice.link.shortner.response.BaseResponse;

import java.util.Optional;

public interface UrlService {
    BaseResponse createShortUrl(String originalUrl, int days);

    String getOriginalUrl(String shortCode);
}
