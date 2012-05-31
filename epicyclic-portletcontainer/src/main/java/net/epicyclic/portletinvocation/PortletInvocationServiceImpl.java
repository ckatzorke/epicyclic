package net.epicyclic.portletinvocation;

import java.io.IOException;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.epicyclic.common.Commons;
import net.epicyclic.common.PageLayout;
import net.epicyclic.common.PortletIntegrationException;
import net.epicyclic.common.PortletIntegrationRuntimeException;
import net.epicyclic.common.PortletWindowDefinition;
import net.epicyclic.common.rendering.PortletRenderResult;
import net.epicyclic.portletcontainer.PortletContainerService;
import net.epicyclic.portletinvocation.action.GateinPortletExecutor;
import net.epicyclic.portletinvocation.interceptors.Interceptor;
import net.epicyclic.portletinvocation.rendering.GateinPortletRenderer;

import org.apache.commons.lang.StringUtils;
import org.gatein.common.io.IOTools;
import org.gatein.common.io.SerializationFilter;
import org.gatein.common.util.Base64;
import org.gatein.pc.api.info.PortletInfo;
import org.gatein.pc.controller.impl.ControllerRequestParameterNames;
import org.gatein.pc.controller.state.PortletPageNavigationalState;
import org.gatein.pc.controller.state.PortletPageNavigationalStateSerialization;
import org.springframework.util.Assert;

import com.jamonapi.Monitor;

@Named("portletInvocationService")
public class PortletInvocationServiceImpl implements PortletInvocationService {

	private final static Logger logger = Logger.getLogger(PortletInvocationServiceImpl.class.getCanonicalName());

	@Inject
	private PortletContainerService portletContainerService;

	@Inject
	private Collection<Interceptor> interceptors;

	@Override
	public PortletRenderResult render(HttpServletRequest request, HttpServletResponse response,
			PortletWindowDefinition portletWindowDefinition) {
		Assert.notNull(portletWindowDefinition);
		Assert.notNull(portletWindowDefinition.getWindowId());

		// check for resourcerequest
		if ("resource".equals(request.getParameter("phase"))) {
			return Commons.EMPTY_PORTLETRENDERRESULT;
		}
		PortletRenderResult result = null;
		PageLayout pageLayout = new PageLayout();
		pageLayout.addPortletWindowDefinition(portletWindowDefinition);
		try {
			EpicyclicPortletControllerContext portletControllerContext = new EpicyclicPortletControllerContext(
					portletContainerService, request, response, pageLayout);
			// TODO render not necessary if other portlet is maximized.
			// potential
			// performance optimization + auch in action phase!
			PortletInfo portletInfo = portletControllerContext.getPortletInfo(portletWindowDefinition.getWindowId());

			PortletPageNavigationalState portletPageNavigationalState = createPortletPageNavigationalState(request,
					portletControllerContext);

			if (portletInfo == null) {
				throw new PortletIntegrationException("Portlet not found! Check Deployment!");
			} else {
				invokeInterceptors(request);
				final GateinPortletRenderer portletRenderer = new GateinPortletRenderer(portletControllerContext,
						portletPageNavigationalState, portletInfo, portletWindowDefinition);
				Monitor renderMon = PortletInvocationUtils.createPortletMonitor(portletWindowDefinition, "render");
				try {
					result = portletRenderer.render();
				} finally {
					renderMon.stop();
				}
			}
		} catch (PortletIntegrationException e) {
			result = PortletInvocationUtils.handleException(portletWindowDefinition, e);
		} catch (IOException e) {
			result = PortletInvocationUtils.handleException(portletWindowDefinition, e);
		}
		return result;
	}

	@Override
	public void executePhase(HttpServletRequest request, HttpServletResponse response, PageLayout pageLayout) {
		if (pageLayout != null && pageLayout.getPortletWindowDefinitions() != null
				&& !pageLayout.getPortletWindowDefinitions().isEmpty()) {
			request.setAttribute(Commons.REQUEST_ATTR_PAGELAYOUT, pageLayout);
			try {
				EpicyclicPortletControllerContext portletControllerContext = new EpicyclicPortletControllerContext(
						portletContainerService, request, response, pageLayout);
				final GateinPortletExecutor executor = new GateinPortletExecutor(portletControllerContext);
				final PortletPageNavigationalState portletPageNavigationalState = createPortletPageNavigationalState(
						request, portletControllerContext);
				// interceptors
				invokeInterceptors(request);
				// portal phase
				PortletPageNavigationalState navigationalStateUpdate = executor
						.executeActionPhase(portletPageNavigationalState);
				request.setAttribute(Commons.REQUEST_ATTR_PAGENAVIGATIONALSTATE, navigationalStateUpdate);
			} catch (IOException e) {
				throw new PortletIntegrationRuntimeException("IOException occured!", e);
			} catch (ServletException e) {
				throw new PortletIntegrationRuntimeException("ServletException occured!", e);
			}

		}
	}

	private void invokeInterceptors(HttpServletRequest request) {
		if (interceptors != null) {
			for (Interceptor interceptor : interceptors) {
				try {
					interceptor.invoke(request);
				} catch (Exception e) {
					logger.log(Level.SEVERE, "Cannot invoke Interceptor " + interceptor, e);
				}
			}
		}
	}

	private PortletPageNavigationalState createPortletPageNavigationalState(HttpServletRequest request,
			EpicyclicPortletControllerContext portletControllerContext) {
		PortletPageNavigationalState portletPageNavigationalState = null;

		// check in request
		portletPageNavigationalState = (PortletPageNavigationalState) request
				.getAttribute(Commons.REQUEST_ATTR_PAGENAVIGATIONALSTATE);
		if (portletPageNavigationalState == null) {
			// create pagenavigationalstate if provided...
			String blah = request.getParameter(ControllerRequestParameterNames.PAGE_NAVIGATIONAL_STATE);
			if (!StringUtils.isEmpty(blah)) {
				PortletPageNavigationalStateSerialization serialization = new PortletPageNavigationalStateSerialization(
						portletControllerContext.getStateControllerContext());
				// The nav state provided with the request
				// Unmarshall portal navigational state if it is provided
				byte[] bytes = Base64.decode(blah, Base64.EncodingOption.USEURLSAFEENCODING);
				portletPageNavigationalState = IOTools
						.unserialize(serialization, SerializationFilter.COMPRESSOR, bytes);
			} else {
				// ... otherwise create default one
				portletPageNavigationalState = portletControllerContext.getStateControllerContext()
						.createPortletPageNavigationalState(true);
			}
		}
		request.setAttribute(Commons.REQUEST_ATTR_PAGENAVIGATIONALSTATE, portletPageNavigationalState);
		return portletPageNavigationalState;
	}

	public void setPortletContainerService(PortletContainerService portletContainerService) {
		this.portletContainerService = portletContainerService;
	}

	public void setInterceptors(Collection<Interceptor> interceptors) {
		this.interceptors = interceptors;
	}
}
