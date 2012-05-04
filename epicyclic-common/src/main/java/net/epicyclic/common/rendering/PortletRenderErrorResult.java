package net.epicyclic.common.rendering;

import net.epicyclic.common.ErrorUtils;
import net.epicyclic.common.PortletWindowDefinition;

/**
 * {@link PortletRenderResult} that marks an error. The causing error can be
 * retrieved and displayed using {@link #getCause()}
 * 
 */
public class PortletRenderErrorResult extends PortletRenderResult {

	private final String errorId;

	private final Throwable cause;
	private final PortletWindowDefinition portletWindowDefinition;

	public PortletRenderErrorResult(
			PortletWindowDefinition portletWindowDefinition, Throwable cause) {
		setRenderType(RenderType.XML);
		this.portletWindowDefinition = portletWindowDefinition;
		this.cause = cause;
		this.errorId = ErrorUtils
				.generateErrorId();

		// set fragment
		StringBuffer fragment = new StringBuffer("");

		fragment.append("<error>");
		fragment.append("<errorid>");
		fragment.append(this.errorId);
		fragment.append("</errorid>");
		fragment.append("<errormsg><![CDATA[");
		fragment.append("<div>Error in portlet</div><div>Applicationname: ");
		fragment.append(this.portletWindowDefinition.getApplicationName());
		fragment.append("</div><div>Portletname: ");
		fragment.append(this.portletWindowDefinition.getPortletName());
		fragment.append("</div><div>handle: ");
		fragment.append(this.portletWindowDefinition.getHandle());
		fragment.append("</div><div>WindowId: ");
		fragment.append(this.portletWindowDefinition.getWindowId());
		fragment.append("</div><div>Preferences: ");
		fragment.append(this.portletWindowDefinition.getPreferencesInfo());
		fragment.append("</div><div>Reason: ");
		if (this.cause != null) {
			fragment.append(this.cause.getMessage());
			fragment.append(".");
		} else {
			fragment.append("n/a");
		}
		fragment.append("</div>]]>");
		fragment.append("</errormsg>");
		fragment.append("</error>");
		setFragment(fragment);
	}

	public Throwable getCause() {
		return cause;
	}

	public String getErrorId() {
		return errorId;
	}

}
