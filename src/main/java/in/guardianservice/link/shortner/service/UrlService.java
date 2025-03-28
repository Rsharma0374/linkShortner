package in.guardianservice.link.shortner.service;

import in.guardianservice.link.shortner.request.DashboardDetailsRequest;
import in.guardianservice.link.shortner.response.BaseResponse;


public interface UrlService {
    BaseResponse createShortUrl(String originalUrl, int days, String user);

    String getOriginalUrl(String shortCode);

    BaseResponse getDashboardDetails(DashboardDetailsRequest dashboardDetailsRequest);
}
