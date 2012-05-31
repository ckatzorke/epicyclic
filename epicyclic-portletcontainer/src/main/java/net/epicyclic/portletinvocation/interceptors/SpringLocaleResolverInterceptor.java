package net.epicyclic.portletinvocation.interceptors;

import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

/**
 * When invoking the portlet from a spring dispatcher servlet, the portlet finds
 * a LocaleResolver in the request with the attribute that is of course not class cast compatible due to different classloaders
 * "org.springframework.web.servlet.DispatcherServlet.LOCALE_RESOLVER" See also
 * https://jira.springsource.org/browse/SPR-9207
 */
@Named("springLocaleInterceptor")
public class SpringLocaleResolverInterceptor implements Interceptor {

	@Override
	public void invoke(HttpServletRequest request) {
		request.removeAttribute("org.springframework.web.servlet.DispatcherServlet.LOCALE_RESOLVER");
	}

}
