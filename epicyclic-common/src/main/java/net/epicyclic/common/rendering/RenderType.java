package net.epicyclic.common.rendering;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Defines the rendertype of a portlet. Depending on the supported mimetype, the portlet renderer generates a different div
 * container
 * 
 */
public enum RenderType {

   HTML("text/html", PortletType.htmlPortlet, 0), XHTML("text/xhtml", PortletType.htmlPortlet, 10), XML("text/xml",
         PortletType.xmlPortlet, 30);

   enum PortletType {
      htmlPortlet, xhtmlPortlet, xmlPortlet;
   }

   private static Map<String, RenderType> renderTypesPerMediaType = new HashMap<String, RenderType>();

   static {
      for (RenderType type : EnumSet.allOf(RenderType.class)) {
         renderTypesPerMediaType.put(type.mediaType, type);
      }
   }

   private PortletType portletType;
   private String mediaType;
   private int ranking;

   private RenderType(String mediaType, PortletType portletType, int ranking) {
      this.mediaType = mediaType;
      this.portletType = portletType;
      this.ranking = ranking;
   }

   /**
    * @return the portlettype, one of {@link #portletType}, i.e. the identifier for the XSL which template to apply. Values are
    *         htmlPortlet, azPortlet, xmlPortlet, xhtmlPortlet.
    */
   public String getPortletType() {
      return portletType.toString();
   }

   public String getMediaType() {
      return mediaType;
   }

   public int getRanking() {
      return ranking;
   }

   /**
    * Looks up the corresponding renderType null if not found
    * 
    * @param mediaType as mediaType, e.g. text/html, text/xml, text/xhtml NOT only the subtype html
    * @return the correct RenderType for given mediaTyoe
    */
   public static RenderType getRenderTypeForMediaType(String mediaType) {
      return renderTypesPerMediaType.get(mediaType);
   }

   @Override
   public String toString() {
      return portletType + " [" + mediaType + "]";
   }
}
