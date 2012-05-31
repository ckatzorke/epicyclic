package net.epicyclic.server.rest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.epicyclic.common.Commons;
import net.epicyclic.common.PageLayout;
import net.epicyclic.common.PortletWindowDefinition;
import net.epicyclic.common.rendering.PortletRenderResult;
import net.epicyclic.portletcontainer.PortletContainerService;
import net.epicyclic.portletinvocation.PortletInvocationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class EpicyclicRestController {

	@Autowired
	private PortletContainerService portletContainerService;

	@Autowired
	private PortletInvocationService portletInvocationService;

	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public @ResponseBody
	String test(@RequestParam("epi:app") String app, @RequestParam("epi:portlet") String portletName,
			@RequestParam("epi:windowid") String windowId, HttpServletRequest request, HttpServletResponse response) {
		PortletWindowDefinition def = new PortletWindowDefinition(portletName, app, windowId, null);
		PageLayout pageLayout = new PageLayout();
		pageLayout.addPortletWindowDefinition(def);
		request.setAttribute(Commons.REQUEST_ATTR_PAGELAYOUT, pageLayout);
		PortletRenderResult renderResult = portletInvocationService.render(request, response, def);
		return renderResult.getFragment().toString();
	}
}