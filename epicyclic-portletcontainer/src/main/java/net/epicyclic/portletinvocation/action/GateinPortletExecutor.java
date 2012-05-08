package net.epicyclic.portletinvocation.action;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import net.epicyclic.common.Commons;
import net.epicyclic.common.PageLayout;
import net.epicyclic.common.PortletWindowDefinition;
import net.epicyclic.portletinvocation.EpicyclicPortletControllerContext;
import net.epicyclic.portletinvocation.PortletInvocationUtils;

import org.gatein.common.io.IOTools;
import org.gatein.common.io.Serialization;
import org.gatein.pc.api.PortletInvokerException;
import org.gatein.pc.api.URLFormat;
import org.gatein.pc.api.invocation.response.ContentResponse;
import org.gatein.pc.api.invocation.response.ErrorResponse;
import org.gatein.pc.api.invocation.response.HTTPRedirectionResponse;
import org.gatein.pc.api.invocation.response.PortletInvocationResponse;
import org.gatein.pc.controller.PortletController;
import org.gatein.pc.controller.impl.ControllerRequestFactory;
import org.gatein.pc.controller.impl.PortletURLRenderer;
import org.gatein.pc.controller.impl.URLParameterConstants;
import org.gatein.pc.controller.request.ControllerRequest;
import org.gatein.pc.controller.request.PortletActionRequest;
import org.gatein.pc.controller.response.ControllerResponse;
import org.gatein.pc.controller.response.PageUpdateResponse;
import org.gatein.pc.controller.response.PortletResponse;
import org.gatein.pc.controller.response.ResourceResponse;
import org.gatein.pc.controller.state.PortletPageNavigationalState;
import org.gatein.pc.controller.state.PortletPageNavigationalStateSerialization;
import org.gatein.wci.Body;

import com.jamonapi.Monitor;

public class GateinPortletExecutor {

   private final static Logger logger = Logger.getLogger(GateinPortletExecutor.class.getCanonicalName());

   private boolean redirectAfterAction;

   private final EpicyclicPortletControllerContext portletControllerContext;

   public GateinPortletExecutor(EpicyclicPortletControllerContext portletControllerContext) {
      this.portletControllerContext = portletControllerContext;
   }

   public PortletPageNavigationalState executeActionPhase(PortletPageNavigationalState portletPageNavigationalState)
         throws UnsupportedEncodingException, ServletException, IOException {
      // Process only portlet type
      // The request decoded if not null
      PortletPageNavigationalState pageNavigationalState = portletPageNavigationalState;
      // The type of invocation
      String type = portletControllerContext.getClientRequest().getParameter(URLParameterConstants.TYPE);
      if (URLParameterConstants.PORTLET_TYPE.equals(type)) {
         Serialization<PortletPageNavigationalState> portletPageNavigationalStateSerialization = new PortletPageNavigationalStateSerialization(
               portletControllerContext.getStateControllerContext());
         ControllerRequestFactory factory = new ControllerRequestFactory(portletPageNavigationalStateSerialization);

         GateinRequestDecoder decoder = new GateinRequestDecoder(portletControllerContext.getClientRequest());
         // does not work for resource requests
         // ControllerRequest request =
         // factory.decode(decoder.getQueryParameters(), decoder.getBody());
         //
         Body body = decoder.getBody();
         ControllerRequest request;
         if (body instanceof Body.Form) {
            request = factory.decode(decoder.getQueryParameters(), body);
         }
         else {
            request = factory.decode(decoder.getQueryParameters(), decoder.getFunctionalPortletParameters());
         }

         ControllerResponse controllerResponse = null;
         Monitor processMon = null;
         try {
            String phase = getPhase();
            phase = phase == null ? "unknown" : phase;
            PortletWindowDefinition portletWindowDefinition = getPortletWindowDefinition();
            processMon = PortletInvocationUtils.createPortletMonitor(portletWindowDefinition, phase);
            controllerResponse = new PortletController().process(portletControllerContext, request);
         }
         catch (PortletInvokerException e) {
            throw new ServletException(e);
         }
         finally {
            if (processMon != null) {
               processMon.stop();
            }
         }

         //
         if (controllerResponse instanceof PageUpdateResponse) {
            PageUpdateResponse pageUpdate = (PageUpdateResponse) controllerResponse;

            //
            pageNavigationalState = pageUpdate.getPageNavigationalState();

            //
            portletControllerContext.getClientRequest().setAttribute("bilto", portletControllerContext);

            // We perform a send redirect on actions
            if (request instanceof PortletActionRequest && redirectAfterAction) {
               PortletURLRenderer renderer = new PortletURLRenderer(pageNavigationalState,
                     portletControllerContext.getClientRequest(), portletControllerContext.getClientResponse(),
                     portletPageNavigationalStateSerialization);

               //
               String url = renderer.renderURL(new URLFormat(null, null, true, null));
               portletControllerContext.getClientResponse().sendRedirect(url);
               return null;
            }
         }
         else if (controllerResponse instanceof ResourceResponse) {
            ResourceResponse resourceResponse = (ResourceResponse) controllerResponse;
            PortletInvocationResponse pir = resourceResponse.getResponse();

            //
            if (pir instanceof ContentResponse) {
               ContentResponse contentResponse = (ContentResponse) pir;

               //
               if (contentResponse.getType() == ContentResponse.TYPE_EMPTY) {
                  portletControllerContext.getClientResponse().setStatus(HttpServletResponse.SC_NO_CONTENT);
               }
               else {
                  String contentType = contentResponse.getContentType();
                  if (contentType != null) {
                     portletControllerContext.getClientResponse().setContentType(contentType);
                  }

                  //
                  if (contentResponse.getType() == ContentResponse.TYPE_BYTES) {
                     ServletOutputStream out = null;
                     try {
                        out = portletControllerContext.getClientResponse().getOutputStream();
                        out.write(contentResponse.getBytes());
                     }
                     finally {
                        IOTools.safeClose(out);
                     }
                  }
                  else {
                     Writer writer = null;
                     try {
                        writer = portletControllerContext.getClientResponse().getWriter();
                        writer.write(contentResponse.getChars());
                     }
                     finally {
                        if (writer != null) {
                           writer.close();
                        }
                     }
                  }
               }
            }
            // else {
            // // todo wat nu?
            // }

            //
            return null;
         }
         else if (controllerResponse instanceof PortletResponse) {
            PortletResponse portletResponse = (PortletResponse) controllerResponse;
            PortletInvocationResponse pir = portletResponse.getResponse();

            //
            if (pir instanceof ErrorResponse) {
               ErrorResponse errorResponse = (ErrorResponse) pir;

               //
               if (errorResponse.getCause() != null) {
                  // throw new ServletException(GateinContainerRenderingConstants.PORTLET_ERROR, errorResponse.getCause());
                  logger.severe("Portlet could not be executed! [" + errorResponse.getMessage() + "]");
               }
               else {
                  // throw new ServletException(GateinContainerRenderingConstants.PORTLET_ERROR);
                  logger.severe("Portlet could not be executed! ");
               }
            }
            // else if (pir instanceof UnavailableResponse) {
            // // throw new ServletException(GateinContainerRenderingConstants.UNAVAILABLE);
            // }
            else if (pir instanceof HTTPRedirectionResponse) {
               HTTPRedirectionResponse redirectResponse = (HTTPRedirectionResponse) pir;
               portletControllerContext.getClientResponse().sendRedirect(redirectResponse.getLocation());
            }
            // else {
            // // todo wat nu?
            // }
         }
         // else {
         // // todo wat nu?
         // }
      }
      return pageNavigationalState;
   }

   public String getPhase() {
      return portletControllerContext.getClientRequest().getParameter("phase");
   }

   public PortletWindowDefinition getPortletWindowDefinition() {
      PortletWindowDefinition portletWindowDefinition = Commons.PORTLETWINDOWDEFINITION_UNKNOWN;
      String windowId = portletControllerContext.getClientRequest().getParameter("windowid");
      if (windowId != null) {
         PageLayout pageLayout = (PageLayout) portletControllerContext.getClientRequest().getAttribute(
               Commons.REQUEST_ATTR_PAGELAYOUT);
         if (pageLayout != null) {
            PortletWindowDefinition tmp = pageLayout.findPortletWindowDefinition(windowId);
            if (tmp != null) {
               portletWindowDefinition = tmp;
            }
         }

      }
      return portletWindowDefinition;
   }

   public void setRedirectAfterAction(boolean redirectAfterAction) {
      this.redirectAfterAction = redirectAfterAction;
   }

   public boolean isRedirectAfterAction() {
      return redirectAfterAction;
   }

   // utility
   // private Monitor getProcessMon(HttpServletRequest req, PortletControllerContext context) {
   // Monitor processMon;
   // String windowId = req.getParameter("windowid");
   // String portlet = null;
   // String phase = req.getParameter("phase");
   // if (phase == null) {
   // phase = "unknown";
   // }
   // AggregatorPortletControllerContext portletControllerContext = (AggregatorPortletControllerContext) context;
   // if (windowId == null) {
   // windowId = "unknown";
   // portlet = "unknown";
   // }
   // else {
   // PortletWindowDefinition portletWindowDefinition =
   // portletControllerContext.getPortletRenderingContext().getPortletWindowDefinition(
   // windowId);
   // if (portletWindowDefinition != null) {
   // portlet = portletWindowDefinition.getPortletContextId();
   // }
   // }
   // processMon = MonitorFactory.start("portlet: " + portlet + ", phase [" + phase + "]");// , windowId [" + windowId + "]");
   // return processMon;
   // }
}
