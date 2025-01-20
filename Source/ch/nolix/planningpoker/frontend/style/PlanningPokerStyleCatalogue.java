package ch.nolix.planningpoker.frontend.style;

import java.util.ResourceBundle.Control;

import ch.nolix.core.container.immutablelist.ImmutableList;
import ch.nolix.system.element.style.DeepSelectingStyle;
import ch.nolix.system.element.style.SelectingStyle;
import ch.nolix.system.webgui.atomiccontrol.imagecontrol.ImageControl;
import ch.nolix.system.webgui.container.Grid;
import ch.nolix.system.webgui.linearcontainer.HorizontalStack;
import ch.nolix.systemapi.elementapi.styleapi.IStyle;
import ch.nolix.systemapi.webguiapi.basecontainerapi.ContainerRole;
import ch.nolix.template.webgui.style.StyleCatalog;

public final class PlanningPokerStyleCatalogue {

  public static final IStyle DARK_MODE_STYLE = StyleCatalog.DARK_STYLE
    .withSubStyles(
      ImmutableList
        .withElement(
          new DeepSelectingStyle()
            .withSelectorType(HorizontalStack.class)
            .withSelectorRole(ContainerRole.FOOTER_CONTAINER)
            .withSubStyle(
              new SelectingStyle()
                .withSelectorType(HorizontalStack.class)
                .withAttachingAttribute("ContentAlignment(BOTTOM)")
                .withSubStyle(
                  new SelectingStyle()
                    .withSelectorType(ImageControl.class)
                    .withAttachingAttribute(
                      "BaseWidth(50)",
                      "BaseHeight(50)"))),
          new DeepSelectingStyle()
            .withSelectorType(Grid.class)
            .withAttachingAttribute(
              "BaseGridThickness(1)",
              "BaseGridColor(0xC0C0C0)")
            .withSubStyle(
              new SelectingStyle()
                .withSelectorType(Control.class)
                .withAttachingAttribute("MinWidth(150)")),
          new DeepSelectingStyle()
            .withSelectorToken("card")
            .withAttachingAttribute(
              "BaseWidth(63)",
              "BaseHeight(88)",
              "MinWidth(1)",
              "BaseBorderThickness(0)",
              "BaseBackground(Color(Beige))",
              "HoverBackground(Color(Black))",
              "FocusBackground(Color(Black))",
              "BaseTextSize(30)",
              "BaseTextColor(Black)",
              "HoverTextColor(White)",
              "FocusTextColor(White)"),
          new DeepSelectingStyle()
            .withSelectorToken("activated_card")
            .withAttachingAttribute(
              "BaseBackground(Color(Black))",
              "HoverBackground(Color(Black))",
              "FocusBackground(Color(Black))",
              "BaseTextColor(White)")));

  private PlanningPokerStyleCatalogue() {
  }
}
