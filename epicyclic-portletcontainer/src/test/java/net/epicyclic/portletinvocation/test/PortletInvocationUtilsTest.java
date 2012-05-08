package net.epicyclic.portletinvocation.test;

import junit.framework.Assert;
import net.epicyclic.common.Commons;
import net.epicyclic.common.PortletWindowDefinition;
import net.epicyclic.common.rendering.PortletRenderErrorResult;
import net.epicyclic.portletinvocation.PortletInvocationUtils;

import org.junit.Test;

import com.jamonapi.Monitor;

public class PortletInvocationUtilsTest {

   @Test
   public void testHandleException() throws Exception {
      PortletWindowDefinition pwd = new PortletWindowDefinition("portlet", "app", "w", null);
      Exception e = new RuntimeException();
      PortletRenderErrorResult handleException = PortletInvocationUtils.handleException(pwd, e);
      Assert.assertNotNull(handleException);
      Assert.assertEquals(e, handleException.getCause());

   }

   @Test(expected = NullPointerException.class)
   public void testMonitor() throws Exception {

      PortletInvocationUtils.createPortletMonitor(null, null);
   }

   @Test
   public void testMonitorSuccess() throws Exception {
      String phase = "action";
      PortletWindowDefinition portletWindowDefinition = Commons.PORTLETWINDOWDEFINITION_UNKNOWN;
      Monitor monitor = PortletInvocationUtils.createPortletMonitor(portletWindowDefinition, phase);
      Assert.assertNotNull(monitor);
      Assert.assertTrue(monitor.getLabel().contains("[action]"));

   }
}
