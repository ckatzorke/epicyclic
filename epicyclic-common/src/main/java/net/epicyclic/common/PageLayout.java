package net.epicyclic.common;

import java.util.ArrayList;
import java.util.List;

/**
 * Defines the page layout, i.e. which portlets do reside on the page
 * 
 * @author ckatzorke
 * 
 */
public class PageLayout {

   private List<PortletWindowDefinition> portletWindowDefinitions = new ArrayList<PortletWindowDefinition>();

   public List<PortletWindowDefinition> getPortletWindowDefinitions() {
      return portletWindowDefinitions;
   }

   public void addPortletWindowDefinition(PortletWindowDefinition pwd) {
      portletWindowDefinitions.add(pwd);
   }

   public PortletWindowDefinition findPortletWindowDefinition(String windowId) {
      PortletWindowDefinition ret = null;
      for (PortletWindowDefinition pwd : portletWindowDefinitions) {
         if (pwd.getWindowId().equals(windowId)) {
            ret = pwd;
            break;
         }
      }
      return ret;
   }

   @Override
   public String toString() {
      return "PageLayout [portletWindowDefinitions=" + portletWindowDefinitions + "]";
   }

   
}
