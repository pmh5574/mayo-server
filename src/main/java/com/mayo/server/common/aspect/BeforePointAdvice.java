package com.mayo.server.common.aspect;

import com.mayo.server.auth.app.service.JwtTokenProvider;
import com.mayo.server.chef.app.port.out.ChefQueryOutputPort;
import com.mayo.server.chef.domain.model.Chef;
import com.mayo.server.common.Constants;
import com.mayo.server.common.annotation.PasswordAccess;
import com.mayo.server.common.annotation.PublicAccess;
import com.mayo.server.common.annotation.RefreshTokenAccess;
import com.mayo.server.common.enums.ErrorCode;
import com.mayo.server.common.enums.UserType;
import com.mayo.server.common.exception.AuthException;
import com.mayo.server.common.exception.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
@RequiredArgsConstructor
@Aspect
@Order
public class BeforePointAdvice {

    private final JwtTokenProvider jwtTokenProvider;
    private final ChefQueryOutputPort queryOutputPort;

    @Before("com.mayo.server.common.aspect.ControllerPointCut.allController()")
    public void checkBeforeController(JoinPoint joinPoint) {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
        if (HttpMethod.OPTIONS.matches(request.getMethod())) {
            return;
        }
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        PublicAccess publicAccess = method.getDeclaredAnnotation(PublicAccess.class);
        if (Objects.nonNull(publicAccess)) {
            return;
        }

        PasswordAccess passwordAccess = method.getDeclaredAnnotation(PasswordAccess.class);
        if (Objects.nonNull(passwordAccess)) {
            String passwordToken = extractUsername(request.getHeader(Constants.HEADER_PASSWORD_TOKEN));

            if (Objects.isNull(passwordToken) || passwordToken.trim().isEmpty()) {
                throw new AuthException(ErrorCode.JWT_NOT_FOUND_TOKEN);
            }

            jwtTokenProvider.getTokenClaims(passwordToken);
            String id = jwtTokenProvider.getTokenClaims(passwordToken).getBody().get("sub").toString();

            Chef chef = queryOutputPort.getChefById(id);
            if (Objects.isNull(chef)) {
                throw new NotFoundException(ErrorCode.CHEF_NOT_FOUND);
            }

            request.setAttribute("id", id);
            return;
        }

        RefreshTokenAccess refreshAnnotation = method.getDeclaredAnnotation(RefreshTokenAccess.class);
        String refreshToken = null;
        if (Objects.nonNull(refreshAnnotation)) {
            refreshToken = extractUsername(request.getHeader(Constants.HEADER_REFRESH_TOKEN));
        }

        String accessToken = extractUsername(request.getHeader(Constants.HEADER_ACCESS_TOKEN));
        if (Objects.isNull(accessToken) || accessToken.trim().isEmpty()) {
            throw new AuthException(ErrorCode.JWT_NOT_FOUND_TOKEN);
        }

        if (Objects.isNull(jwtTokenProvider.getTokenClaims(accessToken).getBody())) {
            throw new AuthException(ErrorCode.JWT_VALIDATE_ERROR);
        }

        if (UserType.CHEF.name().equals(jwtTokenProvider.getTokenClaims(accessToken).getBody().get("UserType"))) {
            String id = jwtTokenProvider.getTokenClaims(accessToken).getBody().get("sub").toString();

            Chef chef = queryOutputPort.getChefById(id);
            if (Objects.isNull(chef)) {
                throw new NotFoundException(ErrorCode.CHEF_NOT_FOUND);
            }

            if(chef.getIsApproved().equals(0)){
                throw new AuthException(ErrorCode.CHEF_NOT_APPROVED);
            }

            request.setAttribute("id", id);
        }


    }

    private String extractUsername(String token) {

        if (Objects.isNull(token) || token.trim().isEmpty()) {
            throw new AuthException(ErrorCode.JWT_NOT_FOUND_TOKEN);
        }

        List<String> splitToken = Arrays.stream(Optional.of(token.split(" "))
                        .orElseThrow(() -> new AuthException(ErrorCode.JWT_NOT_FOUND_TOKEN)))
                .toList();

        if (splitToken.size() < 2) {
            throw new AuthException(ErrorCode.JWT_VALIDATE_ERROR);
        }

        return splitToken.get(1);

    }


}
