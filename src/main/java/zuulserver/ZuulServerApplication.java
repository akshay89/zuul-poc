package zuulserver;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;

import filter.HeaderUrlRewritingFilter;
import filter.SimpleFilter;

/**
 * @author Spencer Gibb
 */
@Configuration
@ComponentScan
@EnableAutoConfiguration
@Controller
@EnableZuulProxy
public class ZuulServerApplication {
	
    public static void main(String[] args) {
        new SpringApplicationBuilder(ZuulServerApplication.class).web(true).run(args);
    }
    
    @Bean
    public SimpleFilter simpleFilter(){
    	ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
    	StringRedisTemplate template = (StringRedisTemplate) context.getBean("stringRedisTemplate");
    	return new SimpleFilter(template);
    }
    


}
