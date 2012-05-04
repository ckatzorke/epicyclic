package net.epicyclic.portletintegration.test;

import junit.framework.Assert;

import net.epicyclic.common.Commons;
import net.epicyclic.common.PortletWindowDefinition;
import net.epicyclic.common.rendering.PortletRenderErrorResult;
import net.epicyclic.common.rendering.PortletRenderResult;
import net.epicyclic.common.rendering.RenderType;

import org.junit.Test;


public class PortletRenderResultTest {
	@Test
	public void testPortletRenderResult() throws Exception {
		PortletRenderResult result = new PortletRenderResult();

		Assert.assertNotNull(result.getFragment());
		Assert.assertEquals(RenderType.HTML, result.getRenderType());

		Assert.assertEquals("text/html", result.getRenderType().getMediaType());

		System.out.println(result.toString());

		result.setFragment(new StringBuffer("blablubb"));
		Assert.assertNotNull(result.getFragment());
		Assert.assertEquals("blablubb", result.getFragment().toString());
	}

	@Test
	public void testPortletRenderErrorResult() throws Exception {
		Throwable cause = new NullPointerException();
		PortletWindowDefinition portletWindowDefinition = Commons.PORTLETWINDOWDEFINITION_UNKNOWN;
		PortletRenderErrorResult error = new PortletRenderErrorResult(
				portletWindowDefinition, cause);

		Assert.assertEquals(NullPointerException.class, error.getCause()
				.getClass());
		Assert.assertNotNull(error.getErrorId());

		System.out.println(error);

		error = new PortletRenderErrorResult(portletWindowDefinition, null);
	}

	@Test
	public void testRenderType() throws Exception {
		RenderType type = RenderType.getRenderTypeForMediaType("text/html");
		Assert.assertEquals(RenderType.HTML, type);
		Assert.assertEquals("htmlPortlet", type.getPortletType());

		Assert.assertTrue(RenderType.XML.getRanking() > RenderType.HTML
				.getRanking());
	}

}
