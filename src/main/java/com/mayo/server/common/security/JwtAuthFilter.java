//package com.mayo.server.common.security;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.mayo.server.auth.app.service.JwtTokenProvider;
//import com.mayo.server.chef.app.port.out.ChefQueryOutputPort;
//import com.mayo.server.chef.domain.model.Chef;
//import com.mayo.server.common.Constants;
//import com.mayo.server.common.annotation.PublicAccess;
//import com.mayo.server.common.enums.ErrorCode;
//import com.mayo.server.common.enums.UserType;
//import com.mayo.server.common.exception.AuthException;
//import com.mayo.server.common.exception.NotFoundException;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.context.annotation.Primary;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.filter.OncePerRequestFilter;
//import org.springframework.web.method.HandlerMethod;
//import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
//
//import java.io.IOException;
//import java.lang.reflect.Method;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Objects;
//import java.util.stream.Collectors;
//
//@RequiredArgsConstructor
//@Slf4j
//public class JwtAuthFilter extends OncePerRequestFilter {
//
//    private final JwtTokenProvider jwtTokenProvider;
//    private final ChefQueryOutputPort outputPort;
//
//    @Qualifier("requestMappingHandlerMapping")
//    @Autowired
//    private RequestMappingHandlerMapping handlerMapping;
//
//    @Override
//    protected void doFilterInternal(
//            HttpServletRequest request,
//            HttpServletResponse response,
//            FilterChain filterChain
//    ) {
//
//        if (HttpMethod.OPTIONS.matches(request.getMethod())) {
//            return;
//        }
//        try {
//            HandlerMethod handlerMethod = (HandlerMethod) Objects.requireNonNull(
//                    handlerMapping.getHandler(request)
//            )
//                    .getHandler();
//
//            handlerMethod.getMethod();
//            Method method = handlerMethod.getMethod();
//            PublicAccess publicAccess = method.getDeclaredAnnotation(PublicAccess.class);
//
//            if (publicAccess != null) {
//                filterChain.doFilter(request, response);
//                return;
//            }
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//
//        String passwordToken = extractUsername(request.getHeader(Constants.HEADER_PASSWORD_TOKEN));
//
//        if(Objects.nonNull(passwordToken)) {
//            try {
//                jwtTokenProvider
//                        .getTokenClaims(passwordToken);
//
//                authenticateChef(
//                        passwordToken,
//                        request,
//                        response,
//                        filterChain
//                );
//            } catch (IllegalAccessException | ServletException | IOException e) {
//                throw new RuntimeException(e);
//            }
//
//            return;
//        }
//
//        String accessToken = extractUsername(request.getHeader(Constants.HEADER_ACCESS_TOKEN));
//        String refreshToken = extractUsername(request.getHeader(Constants.HEADER_REFRESH_TOKEN));
//
//        if (Objects.isNull(accessToken) || accessToken.trim().isEmpty()) {
//            throw new AuthException(ErrorCode.JWT_NOT_FOUND_TOKEN);
//        }
//
//        try {
//            if (Objects.isNull(jwtTokenProvider.getTokenClaims(accessToken).getBody())) {
//                throw new AuthException(ErrorCode.JWT_VALIDATE_ERROR);
//            }
//
//            if (jwtTokenProvider
//                    .getTokenClaims(accessToken)
//                    .getBody()
//                    .get("UserType")
//                    .equals(UserType.CHEF.name())) {
//
//                authenticateChef(
//                        accessToken,
//                        request,
//                        response,
//                        filterChain
//                );
//
//                return;
//            }
//
//        } catch (IllegalAccessException | ServletException | IOException e) {
//            throw new RuntimeException(e);
//        }
//
//    }
//
//    private void authenticateChef(
//            String accessToken,
//            HttpServletRequest request,
//            HttpServletResponse response,
//            FilterChain filterChain
//    ) throws IllegalAccessException, ServletException, IOException {
//
//        String id = jwtTokenProvider.getTokenClaims(accessToken).getBody().get("sub").toString();
//
//        Chef chef = outputPort.getChefById(id);
//        if (Objects.isNull(chef)) {
//            throw new NotFoundException(ErrorCode.CHEF_NOT_FOUND);
//        }
//
//        SecurityContextHolder.getContext().setAuthentication(getUserDetails(chef));
//
//        request.setAttribute("id", id);
//
//        filterChain.doFilter(request, response);
//
//    }
//
//    private UsernamePasswordAuthenticationToken getUserDetails(Chef chef) {
//
//        List<String> roles = new ArrayList<>();  // Modify this based on how roles are stored in Chef
//
//        // Convert the list of roles to a collection of GrantedAuthority
//        List<GrantedAuthority> authorities = roles.stream()
//                .map(SimpleGrantedAuthority::new)
//                .collect(Collectors.toList());
//
//        // Return a UsernamePasswordAuthenticationToken with chef as the principal and authorities
//        return new UsernamePasswordAuthenticationToken(chef, null, authorities);
//    }
//
//    private String extractUsername(String token) {
//
//        List<String> splitToken = Arrays.stream(token.split(" ")).toList();
//
//        return splitToken.get(1);
//
//    }
//
//}
