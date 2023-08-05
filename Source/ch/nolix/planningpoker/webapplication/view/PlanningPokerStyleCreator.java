package ch.nolix.planningpoker.webapplication.view;

import java.util.ResourceBundle.Control;

import ch.nolix.core.container.immutablelist.ImmutableList;
import ch.nolix.core.container.linkedlist.LinkedList;
import ch.nolix.system.element.stylebuilder.DeepSelectingStyleBuilder;
import ch.nolix.system.element.stylebuilder.SelectingStyleBuilder;
import ch.nolix.system.webgui.atomiccontrol.ImageControl;
import ch.nolix.system.webgui.container.Grid;
import ch.nolix.system.webgui.linearcontainer.HorizontalStack;
import ch.nolix.systemapi.elementapi.styleapi.IStyle;
import ch.nolix.systemapi.webguiapi.basecontainerapi.ContainerRole;
import ch.nolix.template.webgui.style.StyleCatalogue;

public final class PlanningPokerStyleCreator {
	
	public IStyle createPageSessionStyle() {
		return
		StyleCatalogue
		.DARK_MODE_STYLE
		.withAttachingAttributesAndSubStyles(
			new ImmutableList<>(),
			LinkedList
			.withElement(
				new DeepSelectingStyleBuilder()
				.setSelectorType(HorizontalStack.class)
				.addSelectorRole(ContainerRole.FOOTER_CONTAINER)
				.addSubStyle(
					new SelectingStyleBuilder()
					.setSelectorType(HorizontalStack.class)
					.addAttachingAttribute("ContentAlignment(BOTTOM)")
					.addSubStyle(
						new SelectingStyleBuilder()
						.setSelectorType(ImageControl.class)
						.addAttachingAttribute(
							"BaseWidth(50)",
							"BaseHeight(50)"
						)
						.build()
					)
					.build()
				)
				.build(),
				new DeepSelectingStyleBuilder()
				.setSelectorType(Grid.class)
				.addAttachingAttribute(
					"BaseGridThickness(1)",
					"BaseGridColor(0xC0C0C0)"
				)
				.addSubStyle(
					new SelectingStyleBuilder()
					.setSelectorType(Control.class)
					.addAttachingAttribute("MinWidth(150)")
					.build()
				)
				.build(),
				new DeepSelectingStyleBuilder()
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
				)
				.build(),
				new DeepSelectingStyleBuilder()
				.addSelectorToken("activated_card")
				.addAttachingAttribute(
					"BaseBackground(Color(Black))",
					"HoverBackground(Color(Black))",
					"FocusBackground(Color(Black))",
					"BaseTextColor(White)"
				)
				.build()
			)
		);
	}
}
