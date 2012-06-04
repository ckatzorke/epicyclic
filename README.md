# Epicyclic Server - a portable AJAX portlet container

epicyclic (from epicyclic gear) is a portable portlet container (JSR 286) built with gatein (inspired by gatein simple portal), intended to run on a lightweight webcontainer (currently tomcat 7 as target runtime).
Together with a lean ReST controller to the portlet container and a javascript library (built with jQuery), the portlets can be integrated in any webpage.
.

## The submodules

### common

common model library

### portletcontainer

in conjunction with the gatein wci it facades the access to the portlets and the relevant portlet invokers for rendering and execution phases.

### war

the web app hosting the ReST controller. Currently targeted to run on tc 7 (just put in webapps directory).

### assemble

Assembly project that packages the required server libraries - just build and unpack in Tomcat lib directory.

## Notes

### Jamon

The com.jamon:jamonapi:2.73 is not available in maven repo. Set the dependency either to 2.4 or download the appropriate library @ http://jamonapi.sourceforge.net/

## How to run the container

1. Run "mvn clean install" on the project epicyclic-server

2. Download a tomcat 7 version and copy all jar files from epicyclic\epicyclic-assemble\target\epicyclic-assemble-1.0-SNAPSHOT-tomcat70\tc7\lib to the lib directory of your tomcat installation

3. Deploy the war file from the epicyclic-war project and a portlet which you want to test

4. Add privileged="true" to the Context-tag in the server.xml of the tomcat configuration
   e.g.: <Context docBase="epicyclic-war" path="/epicyclic" reloadable="true" source="org.eclipse.jst.jee.server:epicyclic-war" privileged="true"/>

4. Start Tomcat

5. Call the Rest controller with the parameters of the portlet you deployed (note: epicyclic ships with a simple test portlet, where the following URI example is taken from)
   http://localhost:8080/epicyclic/render?epi:app=epicyclic&epi:portlet=EpicyclicPortlet&epi:windowid=unique
   
   