package net.epicyclic.portletcontainer.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import net.epicyclic.common.PortletWindowDefinition;
import net.epicyclic.portletcontainer.PortletContainerService;

import org.gatein.pc.api.Portlet;
import org.gatein.pc.api.PortletInvoker;
import org.gatein.pc.api.PortletInvokerException;

@Named("portletContainerService")
public class PortletContainerServiceImpl implements PortletContainerService {

	private final static Logger logger = Logger.getLogger(PortletContainerServiceImpl.class.getCanonicalName());
	private final static String LOG_CLASS = PortletContainerServiceImpl.class.getName();

	@Inject
	@Named("ConsumerPortletInvoker")
	private PortletInvoker portletInvoker;

	/** getter/setter */

	public void setPortletInvoker(PortletInvoker portletInvoker) {
		this.portletInvoker = portletInvoker;
	}

	@Override
	public PortletInvoker getPortletInvoker() {
		return this.portletInvoker;
	}

	@Override
	public Map<String, Portlet> getAvailablePortlets() {
		// this.consumerRegistryHandler.initConsumers();
		// TODO cache and reload asynchronously
		Map<String, Portlet> availportlets = new HashMap<String, Portlet>();
		try {
			Set<Portlet> portlets = portletInvoker.getPortlets();
			for (Portlet portlet : portlets) {
				String id = getStorageCompundId(portlet);
				availportlets.put(id, portlet);
			}
		} catch (PortletInvokerException e) {
			logger.log(Level.SEVERE, "Cannot get list of available portlets!", e);
		}
		return availportlets;
	}

	private String getStorageCompundId(Portlet portlet) {
		String id = portlet.getContext().getId();
		if (id.startsWith("local.")) {
			id = id.substring(id.indexOf('/'));
		}
		return id;
	}

	@Override
	public Portlet findPortlet(PortletWindowDefinition pwd) {
		return getAvailablePortlets().get(pwd.getPortletContextId());
	}

	@PostConstruct
	public void afterPropertiesSet() throws Exception {
		logger.log(Level.INFO, "*************************************************");
		logger.log(Level.INFO, "* Initializing " + LOG_CLASS);
		logger.log(Level.INFO, "* PortletContainerService is starting. Initializing local portletInvoker...");
		// TODO local portlets

		logger.log(Level.INFO, "* PortletContainerService started...");
		logger.log(Level.INFO, "*************************************************");
	}

}
