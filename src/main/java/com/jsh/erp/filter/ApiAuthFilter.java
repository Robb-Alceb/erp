package com.jsh.erp.filter;

import com.alibaba.fastjson.JSONObject;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebFilter(filterName = "ApiFilter", urlPatterns = {"/*"},
        initParams = {@WebInitParam(name = "filterPath",
                              value = "/api")})
public class ApiAuthFilter implements Filter {

    private static final String key = "lumiere_erp_123";
    private static final String secret = "lumiere_erp_123";
    private static final String FILTER_PATH = "filterPath";

    private String[] authUrls;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String filterPath = filterConfig.getInitParameter(FILTER_PATH);
        if (!StringUtils.isEmpty(filterPath)) {
            authUrls = filterPath.contains("#") ? filterPath.split("#") : new String[]{filterPath};
        }
    }
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        HttpServletResponse servletResponse = (HttpServletResponse) response;
        String requestUrl = servletRequest.getRequestURI();
        String secret = servletRequest.getHeader(key);

        if (null != authUrls && authUrls.length > 0) {
            boolean match = false;
            for (String url : authUrls) {
                if (requestUrl.startsWith(url)) {
                    if(secret != null && secret.equals(secret)){
                        chain.doFilter(request, response);
                        return;
                    }
                    match = true;
                }
            }
            if(match){
                servletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("error","no permission");
                servletResponse.getWriter().print(jsonObject.toJSONString());
            }
        }
        chain.doFilter(request, response);
//        servletResponse.sendRedirect("/login.html");
    }

    private static String regexPrefix = "^.*";
    private static String regexSuffix = ".*$";

    private static boolean verify(List<String> ignoredList, String url) {
        for (String regex : ignoredList) {
            Pattern pattern = Pattern.compile(regexPrefix + regex + regexSuffix);
            Matcher matcher = pattern.matcher(url);
            if (matcher.matches()) {
                return true;
            }
        }
        return false;
    }
    @Override
    public void destroy() {

    }
}