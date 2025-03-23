package in.guardianservice.link.shortner.service.impl;

import in.guardianservice.link.shortner.constants.Constant;
import in.guardianservice.link.shortner.model.UrlShortener;
import in.guardianservice.link.shortner.repository.UrlRepository;
import in.guardianservice.link.shortner.response.BaseResponse;
import in.guardianservice.link.shortner.response.Error;
import in.guardianservice.link.shortner.service.UrlService;
import in.guardianservice.link.shortner.utility.QRCodeGenerator;
import in.guardianservice.link.shortner.utility.ResponseUtility;
import in.guardianservice.link.shortner.utility.ShortcodeGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class UrlServiceImpl implements UrlService {

    private static final Logger logger = LoggerFactory.getLogger(UrlServiceImpl.class);
    public static final String HTTPS_SHORTENER_GUARDIANSERVICES_IN = "https://shortener.guardianservices.in/";

    @Autowired
    private UrlRepository urlRepository;

    @Override
    public BaseResponse createShortUrl(String originalUrl, int days) {
        logger.info("Inside createShortUrl method");
        Collection<Error> errors = new ArrayList<>();
        BaseResponse baseResponse = new BaseResponse();
        try {
            if (validateUrl(originalUrl, errors)) return ResponseUtility.getBaseResponse(HttpStatus.BAD_REQUEST, errors);

            String shortCode = ShortcodeGenerator.generateShortCode(originalUrl);

            Optional<UrlShortener> existingUrlShortner = urlRepository.getUrlShortByShortCode(shortCode);

            baseResponse = existingUrlShortner.map(shortener -> checkExistingUrl(shortener, errors)).orElse(null);
            if (null != baseResponse) {
                return baseResponse;
            }
            String shortUrl = HTTPS_SHORTENER_GUARDIANSERVICES_IN + shortCode;
            String qrCode = QRCodeGenerator.generateQRCode(shortUrl);

            UrlShortener urlShortener = new UrlShortener(originalUrl, shortCode, shortUrl, qrCode, createExpiryDate(days));
            boolean success = urlRepository.saveUrlShort(urlShortener);
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
            boolean isLinkExpired = checkLinkExpire(checkExistingUrl.getExpiredAt());
            if (isLinkExpired) {
                errors.add(Error.builder()
                        .message(Constant.LINK_EXPIRED)
                        .errorCode(String.valueOf(Error.ERROR_TYPE.BUSINESS.toCode()))
                        .errorType(Error.ERROR_TYPE.BUSINESS.toValue())
                        .level(Error.SEVERITY.MEDIUM.name())
                        .build());
                return ResponseUtility.getBaseResponse(HttpStatus.GONE, errors);
            }
            return ResponseUtility.getBaseResponse(HttpStatus.OK, checkExistingUrl);
        }
        return null;
    }

    @Override
    public String getOriginalUrl(String shortCode) {
        logger.info("Inside getOriginalUrl method for short code {}", shortCode);

        try {
            Optional<UrlShortener> urlShortener = urlRepository.getUrlShortByShortCode(shortCode);

            if (urlShortener.isPresent()) {
                boolean isLinkExpired = checkLinkExpire(urlShortener.get().getExpiredAt());
                if (isLinkExpired) {
                    logger.error(Constant.LINK_EXPIRED);
                    return null;
                }
                return urlShortener.get().getLongUrl();
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

    private LocalDateTime createExpiryDate(int days) {
        return LocalDateTime.now().plusDays(days);
    }

    private boolean checkLinkExpire(LocalDateTime expirationDate) {
        LocalDateTime now = LocalDateTime.now();
        return expirationDate.isBefore(now);
    }

}
