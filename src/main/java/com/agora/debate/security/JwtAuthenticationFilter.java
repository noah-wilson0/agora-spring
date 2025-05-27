package com.agora.debate.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate redisTemplate;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("필터 진행");
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        String path = httpRequest.getRequestURI();

        // 필터를 적용하지 않을 경로 설정
        if (path.startsWith("/members/sign-in") || path.startsWith("/members/sign-in/test")
                || path.startsWith("/members/signup")) {
            chain.doFilter(request, response);
            return;
        }

        String token = resolveToken(httpRequest);

        // AT 블랙리스트검사
        if (token != null && jwtTokenProvider.validateToken(token)) {
            if (Boolean.TRUE.equals(redisTemplate.hasKey("blacklist:" + token))) {
                log.info("blacklist 확인 실행");
                HttpServletResponse httpResponse = (HttpServletResponse) response;
                SecurityContextHolder.clearContext();
                httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                httpResponse.setContentType("application/json; charset=UTF-8");
                httpResponse.getWriter().write("{\"error\": \"블랙리스트에 등록된 토큰입니다. 재로그인 필요\"}");
                return;
            }
            else{
                log.info("유효성 검사");
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        chain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
