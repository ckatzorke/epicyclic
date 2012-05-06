# Epicyclic Server - a portable AJAX portlet container

epicyclic (from epicyclic gear) is a portable portlet container (JSR 286) built with gatein (inspired by gatein simple portal), intended to run on a lightweight webcontainer (currently tomcat 7 as target runtime).
Together with a lean ReST controller to the portlet container and a javascript library (built with jQuery), the portlets can be integrated in any webpage.
.

## The submodules

### common

common model library

### portletcontainerservice

in conjunction with the gatein wci it facades the access to the portlets and the relevant portlet invokers.

### portletinvocation

all relevant classes necessary to fulfill a portlet request.

### war

the web app hosting the ReST controller. Currently targeted to run on tc 7.