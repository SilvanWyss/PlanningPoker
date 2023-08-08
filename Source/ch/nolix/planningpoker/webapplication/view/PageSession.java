package ch.nolix.planningpoker.webapplication.view;

import ch.nolix.planningpoker.webapplication.footercomponent.FooterComponent;
import ch.nolix.planningpokerapi.logicapi.applicationcontextapi.IDataAdapter;
import ch.nolix.planningpokerapi.logicapi.applicationcontextapi.IPlanningPokerContext;
import ch.nolix.system.application.webapplication.WebClientSession;
import ch.nolix.system.webgui.atomiccontrol.Label;
import ch.nolix.system.webgui.container.SingleContainer;
import ch.nolix.system.webgui.linearcontainer.HorizontalStack;
import ch.nolix.system.webgui.linearcontainer.VerticalStack;
import ch.nolix.systemapi.webguiapi.atomiccontrolapi.LabelRole;
import ch.nolix.systemapi.webguiapi.basecontainerapi.ContainerRole;
import ch.nolix.systemapi.webguiapi.containerapi.ISingleContainer;
import ch.nolix.systemapi.webguiapi.mainapi.IControl;

public abstract class PageSession extends WebClientSession<IPlanningPokerContext> {
	
	private final ISingleContainer rootControl = new SingleContainer();
	
	protected abstract IControl<?, ?> createMainControl(IDataAdapter dataAdapter);
	
	protected abstract IControl<?, ?> createUserProfileControl(IDataAdapter dataAdapter);
	
	@Override
	protected final void initialize() {
		
		getStoredGui()
		.pushLayerWithRootControl(rootControl)
		.setStyle(PlanningPokerStyleCatalogue.DARK_MODE_STYLE)
		.setRemoveLayerAction(this::noteSelfChange);
		
		fillUpRootControl();
	}
	
	protected abstract void noteSelfChange();
	
	protected final void refreshIfDoesNotHaveOpenDialog() {
		if (!hasOpenDialog()) {
			refreshActually();
		}
	}
	
	private void fillUpRootControl() {
		try (final var databaseAdapter = getStoredApplicationContext().createDataAdapter()) {
			rootControl
			.setControl(
				new VerticalStack()
				.setRole(ContainerRole.OVERALL_CONTAINER)
				.addControl(
					new VerticalStack()
					.addControl(
						new HorizontalStack()
						.setRole(ContainerRole.HEADER_CONTAINER)
						.addControl(
							new Label()
							.setRole(LabelRole.TITLE)
							.setText(getApplicationName()),
							createUserProfileControl(databaseAdapter)
						)
					),
					new SingleContainer()
					.setRole(ContainerRole.MAIN_CONTENT_CONTAINER)
					.setControl(createMainControl(databaseAdapter)),
					new FooterComponent(this).getStoredControl()
				)
			);
		}
	}
	
	private boolean hasOpenDialog() {
		return (getStoredGui().getStoredLayers().getElementCount() > 1);
	}
	
	private void refreshActually() {
		
		fillUpRootControl();
		
		refresh();
	}
}
