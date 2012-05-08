package net.epicyclic.portletinvocation.interceptors;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.epicyclic.common.PortletWindowDefinition;

import org.gatein.pc.api.Mode;
import org.gatein.pc.api.StateString;
import org.gatein.pc.api.WindowState;
import org.gatein.pc.controller.state.PortletPageNavigationalState;
import org.gatein.pc.controller.state.PortletWindowNavigationalState;

/**
 * ParameterMerge Tool that merges passed parameters into the {@link PortletPageNavigationalState}. Can be used to infer
 * {@link HttpServletRequest} parameters into a portletrequest.
 * 
 * @author ckatzorke
 * 
 */
public class ParameterMerge {
   private PortletPageNavigationalState pageNavigationalState;

   /**
    * Default constructor for {@link ParameterMerge}
    * 
    * @param portletPageNavigationalState must not be null
    */
   public ParameterMerge(PortletPageNavigationalState portletPageNavigationalState) {
      if (portletPageNavigationalState == null) {
         throw new IllegalArgumentException("PortletPageNavigationalState must not be null");
      }
      this.pageNavigationalState = portletPageNavigationalState;

   }

   public void mergeIntoPortletRequest(Map<String, String[]> params, Collection<PortletWindowDefinition> portletWindowDefinitions) {
      for (PortletWindowDefinition portletWindowDefinition : portletWindowDefinitions) {
         String windowId = portletWindowDefinition.getWindowId();
         PortletWindowNavigationalState originalPortletWindowNavigationalState = this.pageNavigationalState.getPortletWindowNavigationalState(windowId);
         Map<String, String[]> portletParamMap = new HashMap<String, String[]>();
         Mode mode = Mode.VIEW;
         WindowState state = WindowState.NORMAL;
         if (originalPortletWindowNavigationalState != null) {
            // portletwindow has an initial state, so get, decode, update
            StateString originalNavigationalState = originalPortletWindowNavigationalState.getPortletNavigationalState();
            Map<String, String[]> originalportletParamMap = StateString.decodeOpaqueValue(originalNavigationalState.getStringValue());
            if (originalportletParamMap != null) {
               portletParamMap.putAll(originalportletParamMap);
            }
            mode = originalPortletWindowNavigationalState.getMode();
            state = originalPortletWindowNavigationalState.getWindowState();
         }
         portletParamMap.putAll(params);
         // encode statestringvalue
         String newStateStringValue = StateString.encodeAsOpaqueValue(portletParamMap);
         StateString newStateString = StateString.create(newStateStringValue);
         // create portletwindownavigationalstate and update page state
         PortletWindowNavigationalState newPortletWindowNavigationalState = new PortletWindowNavigationalState(newStateString,
               mode, state);
         this.pageNavigationalState.setPortletWindowNavigationalState(windowId, newPortletWindowNavigationalState);
      }
   }

   /**
    * @return the potentially updated pagenavigationalstate
    */
   public PortletPageNavigationalState getPageNavigationalState() {
      return pageNavigationalState;
   }

}
