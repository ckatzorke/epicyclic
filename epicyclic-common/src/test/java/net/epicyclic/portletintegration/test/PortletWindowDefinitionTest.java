package net.epicyclic.portletintegration.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import net.epicyclic.common.PortletWindowDefinition;
import net.epicyclic.common.Preference;

import org.junit.Test;


public class PortletWindowDefinitionTest {

   @Test
   public void testPwd() throws Exception {
      List<Preference> preferences = new ArrayList<Preference>();
      preferences.add(new Preference("test", "1", "2"));
      Preference p = new Preference();
      p.setName("test2");
      p.setValues(Arrays.asList("1"));
      Assert.assertEquals("test2", p.getName());
      Assert.assertTrue(p.getValues().contains("1"));
      preferences.add(p);
      PortletWindowDefinition pwd = new PortletWindowDefinition("portlet", "app", "window", preferences);
      Assert.assertNotNull(pwd.getHandle());
      Assert.assertEquals("local", pwd.getHandle());
      Assert.assertEquals(2, pwd.getPreferencesInfo().size());

      System.out.println(pwd.toString());
      System.out.println(pwd.hashCode());

   }

   @Test
   public void testPwdLocal() throws Exception {
      List<Preference> preferences = new ArrayList<Preference>();
      preferences.add(new Preference("test", "1", "2"));
      PortletWindowDefinition pwd = new PortletWindowDefinition(null, "portlet", "app", "window", preferences);
      Assert.assertNotNull(pwd.getHandle());
      Assert.assertEquals("local", pwd.getHandle());

   }

   @Test
   public void testGetPortletContextId() throws Exception {
      PortletWindowDefinition pwd = new PortletWindowDefinition("local", "portlet", "app", "window", null);
      Assert.assertNotNull(pwd.getPortletContextId());
      Assert.assertEquals("/app.portlet", pwd.getPortletContextId());

      PortletWindowDefinition pwd2 = new PortletWindowDefinition("remote", "portlet", "app", "window", null);
      Assert.assertNotNull(pwd2.getPortletContextId());
      Assert.assertEquals("remote./app.portlet", pwd2.getPortletContextId());
   }

   @Test
   public void testEquals() throws Exception {
      PortletWindowDefinition pwd = new PortletWindowDefinition("local", "portlet", "app", "window", null);

      PortletWindowDefinition pwd2 = new PortletWindowDefinition("remote", "portlet", "app", "window", null);

      Assert.assertNotSame(pwd, pwd2);

      pwd2 = new PortletWindowDefinition("local", "portlet", "app", "window", null);

      Assert.assertEquals(pwd, pwd2);
   }

}
