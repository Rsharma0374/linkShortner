package in.guardianservice.link.shortner.repository;

import in.guardianservice.link.shortner.model.UrlShortener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public class UrlRepository {

    final private JdbcTemplate jdbcTemplate;

    public UrlRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public Optional<UrlShortener> getUrlShortByShortCode(String shortCode) {
        String sql = "SELECT * FROM url WHERE shortcode = ?";

        return jdbcTemplate.query(sql, new Object[]{shortCode}, new UrlShortenerRowMapper())
                .stream()
                .findFirst();
    }

    public boolean saveUrlShort(UrlShortener urlShortener) {
        String sql = "INSERT INTO url (longurl, shortcode, shorturl, qrcode, created_at, expired_at) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        int result = jdbcTemplate.update(sql,
                urlShortener.getLongUrl(),
                urlShortener.getShortCode(),
                urlShortener.getShortUrl(),
                urlShortener.getQrCode(),
                urlShortener.getCreatedAt(),
                urlShortener.getExpiredAt());

        return result > 0; // Returns true if insert is successful
    }
    // Custom RowMapper to map ResultSet to UrlShortener object
    private static class UrlShortenerRowMapper implements RowMapper<UrlShortener> {
        @Override
        public UrlShortener mapRow(ResultSet rs, int rowNum) throws SQLException {
            UrlShortener urlShortener = new UrlShortener();
            urlShortener.setId(rs.getLong("id"));
            urlShortener.setLongUrl(rs.getString("longurl"));
            urlShortener.setShortCode(rs.getString("shortcode"));
            urlShortener.setShortUrl(rs.getString("shorturl"));
            urlShortener.setQrCode(rs.getString("qrcode"));
            urlShortener.setCreatedAt(rs.getObject("created_at", LocalDateTime.class));
            urlShortener.setExpiredAt(rs.getObject("expired_at", LocalDateTime.class));
            return urlShortener;
        }
    }
}
