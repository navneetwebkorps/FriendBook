/*
 * package com.friendbook1.config;
 * 
 * import org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.security.authentication.AbstractAuthenticationToken;
 * import org.springframework.security.authentication.
 * UsernamePasswordAuthenticationToken; import
 * org.springframework.security.core.Authentication; import
 * org.springframework.security.core.context.SecurityContextHolder; import
 * org.springframework.security.web.authentication.
 * WebAuthenticationDetailsSource; import
 * org.springframework.stereotype.Component; import
 * org.springframework.web.filter.OncePerRequestFilter;
 * 
 * import com.friendbook1.entities.User; import
 * com.friendbook1.services.UserService; import
 * com.friendbook1.services.UserServices;
 * 
 * import javax.servlet.FilterChain; import javax.servlet.ServletException;
 * import javax.servlet.http.HttpServletRequest; import
 * javax.servlet.http.HttpServletResponse; import java.io.IOException; import
 * java.util.ArrayList; import java.util.Optional;
 * 
 * @Component public class CustomAuthenticationFilter extends
 * OncePerRequestFilter {
 * 
 * @Autowired private UserService userService;
 * 
 * @Autowired public CustomAuthenticationFilter(UserService userService) {
 * this.userService = userService; }
 * 
 * @Override protected void
 * doFilterInternal(jakarta.servlet.http.HttpServletRequest request,
 * jakarta.servlet.http.HttpServletResponse response,
 * jakarta.servlet.FilterChain filterChain) throws
 * jakarta.servlet.ServletException, IOException { // TODO Auto-generated method
 * 
 * String email = request.getParameter("email"); String password =
 * request.getParameter("password"); System.out.println("in filter interna" +
 * email + password); if (email != null && password != null) {
 * System.out.println("innnnnnn" + email + password);
 * 
 * Optional<User> user = userService.authenticate(email);
 * System.out.println("innnnnnn"); if (user != null) { // Set authentication in
 * Authentication authentication = new UsernamePasswordAuthenticationToken(user,
 * null, new ArrayList<>()); ((AbstractAuthenticationToken) authentication)
 * .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
 * SecurityContextHolder.getContext().setAuthentication(authentication); } }
 * 
 * filterChain.doFilter(request, response);
 * 
 * } }
 */


