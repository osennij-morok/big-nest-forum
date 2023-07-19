package net.gastipatis.bignestforum.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import net.gastipatis.bignestforum.dto.auth.RefreshRequest;
import net.gastipatis.bignestforum.security.exception.InvalidRefreshTokenException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;

@Component
public class JwtManager {

    private final Algorithm algorithm;

    private static final String ROLE_CLAIM = "role";

    public JwtManager(@Value("${TOKEN_SIGNING_PASSWORD}") String tokenSigningPassword) {
        this.algorithm = Algorithm.HMAC256(tokenSigningPassword);
    }

    public JwtTokenPair tokensFromUserDetails(UserDetails userDetails) {
        String userRole = userDetails
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                // get only roles authorities
                .filter(authority -> authority.startsWith("ROLE_"))
                // get the roles themselves
                .map(roleAuthority -> roleAuthority.split("ROLE_")[1])
                .filter(role -> !role.isBlank())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("User doesn't have any role"));
        String accessToken = createAccessToken(userDetails.getUsername(), userRole);
        String refreshToken = createRefreshToken(userDetails.getUsername(), userRole);
        return new JwtTokenPair(accessToken, refreshToken);
    }

    private String createAccessToken(String username, String userRole) {
        return JWT.create()
                .withSubject(username)
                .withClaim(ROLE_CLAIM, userRole)
                .withExpiresAt(Instant.now().plus(Duration.ofMinutes(15)))
                .sign(algorithm);
    }

    private String createRefreshToken(String username, String userRole) {
        return JWT.create()
                .withSubject(username)
                .withClaim(ROLE_CLAIM, userRole)
                .withExpiresAt(Instant.now().plus(Duration.ofDays(1)))
                .sign(algorithm);
    }

    public boolean verifyAccessToken(String token) {
        JWTVerifier verifier = JWT.require(algorithm)
                .withClaimPresence(ROLE_CLAIM)
                .build();
        try {
            verifier.verify(token);
        } catch (JWTVerificationException e) {
            return false;
        }
        return true;
    }

    public String refresh(RefreshRequest refresh) throws InvalidRefreshTokenException {
        JWTVerifier verifier = JWT.require(algorithm)
                .withClaimPresence(ROLE_CLAIM)
                .build();
        try {
            verifier.verify(refresh.getRefreshToken());
        } catch (JWTVerificationException e) {
            throw new InvalidRefreshTokenException("Invalid refresh token", e);
        }
        DecodedJWT refreshToken = JWT.decode(refresh.getRefreshToken());
        return createAccessToken(
                refreshToken.getSubject(),
                refreshToken.getClaim(ROLE_CLAIM).asString());
    }

    public UserDetails extractUserDetails(String accessTokenStr, UserDetailsFactory factory) {
        DecodedJWT accessToken = JWT.decode(accessTokenStr);
        String username = accessToken.getSubject();
        String role = accessToken.getClaim("role").asString();
        return factory.create(username, Collections.singletonList(role));
    }
}
