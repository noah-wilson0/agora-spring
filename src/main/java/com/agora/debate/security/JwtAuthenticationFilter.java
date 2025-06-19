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

    private String token=null;
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("필터 진행");

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String path2 = httpRequest.getRequestURI();
        log.info("요청 URI = {}", path2);
        // 🔐 OPTIONS 요청은 필터를 건너뛴다 (CORS preflight)
        if ("OPTIONS".equalsIgnoreCase(httpRequest.getMethod())) {
            log.info("OPTIONS 요청 - 필터 우회");
            chain.doFilter(request, response);
            return;
        }
        // 쿠키 존재 여부 로그
        if (httpRequest.getCookies() != null) {
            log.info("✅ 쿠키 존재 - 총 {}개", httpRequest.getCookies().length);
            for (jakarta.servlet.http.Cookie cookie : httpRequest.getCookies()) {
                log.info("쿠키 이름 = {}, 값 = {}", cookie.getName(), cookie.getValue());

                if ("accessToken".equals(cookie.getName())) {
                    token = cookie.getValue();
                }
            }
        }
        String path = httpRequest.getRequestURI();

        // 필터를 적용하지 않을 경로 설정
        if (path.startsWith("/members/sign-in") || path.startsWith("/members/sign-in/test") || path.startsWith("/members/signup") ||
                path.startsWith("/ws-chat") || path.startsWith("/api/chat/history")) {
            chain.doFilter(request, response);
            return;
        }


        if (httpRequest.getCookies() != null) {
            for (jakarta.servlet.http.Cookie cookie : httpRequest.getCookies()) {
                if ("accessToken".equals(cookie.getName())) {
                    token=cookie.getValue();
                }
            }
        }else{
            log.info("토큰 없음");
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            SecurityContextHolder.clearContext();
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.setContentType("application/json; charset=UTF-8");
            httpResponse.getWriter().write("{\"error\": \"토큰이 없습니다.\"}");
            return;
        }

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


}
