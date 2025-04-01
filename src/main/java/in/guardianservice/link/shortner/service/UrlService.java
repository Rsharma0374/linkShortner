package in.guardianservice.link.shortner.service;

import in.guardianservice.link.shortner.request.DashboardDetailsRequest;
import in.guardianservice.link.shortner.request.UrlRequest;
import in.guardianservice.link.shortner.response.BaseResponse;


public interface UrlService {
    BaseResponse saveNewEntry(UrlRequest urlRequest);

    String getOriginalUrl(String shortCode);

    BaseResponse getDashboardDetails(DashboardDetailsRequest dashboardDetailsRequest);

    BaseResponse deleteEntry(UrlRequest urlRequest);

    BaseResponse updateEntry(UrlRequest urlRequest);
}
