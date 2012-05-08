package net.epicyclic.portletinvocation;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.epicyclic.common.PageLayout;
import net.epicyclic.common.PortletWindowDefinition;
import net.epicyclic.events.EpicyclicEventControllerContext;
import net.epicyclic.portletcontainer.PortletContainerService;

import org.gatein.common.io.Serialization;
import org.gatein.pc.api.Portlet;
import org.gatein.pc.api.PortletInvokerException;
import org.gatein.pc.api.invocation.PortletInvocation;
import org.gatein.pc.api.invocation.response.PortletInvocationResponse;
import org.gatein.pc.controller.PortletControllerContext;
import org.gatein.pc.controller.event.EventControllerContext;
import org.gatein.pc.controller.impl.AbstractPortletControllerContext;
import org.gatein.pc.controller.impl.state.StateControllerContextImpl;
import org.gatein.pc.controller.state.PortletPageNavigationalState;
import org.gatein.pc.controller.state.PortletPageNavigationalStateSerialization;
import org.gatein.pc.controller.state.StateControllerContext;

/**
 * {@link PortletControllerContext} needed for gatein-pc. This is an internal
 * representation, and should not be used externally! Use the
 * {@link PortletInvocationServiceImpl} for invoking portlets!
 * 
 * @author ckatzorke
 * 
 */
public class EpicyclicPortletControllerContext extends AbstractPortletControllerContext {

	private PageLayout pageLayout;

	private final PortletContainerService portletContainerService;

	private final EpicyclicEventControllerContext eventControllerContext;
	private final StateControllerContext stateControllerContext;
	private final Serialization<PortletPageNavigationalState> serialization;

	public EpicyclicPortletControllerContext(PortletContainerService portletContainerService, HttpServletRequest request,
			HttpServletResponse response, PageLayout pageLayout) throws IOException {
		super(request, response);
		this.portletContainerService = portletContainerService;
		this.pageLayout = pageLayout;
		this.eventControllerContext = new EpicyclicEventControllerContext(this);
		this.stateControllerContext = new StateControllerContextImpl(this);
		this.serialization = new PortletPageNavigationalStateSerialization(stateControllerContext);
	}

	@Override
	public EventControllerContext getEventControllerContext() {
		return eventControllerContext;
	}

	@Override
	public StateControllerContext getStateControllerContext() {
		return stateControllerContext;
	}

	@Override
	protected Portlet getPortlet(String windowId) throws PortletInvokerException {
		PortletWindowDefinition portletWindowDefinition = pageLayout.findPortletWindowDefinition(windowId);
		Portlet portlet = null;
		if (portletWindowDefinition != null) {
			portlet = portletContainerService.findPortlet(portletWindowDefinition);
		}
		if (portlet == null) {
			throw new PortletInvokerException("Cannot find portlet with windowId " + windowId);
		}
		return portlet;
	}

	@Override
	protected PortletInvocationResponse invoke(PortletInvocation invocation) throws PortletInvokerException {
		return portletContainerService.getPortletInvoker().invoke(invocation);
	}

	@Override
	protected Serialization<PortletPageNavigationalState> getPageNavigationalStateSerialization() {
		return serialization;
	}

	public PageLayout getPageLayout() {
		return pageLayout;
	}

}
