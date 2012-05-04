package net.epicyclic.portletinvocation;

import java.util.logging.Level;
import java.util.logging.Logger;

import net.epicyclic.common.PortletWindowDefinition;
import net.epicyclic.common.rendering.PortletRenderErrorResult;

import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;

public final class PortletInvocationUtils {

	private final static Logger logger = Logger.getLogger(PortletInvocationUtils.class.getCanonicalName());

	private PortletInvocationUtils() {
	}

	public static PortletRenderErrorResult handleException(PortletWindowDefinition portletWindowDefinition, Throwable e) {
		PortletRenderErrorResult result = new PortletRenderErrorResult(portletWindowDefinition, e);
		logger.log(Level.SEVERE, result.getErrorId() + " Portlet " + portletWindowDefinition.getPortletName()
				+ " had exception [" + e.getMessage() + "].", e);
		return result;
	}

	public static Monitor createPortletMonitor(PortletWindowDefinition portletWindowDefinition, String phase) {
		Monitor renderMon = MonitorFactory.start("gears-portlet: " + portletWindowDefinition.getPortletContextId()
				+ ", phase [" + phase + "]");
		return renderMon;
	}

}
