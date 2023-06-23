package ch.nolix.planningpoker.webapplication.view;

import ch.nolix.system.element.style.DeepStyle;
import ch.nolix.systemapi.elementapi.styleapi.IStyle;
import ch.nolix.template.webgui.style.DarkModeStyleCreator;

public final class PageSessionStyleCreator {
	
	private static final DarkModeStyleCreator DARK_MODE_STYLE_CREATOR = new DarkModeStyleCreator();
	
	public IStyle createPageSessionStyle() {
		return
		DARK_MODE_STYLE_CREATOR
		.createDarkModeStyle()
		.addConfiguration(
			new DeepStyle()
			.addSelectorToken("card")
			.addAttachingAttribute(
				"BaseWidth(63)",
				"BaseHeight(88)",
				"BaseBackground(Color(Beige))",
				"HoverBackground(Color(Black))",
				"FocusBackground(Color(Black))",
				"BaseTextSize(30)",
				"BaseTextColor(Black)",
				"HoverTextColor(White)",
				"FocusTextColor(White)"
			)
		)
		.addConfiguration(
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
