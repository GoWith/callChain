package cn.fireface.call.webgraph.filter;

import cn.fireface.call.core.utils.LogPool;
import cn.fireface.call.webgraph.filter.strategy.FilterStrategy;
import cn.fireface.call.webgraph.filter.strategy.factory.FilterStrategyFactory;
import com.alibaba.fastjson.JSON;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;

/**
 * web过滤器
 * Created by maoyi on 2018/10/26.
 * don't worry , be happy
 *
 * @author maoyi
 * @date 2023/05/17
 */
public class WebFilter implements Filter {

    /**
     * 属性
     */
    public static final String PROPERTIES = "WEB-INF/call/chain/config.properties";
    /**
     * 调用链排除键
     */
    public static final String CALL_CHAIN_EXCLUDE_KEYS = "call.chain.exclude.keys";
    /**
     * 正则表达式
     */
    public static final String REGEX = ",";
    /**
     * 国旗
     */
    public static final String FLAG = "/";
    /**
     * json
     */
    public static final String JSON = ".json";
    /**
     * 资源
     */
    public static final String RESOURCE = "/resource";
    /**
     * html
     */
    public static final String HTML = ".html";
    /**
     * jpg
     */
    public static final String JPG = ".jpg";
    /**
     * 指数html
     */
    public static final String INDEX_HTML = "/index.html";
    /**
     * svg
     */
    public static final String SVG = ".svg";
    /**
     * svg图像xml
     */
    public static final String IMAGE_SVG_XML = "image/svg+xml";
    /**
     * css
     */
    public static final String CSS = ".css";
    /**
     * 文本css字符集utf 8
     */
    public static final String TEXT_CSS_CHARSET_UTF_8 = "text/css;charset=utf-8";
    /**
     * js
     */
    public static final String JS = ".js";
    /**
     * 文本javascript字符集utf 8
     */
    public static final String TEXT_JAVASCRIPT_CHARSET_UTF_8 = "text/javascript;charset=utf-8";

    /**
     * 初始化
     *
     * @param filterConfig 过滤器配置
     * @throws ServletException servlet异常
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(PROPERTIES)) {
        if (inputStream != null) {
            Properties properties = new Properties();
            properties.load(inputStream);
            System.out.println(com.alibaba.fastjson.JSON.toJSONString(properties));
            String excludeKeys = properties.getProperty(CALL_CHAIN_EXCLUDE_KEYS);
            System.out.println(excludeKeys);
            LogPool.addExcludes(Arrays.asList(excludeKeys.split(REGEX)));
        }
    } catch (IOException exception) {
        exception.printStackTrace();
    }
}


    /**
     * 做过滤器
     *
     * @param servletRequest  servlet请求
     * @param servletResponse servlet响应
     * @param filterChain     过滤器链
     * @throws IOException      ioexception
     * @throws ServletException servlet异常
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (!(servletRequest instanceof HttpServletRequest) || !(servletResponse instanceof HttpServletResponse)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String uriValue = request.getRequestURI().substring(request.getContextPath().length() + 10);

        if ("".equals(uriValue) || uriValue.equals(FLAG)) {
            response.sendRedirect("/callChain/resource/index.html");
            return;
        }

        if (uriValue.contains(JSON)) {
            Map<String, String[]> parameterMap = request.getParameterMap();
            FilterStrategy strategy = FilterStrategyFactory.produce(uriValue);
            String execute = strategy != null ? strategy.execute(parameterMap) : null;
            response.getWriter().print(execute);
            return;
        }

        if (request.getServletPath() == null || request.getServletPath().equals("")) {
            response.sendRedirect("/callChain/resource/index.html");
            return;
        }

        returnResourceFile(uriValue.substring(uriValue.lastIndexOf(FLAG) + 1), uriValue, response);
    }


    /**
     * 资源路径
     */
    protected static final String RESOURCE_PATH = "/resource";

    /**
     * 获取文件路径
     *
     * @param fileName 文件名称
     * @return {@link String}
     */
    protected String getFilePath(String fileName) {
        return RESOURCE_PATH + fileName;
    }

    /**
     * 返回资源文件
     *
     * @param fileName 文件名称
     * @param uri      uri
     * @param response 响应
     * @throws IOException ioexception
     */
    protected void returnResourceFile(String fileName, String uri, HttpServletResponse response)
            throws IOException {
        String filePath = uri;
        if (!filePath.startsWith(RESOURCE)) {
            filePath = getFilePath(fileName);
        }

        if (filePath.endsWith(HTML)) {
            response.setContentType("text/html; charset=utf-8");
        } else if (fileName.endsWith(JPG)) {
            byte[] bytes = Utils.readByteArrayFromResource(filePath);
            if (bytes != null) {
                response.getOutputStream().write(bytes);
            }
        } else {
            String text = Utils.readFromResource(filePath);
            if (text == null) {
                response.sendRedirect(uri + INDEX_HTML);
                return;
            }
            if (fileName.endsWith(SVG)) {
                response.setContentType(IMAGE_SVG_XML);
            } else if (fileName.endsWith(CSS)) {
                response.setContentType(TEXT_CSS_CHARSET_UTF_8);
            } else if (fileName.endsWith(JS)) {
                response.setContentType(TEXT_JAVASCRIPT_CHARSET_UTF_8);
            }
            response.getWriter().write(text);
        }
    }

    /**
     * 摧毁
     */
    @Override
    public void destroy() {

    }
}
