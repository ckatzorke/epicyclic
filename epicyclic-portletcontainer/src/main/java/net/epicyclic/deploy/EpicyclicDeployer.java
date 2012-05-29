package net.epicyclic.deploy;

import org.gatein.wci.WebApp;

public class EpicyclicDeployer extends org.gatein.pc.portlet.impl.deployment.PortletApplicationDeployer {

	@Override
	protected void add(WebApp webApp) {
		System.out.println(">>>>>>before add");
		super.add(webApp);
		System.out.println(">>>>>>after add");
	}

}
