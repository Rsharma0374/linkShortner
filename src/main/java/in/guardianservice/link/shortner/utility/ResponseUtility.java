package in.guardianservice.link.shortner.utility;

import in.guardianservice.link.shortner.response.BaseResponse;
import in.guardianservice.link.shortner.response.Error;
import in.guardianservice.link.shortner.response.Payload;
import in.guardianservice.link.shortner.response.Status;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class ResponseUtility {

    private static final Logger logger = LoggerFactory.getLogger(ResponseUtility.class);
    public static final String SOMETHING_WENT_WRONG = "Something went wrong.";


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

    public static Collection<Error> getNoContentFoundError() {
        Collection<Error> errors = new ArrayList<>();
        errors.add(Error.builder()
                .message("No content found")
                .errorCode(String.valueOf(Error.ERROR_TYPE.SYSTEM.toCode()))
                .errorType(Error.ERROR_TYPE.SYSTEM.name())
                .level(Error.SEVERITY.HIGH.name())
                .build());

        return errors;
    }

    public static Collection<Error> getInternalServerErrorError(String message) {
        Collection<Error> errors = new ArrayList<>();
        errors.add(Error.builder()
                .message(StringUtils.isEmpty(message) ? SOMETHING_WENT_WRONG : message)
                .errorCode(String.valueOf(Error.ERROR_TYPE.SYSTEM.toCode()))
                .errorType(Error.ERROR_TYPE.SYSTEM.name())
                .level(Error.SEVERITY.HIGH.name())
                .build());

        return errors;
    }

}
