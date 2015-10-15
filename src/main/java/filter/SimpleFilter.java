package filter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.Principal;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

import javax.servlet.AsyncContext;
import javax.servlet.DispatcherType;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpUpgradeHandler;
import javax.servlet.http.Part;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.ProxyRouteLocator;
import org.springframework.cloud.netflix.zuul.filters.pre.PreDecorationFilter;
import org.springframework.cloud.netflix.zuul.filters.route.RibbonRoutingFilter;
import org.springframework.cloud.netflix.zuul.filters.route.SimpleHostRoutingFilter;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.util.UrlPathHelper;

import com.netflix.client.http.HttpRequest;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.constants.ZuulHeaders;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.filters.ZuulServletFilter;

public class SimpleFilter extends ZuulFilter{

	private StringRedisTemplate stringRedisTemplate;
	
	public  SimpleFilter(StringRedisTemplate redisTemplate) {
		super();
		stringRedisTemplate = redisTemplate;
		
	}

	
	@Override
	public Object run() {
		
		RequestContext ctx =  RequestContext.getCurrentContext();
		Cookie[] list = ctx.getRequest().getCookies();
		String serviceId = "";
		if(list != null)
		for(Cookie c : list)
		{
			if(c.getName().equalsIgnoreCase("version"))
			{
				serviceId = c.getValue();
			}
		}
		if(serviceId!= null && !serviceId.isEmpty())
			ctx.set("serviceId",serviceId);
//		ctx.addZuulRequestHeader("X-Forwarded-Prefix","/bookmark-service2" );
//		ctx.put("requestURI",ctx.getRequest().getRequestURI() );
//		System.out.println(ctx.getRouteHost());
//		String uri = ctx.getRequest().getRequestURI();
//		System.out.println(uri);
//		if(uri.contains("bookmark"))
//		{	
//			String service ="bookmark";
//			Integer startIndex = uri.indexOf("/", uri.indexOf(service))+1;
//			String dealerID = uri.substring(uri.indexOf("/", uri.indexOf(service))+1, uri.indexOf("/",startIndex));
//			String serviceId = getSrerviceIdFromRedis(dealerID, service);
//			if(serviceId != null && !serviceId.isEmpty())
//			{
//				try {
////					ctx.put("requestURI", uri.substring(startIndex-1, uri.length()));
////					ctx.put("proxy", "1");
//					ctx.set("serviceId",serviceId);
////					ctx.setRouteHost(null);
////					ctx.addOriginResponseHeader("X-Zuul-ServiceId","bookmark-service2");
////					ctx.addZuulRequestHeader(
////							"X-Forwarded-Host",
////							ctx.getRequest().getServerName() + ":"
////									+ String.valueOf(ctx.getRequest().getServerPort()));
////					ctx.addZuulRequestHeader(
////							ZuulHeaders.X_FORWARDED_PROTO,
////							ctx.getRequest().getScheme());
//		//			if (StringUtils.hasText(route.getPrefix())) {
//		//			ctx.addZuulRequestHeader("X-Forwarded-Prefix","/"+serviceId );
//				} catch (Exception e){
//					e.printStackTrace();
//				}
//			}
//		}
		

		return null;
	}

	@Override
	public boolean shouldFilter() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public int filterOrder() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String filterType() {
		// TODO Auto-generated method stub
		return "route";
	}
	
	private String getSrerviceIdFromRedis(String dealerID, String service){
		String result ="";
		try {
			result =(String) stringRedisTemplate.opsForHash().get("Dealer:"+service+":"+dealerID, "serviceId");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

}
