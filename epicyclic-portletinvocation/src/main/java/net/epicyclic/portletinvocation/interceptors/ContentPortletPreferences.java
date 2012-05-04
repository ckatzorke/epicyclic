package net.epicyclic.portletinvocation.interceptors;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.portlet.PortletPreferences;
import javax.portlet.ReadOnlyException;
import javax.portlet.ValidatorException;

import org.gatein.pc.api.info.PreferenceInfo;
import org.gatein.pc.api.info.PreferencesInfo;

/**
 * Custom {@link PortletPreferences} implementation to inject custom, CMS defined portlet preferences.
 *
 */
public class ContentPortletPreferences implements PortletPreferences {

   //the origininating portletprefs
   private final PortletPreferences portletPreferences;

   //the custom portletprefs from tw
   private final PreferencesInfo twPreferencesInfo;

   public ContentPortletPreferences(PortletPreferences preferencesDelegate, PreferencesInfo customPreferencesInfo) {
      this.portletPreferences = preferencesDelegate;
      this.twPreferencesInfo = customPreferencesInfo;
   }

   public boolean isReadOnly(String key) {
      return portletPreferences.isReadOnly(key);
   }

   private List<String> getValue(String key) {
      PreferenceInfo preference = twPreferencesInfo.getPreference(key);
      if (preference != null && preference.getDefaultValue() != null) {
         return preference.getDefaultValue();
      }
      return new ArrayList<String>();
   }

   public String getValue(String key, String def) {
      if (key == null) {
         throw new IllegalArgumentException("key must not be null");
      }
      List<String> value = getValue(key);
      if (value == null || value.isEmpty()) {
         //no value in custom - call delegate for normal container behaviour
         return portletPreferences.getValue(key, def);
      }
      else {
         return value.get(0);
      }
   }

   public String[] getValues(String key, String[] def) {
      if (key == null) {
         throw new IllegalArgumentException("key must not be null");
      }
      List<String> value = getValue(key);
      if (value == null || value.isEmpty()) {
         //no value in custom - call delegate for normal container behaviour
         return portletPreferences.getValues(key, def);
      }
      else {
         return value.toArray(new String[value.size()]);
      }
   }

   public void setValue(String key, String value) throws ReadOnlyException {
      portletPreferences.setValue(key, value);
   }

   public void setValues(String key, String[] values) throws ReadOnlyException {
      portletPreferences.setValues(key, values);
   }

   public Enumeration<String> getNames() {
      Enumeration<String> names = portletPreferences.getNames();
      Set<String> keys = twPreferencesInfo.getKeys();
      if (keys != null) {
         //add custom tw keys to names
         if (names != null) {
            while (names.hasMoreElements()) {
               keys.add(names.nextElement());
            }
         }
         return Collections.enumeration(keys);
      }
      return names;
   }

   public Map<String, String[]> getMap() {
      return portletPreferences.getMap();
   }

   public void reset(String key) throws ReadOnlyException {
      portletPreferences.reset(key);
   }

   public void store() throws IOException, ValidatorException {
      portletPreferences.store();
   }

}
