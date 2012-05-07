package net.epicyclic.common;

import java.util.List;

import javax.portlet.PortletContext;

import org.apache.commons.lang.StringUtils;

/**
 * Defines a portlet window by specifying:
 * <ul>
 * <li>handle (local or producerid)</li>
 * <li>portletName</li>
 * <li>applicationName (uri)</li>
 * <li>preferences</li>
 * </ul>
 * taking this information from content xml
 * 
 */
public class PortletWindowDefinition {

   public static final String LOCAL_PORTLETHANDLE = "local";

   private final String handle;

   private final String portletName;

   private final String applicationName;

   private final String windowId;

   /**
    * the custom preferences for this window. If null, the generic preferencesinfo from the
    * portletinfo is used
    * */
   private final List<Preference> customPreferences;

   private final String compoundId;

   public PortletWindowDefinition(String portletName, String applicationName, String windowId, List<Preference> preferences) {
      this(LOCAL_PORTLETHANDLE, portletName, applicationName, windowId, preferences);
   }

   public PortletWindowDefinition(String handle, String portletName, String applicationName, String windowId,
         List<Preference> preferences) {
      if (StringUtils.isEmpty(handle)) {
         this.handle = LOCAL_PORTLETHANDLE;
      }
      else {
         this.handle = handle;
      }
      this.portletName = portletName;
      this.applicationName = applicationName;
      this.windowId = windowId;
      this.customPreferences = preferences;
      this.compoundId = (this.handle.equals(LOCAL_PORTLETHANDLE) ? "" : this.handle + ".") + "/" + this.applicationName + "."
            + this.portletName;
   }

   public String getHandle() {
      return handle;
   }

   public String getPortletName() {
      return portletName;
   }

   public String getApplicationName() {
      return applicationName;
   }

   public String getWindowId() {
      return windowId;
   }

   public List<Preference> getPreferencesInfo() {
      return customPreferences;
   }

   /**
    * the protletcontextId for this portlet reflected by the {@link PortletWindowDefinition}, e.g.
    * <ul>
    * <li>local./testapp.TestPortlet</li>
    * <li>local./idm.Login</li>
    * <li>producer1./app1.Portlet1</li>
    * </ul>
    * This id is used to identify the portlet in the list of available portlets in the invoker stack, and reflects local and remote
    * portlets.
    * 
    * @return the portletid used in {@link PortletContext}
    */
   public String getPortletContextId() {
      return this.compoundId;
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((applicationName == null) ? 0 : applicationName.hashCode());
      result = prime * result + ((customPreferences == null) ? 0 : customPreferences.hashCode());
      result = prime * result + ((handle == null) ? 0 : handle.hashCode());
      result = prime * result + ((portletName == null) ? 0 : portletName.hashCode());
      result = prime * result + ((windowId == null) ? 0 : windowId.hashCode());
      return result;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      }
      if (obj == null) {
         return false;
      }
      if (getClass() != obj.getClass()) {
         return false;
      }
      PortletWindowDefinition other = (PortletWindowDefinition) obj;
      if (applicationName == null) {
         if (other.applicationName != null) {
            return false;
         }
      }
      else if (!applicationName.equals(other.applicationName)) {
         return false;
      }
      if (customPreferences == null) {
         if (other.customPreferences != null) {
            return false;
         }
      }
      else if (!customPreferences.equals(other.customPreferences)) {
         return false;
      }
      if (handle == null) {
         if (other.handle != null) {
            return false;
         }
      }
      else if (!handle.equals(other.handle)) {
         return false;
      }
      if (portletName == null) {
         if (other.portletName != null) {
            return false;
         }
      }
      else if (!portletName.equals(other.portletName)) {
         return false;
      }
      if (windowId == null) {
         if (other.windowId != null) {
            return false;
         }
      }
      else if (!windowId.equals(other.windowId)) {
         return false;
      }
      return true;
   }

   @Override
   public String toString() {
      return "PortletWindowDefinition [handle=" + getHandle() + ", portletName=" + getPortletName() + ", applicationName="
            + getApplicationName() + ", windowId=" + getWindowId() + ", customPreferences=" + getPreferencesInfo() + "]";
   }

}