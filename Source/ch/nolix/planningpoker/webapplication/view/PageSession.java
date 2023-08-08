package ch.nolix.planningpoker.webapplication.view;

import ch.nolix.core.errorcontrol.validator.GlobalValidator;
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
	
	private static final PlanningPokerStyleCreator PAGE_SESSION_STYLE_CREATOR = new PlanningPokerStyleCreator();
		
	private final ch.nolix.coreapi.containerapi.singlecontainerapi.ISingleContainer<String> userIdContainer;
	
	private final ISingleContainer rootControl = new SingleContainer();
	
	protected PageSession(
		final ch.nolix.coreapi.containerapi.singlecontainerapi.ISingleContainer<String> userIdContainer
	) {
		
		GlobalValidator.assertThat(userIdContainer).thatIsNamed("user id container").isNotNull();
		
		this.userIdContainer = userIdContainer;
	}
	
	protected abstract IControl<?, ?> createMainControl(IDataAdapter dataAdapter);
	
	protected abstract IControl<?, ?> createUserProfileControl(IDataAdapter dataAdapter);
	
	protected final String getUserId() {
		return userIdContainer.getStoredElement();
	}
	
	protected final boolean hasUserId() {
		return userIdContainer.containsAny();
	}
	
	@Override
	protected final void initialize() {
		
		getStoredGui()
		.pushLayerWithRootControl(rootControl)
		.setStyle(PAGE_SESSION_STYLE_CREATOR.createPageSessionStyle())
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
