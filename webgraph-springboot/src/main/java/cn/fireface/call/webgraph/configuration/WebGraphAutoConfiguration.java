package cn.fireface.call.webgraph.configuration;

import cn.fireface.call.webgraph.filter.WebFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.DispatcherType;
import java.util.EnumSet;

/**
 * Created by maoyi on 2018/10/29.
 * don't worry , be happy
 */
@Configuration
public class WebGraphAutoConfiguration {

    /**
     * 注册一个Druid内置的StatViewServlet，用于展示Druid的统计信息。
     */
    @Bean
    FilterRegistrationBean callChainFilterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new WebFilter());
        filterRegistrationBean.addUrlPatterns("/callChain/*");
        EnumSet<DispatcherType> dispatcherTypes = EnumSet.allOf(DispatcherType.class);
        filterRegistrationBean.setDispatcherTypes(dispatcherTypes);
        return filterRegistrationBean;
    }

//    @Bean
//    ServletRegistrationBean servletRegistrationBean(){
//        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(),"/brandsCoupon/*");
//        Map<String, String> initParameters = new HashMap<String, String>();
//        initParameters.put(StatViewServlet.PARAM_NAME_USERNAME, "root");// 用户名
//        initParameters.put(StatViewServlet.PARAM_NAME_PASSWORD, "root");// 密码
//        initParameters.put(StatViewServlet.PARAM_NAME_RESET_ENABLE, "false");// 禁用HTML页面上的“Reset All”功能
//        initParameters.put(StatViewServlet.PARAM_NAME_ALLOW, ""); // IP白名单 (没有配置或者为空，则允许所有访问)
//        //initParameters.put(StatViewServlet.PARAM_NAME_DENY, "192.168.20.38");// IP黑名单 (存在共同时，deny优先于allow)
//        servletRegistrationBean.setInitParameters(initParameters);
//        return servletRegistrationBean;
//    }

    /**
     * 注册一个：filterRegistrationBean,添加请求过滤规则
     */
//    @Bean
//    FilterRegistrationBean filterRegistrationBean(){
//        WebStatFilter wefStatFilter = new WebStatFilter();
//        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
//        filterRegistrationBean.setFilter(wefStatFilter);
//        filterRegistrationBean.addUrlPatterns("/*");
//        Map<String,String> paramMap = new HashMap<>();
//        paramMap.put(WebStatFilter.PARAM_NAME_EXCLUSIONS,"*.js,*.gif,*.jpg,*.bmp,*.png,*.css,*.ico,/druid/*");
//        filterRegistrationBean.setInitParameters(paramMap);
//        return filterRegistrationBean;
//    }

    /**
     * 监听Spring
     *  1.定义拦截器
     * @return
     */
//    @Bean
//    public DruidStatInterceptor druidStatInterceptor(){
//        return new DruidStatInterceptor();
//    }
    /**
     * 监听Spring
     *  2.定义切入点
     * @return
     */
//    @Bean
//    public JdkRegexpMethodPointcut druidStatPointcut(){
//        JdkRegexpMethodPointcut druidStatPointcut = new JdkRegexpMethodPointcut();
//        String patterns = "com.jd.brands.*";
////        String patterns2 = "com.jd.brands.biz.*.dao.*,com.jd.pop.brand.api.mapper.*";
//        druidStatPointcut.setPatterns(patterns);
//        return druidStatPointcut;
//    }
    /**
     * 监听Spring
     *  3.定义通知类
     * @return
     */
//    @Bean
//    public Advisor druidStatAdvisor() {
//        return new DefaultPointcutAdvisor(druidStatPointcut(), druidStatInterceptor());
//    }
}
