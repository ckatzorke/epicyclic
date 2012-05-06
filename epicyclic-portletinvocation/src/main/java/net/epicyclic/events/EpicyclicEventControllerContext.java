package net.epicyclic.events;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.epicyclic.common.PortletWindowDefinition;
import net.epicyclic.portletinvocation.EpicyclicPortletControllerContext;

import org.gatein.pc.api.info.PortletInfo;
import org.gatein.pc.api.invocation.response.PortletInvocationResponse;
import org.gatein.pc.controller.event.EventControllerContext;
import org.gatein.pc.controller.event.EventPhaseContext;
import org.gatein.pc.controller.event.PortletWindowEvent;

/**
 * Route events according to the portlets discovered on the page. For now it is
 * pretty trivial. We could leverage JSP tags to 'wire' portlets on the same
 * page for instance.<br>
 * This is an internal class needed for gears-portletintegration and should not
 * be used outside
 * 
 */
public class EpicyclicEventControllerContext implements EventControllerContext {

	/** . */
	private final EpicyclicPortletControllerContext context;

	/** Used internally. */
	private final Map<PortletWindowEvent, EventRoute> routings;

	private final List<EventRoute> roots;

	public EpicyclicEventControllerContext(EpicyclicPortletControllerContext context) {
		this.context = context;
		this.routings = new LinkedHashMap<PortletWindowEvent, EventRoute>();
		this.roots = new ArrayList<EventRoute>();
	}

	public List<EventRoute> getRoots() {
		return roots;
	}

	public void eventProduced(EventPhaseContext context, PortletWindowEvent producedEvent, PortletWindowEvent causeEvent) {
		EventRoute relatedRoute = routings.get(causeEvent);

		for (PortletWindowDefinition windowDef : this.context.getPageLayout().getPortletWindowDefinitions()) {
			try {

				PortletInfo portletInfo = this.context.getPortletInfo(windowDef.getWindowId());

				//
				if (portletInfo != null) {
					//
					if (portletInfo.getEventing().getConsumedEvents().containsKey(producedEvent.getName())) {
						PortletWindowEvent destinationEvent = new PortletWindowEvent(producedEvent.getName(),
								producedEvent.getPayload(), windowDef.getWindowId());

						//
						EventRoute eventRoute = new EventRoute(relatedRoute, producedEvent.getName(),
								producedEvent.getPayload(), producedEvent.getWindowId(), destinationEvent.getWindowId());

						//
						if (relatedRoute != null) {
							relatedRoute.getChildren().add(eventRoute);
						} else {
							roots.add(eventRoute);
						}

						//
						routings.put(destinationEvent, eventRoute);

						//
						context.queueEvent(destinationEvent);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				context.interrupt();
			}
		}
	}

	public void eventConsumed(EventPhaseContext context, PortletWindowEvent consumedEvent,
			PortletInvocationResponse consumerResponse) {
		EventRoute route = routings.get(consumedEvent);
		route.setAcknowledgement(new EventAcknowledgement.Consumed(consumerResponse));
	}

	public void eventFailed(EventPhaseContext context, PortletWindowEvent failedEvent, Throwable throwable) {
		EventRoute route = routings.get(failedEvent);
		route.setAcknowledgement(new EventAcknowledgement.Failed(throwable));
	}

	public void eventDiscarded(EventPhaseContext context, PortletWindowEvent discardedEvent, int cause) {
		EventRoute route = routings.get(discardedEvent);
		route.setAcknowledgement(new EventAcknowledgement.Discarded(cause));
	}
}
