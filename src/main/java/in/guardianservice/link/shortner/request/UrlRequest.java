package in.guardianservice.link.shortner.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class UrlRequest {

    @JsonProperty("sLongUrl")
    private String longUrl;

    @JsonProperty("iExpiryDay")
    private int expiryDay;

    @JsonProperty("sShortUrl")
    private String shortUrl;

    @JsonProperty("sUser")
    private String user;
}
