package net.epicyclic.common;

import java.util.Arrays;
import java.util.List;

public class Preference {

   private String name;

   private List<String> values;

   public Preference() {
   }

   public Preference(String name, String... values) {
      this.name = name;
      if (values != null) {
         this.values = Arrays.asList(values);
      }
   }

   public String getName() {
      return name;
   }

   public List<String> getValues() {
      return values;
   }

   public void setName(String name) {
      this.name = name;
   }

   public void setValues(List<String> values) {
      this.values = values;
   }

   @Override
   public String toString() {
      return "Preference [name=" + name + ", values=" + values + "]";
   }

}
