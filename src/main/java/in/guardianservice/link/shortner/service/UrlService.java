package in.guardianservice.link.shortner.service;

import in.guardianservice.link.shortner.response.BaseResponse;


public interface UrlService {
    BaseResponse createShortUrl(String originalUrl, int days);

    String getOriginalUrl(String shortCode);
}
