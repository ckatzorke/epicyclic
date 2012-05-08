package net.epicyclic.portletinvocation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.epicyclic.common.PageLayout;
import net.epicyclic.common.PortletWindowDefinition;
import net.epicyclic.common.rendering.PortletRenderResult;

/**
 * Central portletInvocationService, that can be used from external components
 * (like chopes-pipeline).
 * 
 * @author ckatzorke
 * 
 */
public interface PortletInvocationService {

	/**
	 * Renders a portlet with given parameters
	 * 
	 * @param request
	 *            the web request
	 * @param response
	 *            the web response
	 * @param portletWindowDefinition
	 *            the {@link PortletWindowDefinition} of the portlet to be
	 *            invoked.
	 * @return
	 */
	public abstract PortletRenderResult render(HttpServletRequest request, HttpServletResponse response,
			PortletWindowDefinition portletWindowDefinition);

	/**
	 * Executes the action/event phase of the portal. The requested action (if
	 * any) of a portlet is invoked, any possible events are routed.
	 * 
	 * @param request
	 *            the web request
	 * @param response
	 *            the web response
	 * @param pageLayout
	 *            the {@link PageLayout} of the portal page, needed to identify
	 *            the correct state (portlet is on page) and to rout the events.
	 */
	public abstract void executePhase(HttpServletRequest request, HttpServletResponse response, PageLayout pageLayout);

}