package in.guardianservice.link.shortner.model;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
@Entity
@Table(name = "url_shortener")
@Data
public class UrlShortener {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "longurl", nullable = false, length = 1000)
    private String longUrl;

    @Column(name = "shortcode", nullable = false, length = 25)
    private String shortCode;

    @Column(name = "shorturl", nullable = false, length = 50)
    private String shortUrl;

    @Column(name = "qrcode", columnDefinition = "TEXT")
    private String qrCode;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "expired_at")
    private LocalDateTime expiredAt;

    // Constructors, getters, and setters

    public UrlShortener() {}

    public UrlShortener(String longUrl, String shortCode, String shortUrl, String qrCode, LocalDateTime expiredAt) {
        this.longUrl = longUrl;
        this.shortCode = shortCode;
        this.shortUrl = shortUrl;
        this.qrCode = qrCode;
        this.createdAt = LocalDateTime.now();
        this.expiredAt = expiredAt;
    }

}
