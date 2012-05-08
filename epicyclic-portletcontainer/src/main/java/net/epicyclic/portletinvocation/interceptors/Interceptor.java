package net.epicyclic.portletinvocation.interceptors;

import javax.servlet.http.HttpServletRequest;

public interface Interceptor {

   public abstract void invoke(HttpServletRequest request);

}