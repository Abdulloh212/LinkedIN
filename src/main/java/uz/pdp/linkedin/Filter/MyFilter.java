package uz.pdp.linkedin.Filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import uz.pdp.linkedin.Entity.User;
import uz.pdp.linkedin.Repo.UserRepository;
import uz.pdp.linkedin.Servises.JwtService;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class MyFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository usersRepository;

    public MyFilter(JwtService jwtService, UserRepository usersRepository) {
        this.jwtService = jwtService;
        this.usersRepository = usersRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = authHeader.substring(7);
        if (!jwtService.validateToken(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        User email = jwtService.getUserFromToken(token);
        Optional<User> userOptional = usersRepository.findByEmail(email.getEmail());
        if (userOptional.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        User user = userOptional.get();
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                user,
                null,
                user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getRoleName().name()))
                        .collect(Collectors.toList())
        );

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}