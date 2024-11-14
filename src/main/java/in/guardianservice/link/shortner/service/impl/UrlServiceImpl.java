package in.guardianservice.link.shortner.service.impl;

import in.guardianservice.link.shortner.constants.Constant;
import in.guardianservice.link.shortner.model.UrlShortener;
import in.guardianservice.link.shortner.repository.MongoService;
import in.guardianservice.link.shortner.response.BaseResponse;
import in.guardianservice.link.shortner.response.Error;
import in.guardianservice.link.shortner.service.UrlService;
import in.guardianservice.link.shortner.utility.ResponseUtility;
import in.guardianservice.link.shortner.utility.ShortcodeGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

@Service
public class UrlServiceImpl implements UrlService {

    private static final Logger logger = LoggerFactory.getLogger(UrlServiceImpl.class);
    private static final int DAY_IN_MILLIS = 24 * 60 * 60 * 1000;

    @Autowired
    private MongoService mongoService;

    @Override
    public BaseResponse createShortUrl(String originalUrl, int days) {
        logger.info("Inside createShortUrl method");
        Collection<Error> errors = new ArrayList<>();
        BaseResponse baseResponse = new BaseResponse();
        try {
            if (validateUrl(originalUrl, errors)) return ResponseUtility.getBaseResponse(HttpStatus.BAD_REQUEST, errors);

            String shortCode = ShortcodeGenerator.generateShortCode(originalUrl);

            UrlShortener existingUrlShortner = mongoService.getUrlShortByShortCode(shortCode);

            baseResponse =  checkExistingUrl(existingUrlShortner, errors);
            if (null != baseResponse) {
                return baseResponse;
            }

            UrlShortener urlShortener = new UrlShortener(originalUrl, shortCode, new Date(), createExpiryDate(days), true);
            boolean success = mongoService.saveUrlShort(urlShortener);
            if (success) {
                logger.info("Successfully saved url shortener");
                return ResponseUtility.getBaseResponse(HttpStatus.OK, urlShortener);
            } else {
                logger.error("Failed to save url shortener");
                errors.add(Error.builder()
                        .message(Constant.EXCEPTION_OCCURRED_WHILE_CREATING_URL_SHORTENER)
                        .errorCode(String.valueOf(Error.ERROR_TYPE.BUSINESS.toCode()))
                        .errorType(Error.ERROR_TYPE.BUSINESS.toValue())
                        .level(Error.SEVERITY.HIGH.name())
                        .build());
                return ResponseUtility.getBaseResponse(HttpStatus.INTERNAL_SERVER_ERROR, errors);
            }

        } catch (Exception e) {
            Error error = new Error();
            error.setMessage(e.getMessage());
            logger.error("exception occurred while creating short url with probable cause - ", e);
            return ResponseUtility.getBaseResponse(HttpStatus.INTERNAL_SERVER_ERROR, Collections.singleton(error));
        }
    }

    private BaseResponse checkExistingUrl(UrlShortener checkExistingUrl, Collection<Error> errors) {
        if (checkExistingUrl != null) {
            boolean isLinkExpired = checkLinkExpire(checkExistingUrl.getExpirationDate());
            if (isLinkExpired) {
                errors.add(Error.builder()
                        .message(Constant.LINK_EXPIRED)
                        .errorCode(String.valueOf(Error.ERROR_TYPE.BUSINESS.toCode()))
                        .errorType(Error.ERROR_TYPE.BUSINESS.toValue())
                        .level(Error.SEVERITY.MEDIUM.name())
                        .build());
                return ResponseUtility.getBaseResponse(HttpStatus.GONE, errors);
            }
            checkExistingUrl.setMessage(Constant.URL_ALREADY_EXISTS_AND_ACTIVE);
            return ResponseUtility.getBaseResponse(HttpStatus.OK, checkExistingUrl);
        }
        return null;
    }

    @Override
    public String getOriginalUrl(String shortCode) {
        logger.info("Inside getOriginalUrl method for short code {}", shortCode);

        try {
            UrlShortener urlShortener = mongoService.getUrlShortByShortCode(shortCode);

            if (urlShortener != null) {
                boolean isLinkExpired = checkLinkExpire(urlShortener.getExpirationDate());
                if (isLinkExpired) {
                    logger.error(Constant.LINK_EXPIRED);
                    return null;
                }
                return urlShortener.getOriginalUrl();
            } else {
                logger.error(Constant.NO_LINK_FOUND_BY_SHORT_CODE);
                return null;
            }

        } catch (Exception e) {
            logger.error("exception occurred while getting original url with probable cause - ", e);
            return null;
        }

    }

    private boolean validateUrl(String originalUrl, Collection<Error> errors) {
        if (!originalUrl.startsWith(Constant.PROTOCOL)) {

            errors.add(Error.builder()
                    .message(Constant.INVALID_URL)
                    .errorCode(String.valueOf(Error.ERROR_TYPE.BAD_REQUEST.toCode()))
                    .errorType(Error.ERROR_TYPE.BAD_REQUEST.toValue())
                    .level(Error.SEVERITY.HIGH.name())
                    .build());
            logger.error("Original URL validation failed");
            return true;
        }
        return false;
    }

    private Date createExpiryDate(int days) {
        return new Date(new Date().getTime() + (long) days * DAY_IN_MILLIS);
    }

    private boolean checkLinkExpire(Date expirationDate) {
        Date now = new Date();
        return expirationDate.before(now);
    }

}
