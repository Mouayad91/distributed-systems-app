package de.htwsaar.vs.gruppe05.server.authentication;

import de.htwsaar.vs.gruppe05.server.enums.AuthExceptionType;
import de.htwsaar.vs.gruppe05.server.exceptions.AuthTokenException;
import de.htwsaar.vs.gruppe05.server.helper.JWTHelper;
import de.htwsaar.vs.gruppe05.server.model.User;
import de.htwsaar.vs.gruppe05.server.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

/**
 * JWT-Filter
 * Checks for a JWT in Authorization Header and validates the token
 * if valid ->  Proceed current context
 * if not valid -> AuthenticationEntryPoint (abstract)
 */
@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JWTHelper jwtHelper;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String tokenHeader = request.getHeader("Authorization");
        String username;
        String token;
        try {
            if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
                token = tokenHeader.substring(7);
                username = jwtHelper.getAllClaimsFromToken(token).getSubject();

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    Optional<User> userDetails = userRepository.findByUserName(username);
                    if (userDetails.isPresent()) {
                        if (!jwtHelper.isInvalid(token, userDetails.get())) {

                            UsernamePasswordAuthenticationToken
                                    authenticationToken = new UsernamePasswordAuthenticationToken(
                                    userDetails.get(), userDetails.<Object>map(User::getRole).orElse(null));
                            authenticationToken.setDetails(new
                                    WebAuthenticationDetailsSource().buildDetails(request));
                            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                            SecurityContextHolder.getContext().setAuthentication(
                                    new UsernamePasswordAuthenticationToken(userDetails.get(), null, Collections.singleton(
                                            new SimpleGrantedAuthority("ROLE_" + userDetails.get().getRole().name()))));

                        }
                    }
                } else {
                    SecurityContextHolder.clearContext();
                }
            }
            filterChain.doFilter(request, response);
        } catch (AuthTokenException ex) {
            jwtAuthenticationEntryPoint.commence(request, response, ex);
        }
    }
}



