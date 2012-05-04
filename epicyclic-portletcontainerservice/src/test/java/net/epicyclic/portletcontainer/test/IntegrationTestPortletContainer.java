package net.epicyclic.portletcontainer.test;

import net.epicyclic.portletcontainer.PortletContainerService;

import org.gatein.pc.api.PortletInvoker;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:integrationtest-portletcontainer.xml")
public class IntegrationTestPortletContainer {

	@Autowired
	PortletContainerService portletContainerService;

	@Test
	public void testPortletContainerServiceUp() throws Exception {
		Assert.assertNotNull(portletContainerService);
		PortletInvoker portletInvoker = portletContainerService.getPortletInvoker();
		Assert.assertNotNull(portletInvoker);
	}

}
