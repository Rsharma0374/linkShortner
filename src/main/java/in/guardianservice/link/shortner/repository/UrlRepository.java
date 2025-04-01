package in.guardianservice.link.shortner.repository;

import in.guardianservice.link.shortner.model.UrlShortener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
public class UrlRepository {

    final private JdbcTemplate jdbcTemplate;

    public UrlRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public Optional<UrlShortener> getUrlShortByShortCode(String shortCode) {
        String sql = "SELECT * FROM url_shortener WHERE shortcode = ?";

        return jdbcTemplate.query(sql, new Object[]{shortCode}, new UrlShortenerRowMapper())
                .stream()
                .findFirst();
    }

    public boolean saveUrlShort(UrlShortener urlShortener) {
        String sql = "INSERT INTO url_shortener (longurl, shortcode, shorturl, qrcode, created_at, expired_at, user_name) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        log.debug("Insert sql query is {}", sql);


        int result = jdbcTemplate.update(sql,
                urlShortener.getLongUrl(),
                urlShortener.getShortCode(),
                urlShortener.getShortUrl(),
                urlShortener.getQrCode(),
                urlShortener.getCreatedAt(),
                urlShortener.getExpiredAt(),
                urlShortener.getUser());

        return result > 0; // Returns true if insert is successful
    }

    public List<UrlShortener> getUrlDataByUser(String identifier) {
        String sql = "SELECT * FROM url_shortener WHERE user_name = ?";

        return jdbcTemplate.query(sql, new Object[]{identifier}, new UrlShortenerRowMapper());
    }

    public boolean deleteRecordByShortUrlAndUser(String shortUrl, String user) {
        String sql = "DELETE FROM url_shortener WHERE shorturl = ? AND user_name = ?";

        int rowsAffected = jdbcTemplate.update(sql, shortUrl, user);
        return rowsAffected > 0; // Returns true if at least one row is deleted
    }

    public boolean updateRecordBylongUrl(String longUrl, LocalDateTime expiryDate, String user) {

        String sql = "UPDATE url_shortener SET expired_at = ? WHERE longurl = ? AND user_name = ?" ;


        int rowsAffected = jdbcTemplate.update(sql, expiryDate, longUrl, user);
        return rowsAffected > 0; // Returns true if at least one row is updated
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
