package net.epicyclic.portletinvocation.interceptors;

public final class SafeHtmlUtil {
   public static String sanitize(String raw) {
      if (raw == null || raw.length() == 0) {
         return raw;
      }

      return HTMLEntityEncode(canonicalize(raw));
   }

   public static String HTMLEntityEncode(String input) {
      String next = input;
      StringBuffer sb = new StringBuffer();
      for (int i = 0; i < next.length(); ++i) {
         char ch = next.charAt(i);

         if (ch == '<') {
            sb.append("&lt;");
         }
         else if (ch == '>') {
            sb.append("&gt;");
         }
         else {
            sb.append(ch);
         }
      }

      return sb.toString();
   }

   // "Simplifies input to its simplest form to make encoding tricks more difficult"
   // though it didn't do seem to do anything to hex or html encoded characters... *shrug* maybe for unicode?
   public static String canonicalize(String input) {
      return java.text.Normalizer.normalize(input, java.text.Normalizer.Form.NFC);
   }

   public static String[] sanitize(String[] raw) {
      String[] ret = null;
      if (raw != null) {
         ret = new String[raw.length];
         for (int i = 0; i < raw.length; i++) {
            ret[i] = sanitize(raw[i]);
         }
      }
      return ret;
   }

   private SafeHtmlUtil() {
      // hidden
   }
}
