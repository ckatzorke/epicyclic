package net.epicyclic.common;

import net.epicyclic.common.rendering.PortletRenderResult;

public interface Commons {

   public static final String REQUEST_ATTR_PAGENAVIGATIONALSTATE = "net.epicyclic.portletpagenavigationalstate";
   public static final String REQUEST_ATTR_PAGELAYOUT = "net.epicyclic.pagelayout";
   public static final String REQUEST_ATTR_PORTLETWINDOWDEFINITION = "net.epicyclic.portletwindowdefinition";
   public static final PortletRenderResult EMPTY_PORTLETRENDERRESULT = new PortletRenderResult();
   public static final PortletWindowDefinition PORTLETWINDOWDEFINITION_UNKNOWN = new PortletWindowDefinition("unknown", "unknown",
   "unknown", null);

}
