package net.epicyclic.portletcontainer;

import java.util.Map;

import net.epicyclic.common.PortletWindowDefinition;

import org.gatein.pc.api.Portlet;
import org.gatein.pc.api.PortletContext;
import org.gatein.pc.api.PortletInvoker;

public interface PortletContainerService {

	/**
	 * @return the portlet invoker. will be of type
	 *         {@link FederatingPortletInvoker} to dispatch the calls to local
	 *         or remote portlet containers
	 */
	public abstract PortletInvoker getPortletInvoker();

	/**
	 * @return all avalailable portlets, the id is the id from the
	 *         {@link PortletContext}, e.g. local/appname.PortletName, or
	 *         ow0006/appname.PortletName
	 */
	public abstract Map<String, Portlet> getAvailablePortlets();

	/**
	 * @param pwd
	 * @return
	 */
	public abstract Portlet findPortlet(PortletWindowDefinition pwd);

}