package in.guardianservice.link.shortner.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "urls")
public class UrlShortener {

    @Id
    private String id;
    private String originalUrl;
    private String shortCode;
    private Date creationDate;
    private Date expirationDate;
    private boolean active;
    private String message;

    public UrlShortener(String originalUrl, String shortCode, Date creationDate, Date expirationDate, boolean active) {
        this.originalUrl = originalUrl;
        this.shortCode = shortCode;
        this.creationDate = creationDate;
        this.expirationDate = expirationDate;
        this.active = active;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "UrlShortener{" +
                "id='" + id + '\'' +
                ", originalUrl='" + originalUrl + '\'' +
                ", shortCode='" + shortCode + '\'' +
                ", creationDate=" + creationDate +
                ", expirationDate=" + expirationDate +
                ", active=" + active +
                ", message='" + message + '\'' +
                '}';
    }
}
