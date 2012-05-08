package net.epicyclic.server.rest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.epicyclic.common.PortletWindowDefinition;
import net.epicyclic.common.rendering.PortletRenderResult;
import net.epicyclic.portletcontainer.PortletContainerService;
import net.epicyclic.portletinvocation.PortletInvocationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class EpicyclicRestController {
	
	@Autowired
	private PortletContainerService portletContainerService;
	
	@Autowired
	private PortletInvocationService portletInvocationService;
	
	
	@RequestMapping("/test")
	public @ResponseBody String test(HttpServletRequest request, HttpServletResponse response) {
		PortletWindowDefinition def = new PortletWindowDefinition("HelloWorld", "debug-wsrp-portlets", "test", null);
		PortletRenderResult renderResult = portletInvocationService.render(request, response, def);
		return renderResult.getFragment().toString();
	}
}