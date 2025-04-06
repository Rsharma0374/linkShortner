package in.guardianservice.link.shortner.controller;

//import com.guardianservices.kafka.services.KafkaProducerService;
import in.guardianservice.link.shortner.constants.Constant;
import in.guardianservice.link.shortner.request.DashboardDetailsRequest;
import in.guardianservice.link.shortner.request.UrlRequest;
import in.guardianservice.link.shortner.response.BaseResponse;
import in.guardianservice.link.shortner.service.UrlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/url-service")
public class UrlController {

    private static final Logger logger = LoggerFactory.getLogger(UrlController.class);

//    private final KafkaProducerService producerService;

    @Autowired
    private UrlService urlService;

//    public UrlController(KafkaProducerService producerService) {
//        this.producerService = producerService;
//    }

    @GetMapping("/welcome")
    public String welcome() {
        return "********** Welcome to link shortener **********";
    }

    @PostMapping("/get-dashboard-details")
    public ResponseEntity<BaseResponse> dashboardDetails(@RequestBody DashboardDetailsRequest dashboardDetailsRequest) {
        logger.info(Constant.CONTROLLER_STARTED, "get-dashboard-details");

        return new ResponseEntity<>(urlService.getDashboardDetails(dashboardDetailsRequest), HttpStatus.OK);
    }

    @PostMapping("/save-data")
    public ResponseEntity<BaseResponse> saveNewEntry(@RequestBody UrlRequest urlRequest) {
        logger.info(Constant.CONTROLLER_STARTED, "save-data");

        return new ResponseEntity<>(urlService.saveNewEntry(urlRequest), HttpStatus.OK);
    }


    @PostMapping("/delete-data")
    public ResponseEntity<BaseResponse> deleteEntry(@RequestBody UrlRequest urlRequest) {
        logger.info(Constant.CONTROLLER_STARTED, "delete-data");

        return new ResponseEntity<>(urlService.deleteEntry(urlRequest), HttpStatus.OK);
    }

    @PostMapping("/update-data")
    public ResponseEntity<BaseResponse> updateEntry(@RequestBody UrlRequest urlRequest) {
        logger.info(Constant.CONTROLLER_STARTED, "update-data");

        return new ResponseEntity<>(urlService.updateEntry(urlRequest), HttpStatus.OK);
    }

}
