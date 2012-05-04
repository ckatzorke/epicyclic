package net.epicyclic.portletinvocation.interceptors;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import net.epicyclic.common.Commons;
import net.epicyclic.common.PageLayout;

import org.gatein.pc.controller.state.PortletPageNavigationalState;

/**
 * Merges url parameters into portletrequests. Parameters to be passed must be prefixed with portlet:, e.g.<br>
 * debug.html?portlet:id=123
 * 
 * 
 */
@Named("requestParameterMergeInterceptor")
public class RequestParameterMergeInterceptor implements Interceptor {

   @Override
   public void invoke(HttpServletRequest request) {
      PageLayout pageLayout = (PageLayout) request.getAttribute(Commons.REQUEST_ATTR_PAGELAYOUT);
      if (pageLayout != null) {
         @SuppressWarnings("unchecked")
         Map<String, String[]> requestParameterMap = request.getParameterMap();
         if (requestParameterMap != null && !requestParameterMap.isEmpty()) {
            // only when portlet: params are available
            Map<String, String[]> requestMap2BeAdded = new HashMap<String, String[]>();
            for (Entry<String, String[]> entry : requestParameterMap.entrySet()) {
               String key = entry.getKey();
               if (key.startsWith("portlet:")) {
                  requestMap2BeAdded.put(key.substring(key.indexOf(":") + 1), SafeHtmlUtil.sanitize(requestParameterMap.get(key)));
               }
            }
            if (!requestMap2BeAdded.isEmpty()) {
               PortletPageNavigationalState portletPageNavigationalState = (PortletPageNavigationalState) request.getAttribute(Commons.REQUEST_ATTR_PAGENAVIGATIONALSTATE);
               ParameterMerge merger = new ParameterMerge(portletPageNavigationalState);
               // add all portlet: prefixed params to all portlets on the page. Scoping will be implemented as needed.
               merger.mergeIntoPortletRequest(requestMap2BeAdded, pageLayout.getPortletWindowDefinitions());
               request.setAttribute(Commons.REQUEST_ATTR_PAGENAVIGATIONALSTATE, merger.getPageNavigationalState());
            }
         }
      }
   }

}
