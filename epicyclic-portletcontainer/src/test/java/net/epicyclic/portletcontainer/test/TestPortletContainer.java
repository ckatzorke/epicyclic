package net.epicyclic.portletcontainer.test;

import java.util.Map;

import net.epicyclic.portletcontainer.PortletContainerService;

import org.gatein.pc.api.Portlet;
import org.gatein.pc.api.PortletInvoker;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/test-portletcontainer.xml")
public class TestPortletContainer {
	@Autowired
	PortletContainerService portletContainerService;

	@Before
	public void beforeClass() {
	}

	@Test
	public void testPortletContainerServiceUp() throws Exception {
		Assert.assertNotNull(portletContainerService);
		PortletInvoker portletInvoker = portletContainerService.getPortletInvoker();
		Assert.assertNotNull(portletInvoker);
	}

	@Test
	public void testLocalPortlets() throws Exception {
		Map<String, Portlet> localPortlets = portletContainerService.getAvailablePortlets();
		Assert.assertEquals(0, localPortlets.size());
	}

}
