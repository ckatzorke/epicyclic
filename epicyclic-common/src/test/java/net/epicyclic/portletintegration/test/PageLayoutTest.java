package net.epicyclic.portletintegration.test;

import net.epicyclic.common.PageLayout;
import net.epicyclic.common.PortletWindowDefinition;

import org.junit.Test;

import junit.framework.Assert;


public class PageLayoutTest {

   @Test
   public void testPageLayoutAdd() throws Exception {
      PortletWindowDefinition pwd1 = new PortletWindowDefinition("portlet", "app", "window", null);
      PageLayout layout = new PageLayout();
      Assert.assertNotNull(layout.getPortletWindowDefinitions());
      Assert.assertEquals(0, layout.getPortletWindowDefinitions().size());
      layout.addPortletWindowDefinition(pwd1);
      Assert.assertEquals(1, layout.getPortletWindowDefinitions().size());
      Assert.assertTrue(layout.getPortletWindowDefinitions().contains(pwd1));

   }
   @Test
   public void testPageLayoutFind() throws Exception {
      PortletWindowDefinition pwd1 = new PortletWindowDefinition("portlet", "app", "window", null);
      PageLayout layout = new PageLayout();
      layout.addPortletWindowDefinition(pwd1);
      
      PortletWindowDefinition findPortletWindowDefinition = layout.findPortletWindowDefinition("window");
      Assert.assertNotNull(findPortletWindowDefinition);
      
      findPortletWindowDefinition = layout.findPortletWindowDefinition("grmpf");
      Assert.assertNull(findPortletWindowDefinition);
      
      System.out.println(layout);

   }
}
