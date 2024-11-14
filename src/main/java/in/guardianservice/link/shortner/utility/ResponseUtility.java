package in.guardianservice.link.shortner.utility;

import in.guardianservice.link.shortner.response.BaseResponse;
import in.guardianservice.link.shortner.response.Error;
import in.guardianservice.link.shortner.response.Payload;
import in.guardianservice.link.shortner.response.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.util.Collection;
import java.util.Collections;

public class ResponseUtility {

    private static final Logger logger = LoggerFactory.getLogger(ResponseUtility.class);


    public static BaseResponse getBaseResponse(HttpStatus httpStatus, Object buzResponse) {
        logger.info("Inside getBaseResponse method");

        if (null == buzResponse)
            buzResponse = Collections.emptyMap();

        return BaseResponse.builder()
                .payload(new Payload<>(buzResponse))
                .status(
                        Status.builder()
                                .statusCode(httpStatus.value())
                                .statusValue(httpStatus.name()).build())
                .build();
    }

    public static BaseResponse getBaseResponse(HttpStatus httpStatus, Collection<Error> errors) {
        return BaseResponse.builder()
                .status(
                        Status.builder()
                                .statusCode(httpStatus.value())
                                .statusValue(httpStatus.name()).build())
                .errors(errors)
                .build();
    }
}
