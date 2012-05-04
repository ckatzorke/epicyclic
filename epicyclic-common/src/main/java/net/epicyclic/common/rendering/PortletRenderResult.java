package net.epicyclic.common.rendering;

public class PortletRenderResult {

   private RenderType renderType = RenderType.HTML; // default

   private StringBuffer fragment;

   public PortletRenderResult() {
      this.fragment = new StringBuffer();
   }

   public StringBuffer getFragment() {
      return fragment;
   }

   public void setFragment(StringBuffer fragment) {
      this.fragment = fragment;
   }

   public void setRenderType(RenderType renderType) {
      this.renderType = renderType;
   }

   public RenderType getRenderType() {
      return renderType;
   }

   @Override
   public String toString() {
      return "PortletRenderResult [renderType=" + renderType + ", fragment=" + fragment + "]";
   }

}
