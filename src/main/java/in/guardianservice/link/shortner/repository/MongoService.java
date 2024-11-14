package in.guardianservice.link.shortner.repository;

import in.guardianservice.link.shortner.constants.Constant;
import in.guardianservice.link.shortner.model.UrlShortener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
public class MongoService {
    private static final Logger logger = LoggerFactory.getLogger(MongoService.class);
    @Autowired
    private MongoTemplate mongoTemplate;


    public boolean saveUrlShort(UrlShortener urlShortener) {
        logger.info("Saving url shortener...");
        try {
            mongoTemplate.save(urlShortener);
            return true;
        } catch (Exception e) {
            logger.error("Error saving url shortener with probable cause - ", e);
            return false;
        }
    }

    public UrlShortener getUrlShortByShortCode(String shortCode) {
        logger.info("Getting url shortener by short code {}", shortCode);
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where(Constant.SHORT_CODE).is(shortCode)
                    .and(Constant.ACTIVE).is(true));

            return mongoTemplate.findOne(query, UrlShortener.class);

        } catch (Exception e) {
            logger.error("Error getting url shortener by short code {} with probable cause - ", shortCode, e);
            return null;
        }
    }
}
