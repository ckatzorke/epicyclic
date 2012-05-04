package net.epicyclic.portletinvocation.test;

import junit.framework.Assert;

import net.epicyclic.portletinvocation.interceptors.SafeHtmlUtil;

import org.junit.Test;


public class SafeHTMLUtilsTest {

   @Test
   public void testSanitize() throws Exception {
      String[] tainted = new String[] { "<script>", "bla"};
      String[] sanitize = SafeHtmlUtil.sanitize(tainted);
      Assert.assertNotNull(sanitize);
      Assert.assertEquals(2, sanitize.length);
      Assert.assertEquals("&lt;script&gt;", sanitize[0]);
   }

}
