package net.epicyclic.portletintegration.test;

import net.epicyclic.common.PortletIntegrationException;
import net.epicyclic.common.PortletIntegrationRuntimeException;

import org.junit.Test;

import junit.framework.Assert;


public class PortletIntegrationExceptionTest {

   @Test
   public void testExc() throws Exception {
      PortletIntegrationException e = new PortletIntegrationException();
      Assert.assertTrue(e instanceof Exception);

      PortletIntegrationRuntimeException e2 = new PortletIntegrationRuntimeException();
      Assert.assertTrue(e2 instanceof RuntimeException);
   }

}
