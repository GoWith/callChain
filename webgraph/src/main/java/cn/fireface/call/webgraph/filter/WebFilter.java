package cn.fireface.call.webgraph.filter;

import cn.fireface.call.webgraph.filter.strategy.FilterStrategy;
import cn.fireface.call.webgraph.filter.strategy.factory.FilterStrategyFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Created by maoyi on 2018/10/26.
 * don't worry , be happy
 */
public class WebFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        if (servletRequest instanceof HttpServletRequest) {
            request = (HttpServletRequest) servletRequest;
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        String pathInfo = request.getPathInfo();
        String contextPath = request.getContextPath();
        String servletPath = request.getServletPath();
        String requestURI = request.getRequestURI();

        response.setCharacterEncoding("utf-8");

        String uriValue = requestURI.substring(10);

        if ("".equalsIgnoreCase(uriValue) || "/".equalsIgnoreCase(uriValue)) {
            response.sendRedirect("/callChain/resource/index.html");
            return;
        }

        if (contextPath == null) { // root context
            contextPath = "";
        }

        if (uriValue.contains(".json")) {
            Map<String, String[]> parameterMap = request.getParameterMap();
            FilterStrategy strategy = FilterStrategyFactory.produce(uriValue);
            String execute = null;
            if (null != strategy) {
                execute = strategy.execute(parameterMap);
            }
            response.getWriter().print(execute);
            return;
        }
        if ("".equals(servletPath) || null == servletPath) {
            response.sendRedirect("/callChain/resource/index.html");
            return;
        }

        returnResourceFile(uriValue.substring(uriValue.lastIndexOf("/")), uriValue, (HttpServletResponse) servletResponse);
    }


    protected static final String RESOURCE_PATH = "/resource";

    protected String getFilePath(String fileName) {
        return RESOURCE_PATH + fileName;
    }

    protected void returnResourceFile(String fileName, String uri, HttpServletResponse response)
            throws ServletException,
            IOException {

//        String filePath = getFilePath(fileName);
        String filePath = uri;
        if (!filePath.startsWith("/resource")) {
            filePath = getFilePath(filePath);
        }

        if (filePath.endsWith(".html")) {
            response.setContentType("text/html; charset=utf-8");
        }
        if (fileName.endsWith(".jpg")) {
            byte[] bytes = Utils.readByteArrayFromResource(filePath);
            if (bytes != null) {
                response.getOutputStream().write(bytes);
            }

            return;
        }

        String text = Utils.readFromResource(filePath);
        if (text == null) {
            response.sendRedirect(uri + "/index.html");
            return;
        }
        if (fileName.endsWith(".svg")) {
            response.setContentType("image/svg+xml");
        }
        if (fileName.endsWith(".css")) {
            response.setContentType("text/css;charset=utf-8");
        } else if (fileName.endsWith(".js")) {
            response.setContentType("text/javascript;charset=utf-8");
        }
        response.getWriter().write(text);
    }

    @Override
    public void destroy() {

    }
}
