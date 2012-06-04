package net.epicyclic.portlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

public class EpicyclicTestPortlet extends GenericPortlet {
	
	@Override
	protected void doView(RenderRequest request, RenderResponse response) throws PortletException, IOException {
		PrintWriter writer = response.getWriter();
		writer.write("It works!");
	}

}
