package ch.nolix.planningpoker.webapplication.view;

import java.util.ResourceBundle.Control;

import ch.nolix.system.element.style.DeepStyle;
import ch.nolix.system.element.style.Style;
import ch.nolix.system.webgui.container.GridContainer;
import ch.nolix.system.webgui.control.ImageControl;
import ch.nolix.system.webgui.linearcontainer.HorizontalStack;
import ch.nolix.systemapi.elementapi.styleapi.IStyle;
import ch.nolix.systemapi.webguiapi.containerapi.ContainerRole;
import ch.nolix.template.webgui.style.DarkModeStyleCreator;

public final class PlanningPokerStyleCreator {
	
	private static final DarkModeStyleCreator DARK_MODE_STYLE_CREATOR = new DarkModeStyleCreator();
	
	public IStyle createPageSessionStyle() {
		return
		DARK_MODE_STYLE_CREATOR
		.createDarkModeStyle()
		.addConfiguration(
			new DeepStyle()
			.setSelectorType(HorizontalStack.class)
			.addSelectorRole(ContainerRole.FOOTER_CONTAINER)
			.addConfiguration(
				new Style()
				.setSelectorType(HorizontalStack.class)
				.addAttachingAttribute("ContentAlignment(BOTTOM)")
				.addConfiguration(
					new Style()
					.setSelectorType(ImageControl.class)
					.addAttachingAttribute(
						"BaseWidth(50)",
						"BaseHeight(50)"
					)
				)
			),
			new DeepStyle()
			.setSelectorType(GridContainer.class)
			.addAttachingAttribute(
				"BaseGridThickness(1)",
				"BaseGridColor(0xC0C0C0)"
			)
			.addConfiguration(
				new Style()
				.setSelectorType(Control.class)
				.addAttachingAttribute("MinWidth(150)")
			),
			new DeepStyle()
			.addSelectorToken("card")
			.addAttachingAttribute(
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
				"FocusTextColor(White)"
			),
			new DeepStyle()
			.addSelectorToken("activated_card")
			.addAttachingAttribute(
				"BaseBackground(Color(Black))",
				"HoverBackground(Color(Black))",
				"FocusBackground(Color(Black))",
				"BaseTextColor(White)"
			)
		);
	}
}
