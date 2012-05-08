package net.epicyclic.portletinvocation.rendering;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.servlet.http.Cookie;

import net.epicyclic.common.PortletIntegrationException;
import net.epicyclic.common.PortletWindowDefinition;
import net.epicyclic.common.rendering.PortletRenderResult;
import net.epicyclic.common.rendering.RenderType;
import net.epicyclic.portletinvocation.EpicyclicPortletControllerContext;
import net.epicyclic.portletinvocation.PortletInvocationUtils;

import org.gatein.common.net.media.MediaType;
import org.gatein.pc.api.PortletInvokerException;
import org.gatein.pc.api.info.CapabilitiesInfo;
import org.gatein.pc.api.info.PortletInfo;
import org.gatein.pc.api.invocation.response.ContentResponse;
import org.gatein.pc.api.invocation.response.ErrorResponse;
import org.gatein.pc.api.invocation.response.PortletInvocationResponse;
import org.gatein.pc.api.invocation.response.UnavailableResponse;
import org.gatein.pc.controller.PortletController;
import org.gatein.pc.controller.state.PortletPageNavigationalState;

public class GateinPortletRenderer {

   private final static Logger logger = Logger.getLogger(GateinPortletRenderer.class.getCanonicalName());

   private static final String ERROR_FRAGMENT_RESOURCE_NOT_AVAILABLE = "The requested resource";
   private final PortletWindowDefinition portletWindowDefinition;
   private final PortletInfo portletInfo;
   private final EpicyclicPortletControllerContext portletControllerContext;
   private final PortletPageNavigationalState portletPageNavigationalState;

   public GateinPortletRenderer(EpicyclicPortletControllerContext portletControllerContext,
         PortletPageNavigationalState portletPageNavigationalState, PortletInfo portletInfo,
         PortletWindowDefinition portletWindowDefinition) {
      this.portletControllerContext = portletControllerContext;
      this.portletPageNavigationalState = portletPageNavigationalState;
      this.portletWindowDefinition = portletWindowDefinition;
      this.portletInfo = portletInfo;
   }

   public PortletRenderResult render() throws PortletIntegrationException {
      // get current gateinportalrendercontext

      Cookie[] requestCookies = portletControllerContext.getClientRequest().getCookies();
      List<Cookie> cookies = requestCookies == null ? null : Arrays.asList(requestCookies);
      PortletRenderResult result = new PortletRenderResult();
      try {
         PortletInvocationResponse windowResponse = new PortletController().render(portletControllerContext, cookies,
               portletPageNavigationalState, portletWindowDefinition.getWindowId());
         if (windowResponse instanceof ContentResponse) {
            ContentResponse fragment = (ContentResponse) windowResponse;
            if (fragment.getType() != ContentResponse.TYPE_EMPTY) {
               String frag = null;
               if (fragment.getType() == ContentResponse.TYPE_BYTES) {
                  frag = new String(fragment.getBytes(), "utf-8");
               }
               else {
                  frag = fragment.getChars();
               }
               result.setFragment(new StringBuffer(frag));
               // test for fragments that are faulty
               if (ERROR_FRAGMENT_RESOURCE_NOT_AVAILABLE.equals(result.getFragment().substring(0,
                     ERROR_FRAGMENT_RESOURCE_NOT_AVAILABLE.length()))) {
                  throw new PortletIntegrationException(result.getFragment().toString());
               }
               // in case of xml/xhtml/azportlet, only when aggregation
               // was successful
               result.setRenderType(evaluatePortletType(portletInfo));
            }
         }
         else if (windowResponse instanceof ErrorResponse) {
            ErrorResponse response = (ErrorResponse) windowResponse;
            logger.severe("There was an error rendering the portlet " + portletWindowDefinition + " [" + response.getMessage()
                  + "]");

            result = PortletInvocationUtils.handleException(portletWindowDefinition, response.getCause());
         }
         else if (windowResponse instanceof UnavailableResponse) {
            UnavailableResponse response = (UnavailableResponse) windowResponse;
            String msg = "Portlet " + portletWindowDefinition + " unavailable [permanent=" + response.isPermanent()
                  + "; unavail. time: " + response.getUnavailableSeconds() + "]";
            result = PortletInvocationUtils.handleException(portletWindowDefinition, new PortletIntegrationException(msg));
         }
      }
      catch (PortletInvokerException e) {
         result = PortletInvocationUtils.handleException(portletWindowDefinition, e);
      }
      catch (Exception e) {
         result = PortletInvocationUtils.handleException(portletWindowDefinition, e);
      }
      return result;
   }

   public RenderType evaluatePortletType(PortletInfo portletInfo) {
      RenderType type = RenderType.HTML;
      if (portletInfo != null) {
         CapabilitiesInfo capabilities = portletInfo.getCapabilities();
         if (capabilities != null) {
            Set<MediaType> mediaTypes = capabilities.getMediaTypes();
            if (mediaTypes != null) {
               for (MediaType mediaType : mediaTypes) {
                  RenderType foundType = RenderType.getRenderTypeForMediaType(mediaType.getValue());
                  if (foundType != null) {
                     if (foundType.getRanking() > type.getRanking()) {
                        logger.fine("The Portlet " + portletInfo.getName() + " also supports " + mediaType.getValue()
                              + ", switching to " + foundType);
                        type = foundType;
                     }
                  }
               }
            }
         }
         else {
            logger.finest("No capabilities/mediatypes. Asserting html");
         }
      }
      else {
         logger.severe("No portletInfo passed!");
      }
      return type;
   }
}