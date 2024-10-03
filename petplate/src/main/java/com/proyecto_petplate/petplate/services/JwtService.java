package com.proyecto_petplate.petplate.services;

import java.util.Date; // Importar la clase Date para manejar fechas

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto_petplate.petplate.Repositories.SessionRepository;

import java.security.Key; // Importar la clase Key para manejar claves de seguridad

import io.jsonwebtoken.Claims; // Importar la clase Claims para manejar las afirmaciones del token JWT
import io.jsonwebtoken.Jwts; // Importar la clase Jwts para crear y validar tokens JWT
import io.jsonwebtoken.io.Decoders; // Importar la clase Decoders para decodificar la clave secreta
import io.jsonwebtoken.security.Keys; // Importar la clase Keys para generar la clave secreta

@Service
public class JwtService {

    // Clave secreta para firmar los tokens. Debe ser almacenada de forma segura.
    private static final String SECRET_KEY="586E3272357538782F413F442847ELPSYCONGROO68566B597033733676397924";

    @Autowired
    private SessionRepository sessionRepo;

    // Método para generar un token JWT utilizando el nombre de usuario y el ID del usuario
    public String getToken(String userName, int userID) {
        return Jwts
            .builder() // Iniciar la construcción del token
            .claim("ID", userID) // Añadir el ID del usuario como una reclamación
            .setSubject(userName) // Establecer el sujeto del token (nombre de usuario)
            .setIssuedAt(new Date(System.currentTimeMillis())) // Establecer la fecha de emisión del token
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // Establecer la fecha de expiración (24 horas)
            .signWith(getKey()) // Firmar el token con la clave secreta
            .compact(); // Construir el token y devolverlo como una cadena
    }

    // Método privado para obtener la clave secreta como un objeto Key
    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY); // Decodificar la clave secreta en base64
        return Keys.hmacShaKeyFor(keyBytes); // Generar una clave HMAC-SHA-256 a partir de los bytes de la clave
    }

    // Método público para obtener el nombre de usuario desde el token
    public String getUsernameFromToken(String token) {
        return getAllClaims(token).getSubject(); // Obtener el sujeto del token (nombre de usuario)
    }

    // Método público para obtener el ID del usuario desde el token
    public String getUserIdFromToken(String token) {
        return (String) getAllClaims(token).get("ID"); // Obtener la reclamación "ID" del token
    }

    // Método público para obtener la fecha de expiración del token
    public Date getExpirationFromToken(String token) {
        return getAllClaims(token).getExpiration(); // Obtener la fecha de expiración del token
    }

    // Método público para obtener la fecha de creación del token
    public Date getCreationFromToken(String token) {
        return (Date) getAllClaims(token).getIssuedAt(); // Obtener la fecha de creación del token
    }

    // Método para validar el token comparando el nombre de usuario con los detalles del usuario
    public boolean isTokenValid(String token) {
        try {
            return !isTokenExpired(token) && sessionRepo.existsBySesionToken(token); // Validar el token
        } catch (Exception e) {
            // Manejo de excepciones si el token no es válido
            return false;
        }
    }

    // Método privado para obtener todos los reclamos del token
    private Claims getAllClaims(String token) {
        try {
            return Jwts
            .parserBuilder() // Usar el constructor de parser para crear un analizador de tokens
            .setSigningKey(getKey()) // Establecer la clave de firma para la verificación
            .build() // Construir el analizador
            .parseClaimsJws(token) // Parsear el token y obtener el objeto Jws<Claims>
            .getBody(); // Obtener el objeto Claims del Jws
        } catch (Exception e) {
            // Manejo de excepciones si el token no se puede analizar
            throw new RuntimeException("el token no se puede analizar", e);
        }
        
    }

    // Método privado para verificar si el token ha expirado
    private boolean isTokenExpired(String token) {
        return getExpirationFromToken(token).before(new Date()); // Comparar la fecha de expiración con la fecha actual
    }
}
