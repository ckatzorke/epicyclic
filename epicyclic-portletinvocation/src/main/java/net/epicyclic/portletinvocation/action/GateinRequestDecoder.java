package net.epicyclic.portletinvocation.action;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.gatein.pc.controller.impl.ControllerRequestParameterNames;
import org.gatein.wci.util.RequestDecoder;

/**
 * Extension to {@link RequestDecoder}, that enables the container to pass url parameter to a portlet.<br>
 * Currently, only action and resource phase is supported, <b>not</b> render phase.
 * <b>ATTENTION:</b><br>
 * This implementation does not make use of ANY security checking when passing a parameter to a portlet! //TODO security
 * layer/String sanitizer for parameters!
 * 
 */
public class GateinRequestDecoder extends RequestDecoder {

   private final Map<String, String[]> functionalParams;

   private static final List<String> CONTAINERPARAMETERNAMES = Arrays.asList(ControllerRequestParameterNames.ACTION_PHASE,
         ControllerRequestParameterNames.INTERACTION_STATE, ControllerRequestParameterNames.LIFECYCLE_PHASE,
         ControllerRequestParameterNames.MODE, ControllerRequestParameterNames.NAVIGATIONAL_STATE,
         ControllerRequestParameterNames.PAGE_NAVIGATIONAL_STATE,
         ControllerRequestParameterNames.PUBLIC_NAVIGATIONAL_STATE_CHANGES, ControllerRequestParameterNames.RENDER_PHASE,
         ControllerRequestParameterNames.RESOURCE_CACHEABILITY, ControllerRequestParameterNames.RESOURCE_ID,
         ControllerRequestParameterNames.RESOURCE_PHASE, ControllerRequestParameterNames.RESOURCE_STATE,
         ControllerRequestParameterNames.WINDOW_ID, ControllerRequestParameterNames.WINDOW_STATE, "type");

   public GateinRequestDecoder(HttpServletRequest request) throws UnsupportedEncodingException {
      super(request);
      if (getQueryParameters() != null) {
         functionalParams = new HashMap<String, String[]>();
         for (String key : getQueryParameters().keySet()) {
            if (!CONTAINERPARAMETERNAMES.contains(key)) {
               functionalParams.put(key, getQueryParameters().get(key));
            }
         }
      }
      else {
         functionalParams = null;
      }
   }

   /**
    * @return all uri parameters that are not part of the portlet container, null when no additional parameters are present
    */
   public Map<String, String[]> getFunctionalPortletParameters() {
      return functionalParams;
   }

}
