package com.gov.sistem.reservation.login.service.impl;

import com.gov.sistem.reservation.login.service.IJwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Servicio para manejar la creación, validación y extracción de información de JSON Web Tokens (JWT).
 */
@Service
public class JwtService implements IJwtService {

    // Clave secreta utilizada para firmar los JWT. Debe ser segura y secreta.
    @Value("${jwt.secret-key}")
    private String SECRET_KEY;

    // Tiempo de expiración del token en milisegundos (por ejemplo, 24 horas).
    @Value("${jwt.expiration-time-ms}")
    private long expirationTime;

    /**
     * Genera un JWT para un usuario dado con los reclamos adicionales por defecto.
     *
     * @param userDetails Detalles del usuario para incluir en el token.
     * @return El JWT generado.
     */
    public String getToken(UserDetails userDetails) {
        return getToken(new HashMap<>(), userDetails);
    }

    /**
     * Genera un JWT con reclamos adicionales especificados.
     *
     * @param extraClaims Reclamos adicionales para incluir en el token.
     * @param userDetails Detalles del usuario para incluir en el token.
     * @return El JWT generado.
     */
    private String getToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(extraClaims) // Establece los reclamos adicionales.
                .setSubject(userDetails.getUsername()) // Establece el sujeto del token (generalmente el nombre de usuario).
                .setIssuedAt(new Date()) // Fecha de creación del token.
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime)) // Fecha de expiración del token.
                .signWith(getKey(), SignatureAlgorithm.HS256) // Firma el token con la clave secreta usando el algoritmo HMAC SHA256.
                .compact(); // Genera el token JWT.
    }

    /**
     * Obtiene la clave secreta utilizada para firmar el JWT.
     *
     * @return La clave secreta como un objeto Key.
     */
    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY); // Decodifica la clave secreta desde Base64.
        return Keys.hmacShaKeyFor(keyBytes); // Genera una clave HMAC a partir del byte array decodificado.
    }

    /**
     * Extrae el nombre de usuario del token JWT.
     *
     * @param token El JWT del cual se extraerá el nombre de usuario.
     * @return El nombre de usuario extraído del token.
     */
    public String getUsernameFromToken(String token) {
        return getClaim(token, Claims::getSubject); // Extrae el sujeto del token, que es el nombre de usuario.
    }

    /**
     * Verifica si el token JWT es válido en relación con los detalles del usuario.
     *
     * @param token El JWT a validar.
     * @param userDetails Detalles del usuario para comparar con el token.
     * @return True si el token es válido; False en caso contrario.
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token); // Obtiene el nombre de usuario del token.
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token)); // Verifica el nombre de usuario y la expiración del token.
    }

    /**
     * Obtiene un reclamo específico del token JWT.
     *
     * @param token El JWT del cual se extraerá el reclamo.
     * @param claimsResolver Función que define cómo extraer el reclamo del objeto Claims.
     * @param <T> El tipo del reclamo a extraer.
     * @return El valor del reclamo extraído.
     */
    private <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaims(token); // Obtiene todos los reclamos del token.
        return claimsResolver.apply(claims); // Aplica la función para extraer el reclamo específico.
    }

    /**
     * Obtiene todos los reclamos del token JWT.
     *
     * @param token El JWT del cual se extraerán los reclamos.
     * @return El objeto Claims que contiene todos los reclamos del token.
     * @throws SecurityException Si la firma del token no es válida.
     */
    private Claims getAllClaims(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(getKey()) // Establece la clave secreta para verificar la firma.
                    .build()
                    .parseClaimsJws(token) // Analiza el token JWT.
                    .getBody(); // Obtiene el cuerpo del JWT, que contiene los reclamos.
        } catch (ExpiredJwtException e) {
            // Manejar caso de token expirado si es necesario
            throw e;
        } catch (Exception e) {
            // Manejar otras excepciones (por ejemplo, firma inválida)
            throw new SecurityException("Token inválido", e);
        }
    }

    /**
     * Obtiene la fecha de expiración del token JWT.
     *
     * @param token El JWT del cual se extraerá la fecha de expiración.
     * @return La fecha de expiración del token.
     */
    private Date getExpirationDate(String token) {
        return getClaim(token, Claims::getExpiration); // Extrae la fecha de expiración del token.
    }

    /**
     * Verifica si el token JWT ha expirado.
     *
     * @param token El JWT a verificar.
     * @return True si el token ha expirado; False en caso contrario.
     */
    private boolean isTokenExpired(String token) {
        return getExpirationDate(token).before(new Date()); // Compara la fecha de expiración con la fecha actual.
    }
}
