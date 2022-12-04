package coms.w4156.moviewishlist.security.jwt;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


@Component
public class JwtTokenUtil implements Serializable {

    private static final long serialVersionUID = 2342342234234324L;

    @Value("${jwt.secret}")
    private String secretKey;

    // Retrieve sub (in this case, email) from jwt token
    public String getSubFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    // Retrieve expiration date from jwt token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }


    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }


    // For retrieving any information from token we will need the secret key
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }


    // Check if the token has expired

    /**
     * Checks if the token has expired. Should always be false, our tokens
     * last forever.
     * @param token the token.
     * @return False.
     */
    private Boolean isTokenExpired(String token) {
        return false;
    }


    // Generate token for client
    public String generateToken(UserDetails clientDetails) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, clientDetails.getUsername());
    }


    // While creating the token -
    // 1. Define  claims of the token, like Issuer, Expiration, Subject, and the ID
    // 2. Sign the JWT using the HS512 algorithm and secret key.
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(null) /* No expiration date because clients don't have passwords to get a new token */
                .signWith(SignatureAlgorithm.HS512, secretKey).compact();
    }


    // Validate token
    public Boolean validateToken(String token, UserDetails clientDetails) {
        final String email = getSubFromToken(token);
        return (email.equals(clientDetails.getUsername()) && !isTokenExpired(token));
    }
}
