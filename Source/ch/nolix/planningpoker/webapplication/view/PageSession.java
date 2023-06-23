package ch.nolix.planningpoker.webapplication.view;

import ch.nolix.core.errorcontrol.validator.GlobalValidator;
import ch.nolix.planningpoker.webapplication.controller.PageController;
import ch.nolix.planningpokerapi.applicationcontextapi.IPlanningPokerContext;
import ch.nolix.planningpokerapi.applicationcontextapi.IDataController;
import ch.nolix.planningpokerapi.applicationcontextapi.IRoomChangeNotifier;
import ch.nolix.system.application.webapplication.WebClientSession;
import ch.nolix.system.webgui.container.SingleContainer;
import ch.nolix.system.webgui.control.Button;
import ch.nolix.system.webgui.control.Label;
import ch.nolix.system.webgui.linearcontainer.HorizontalStack;
import ch.nolix.system.webgui.linearcontainer.VerticalStack;
import ch.nolix.systemapi.webguiapi.containerapi.ContainerRole;
import ch.nolix.systemapi.webguiapi.containerapi.ISingleContainer;
import ch.nolix.systemapi.webguiapi.controlapi.LabelRole;
import ch.nolix.systemapi.webguiapi.mainapi.IControl;

public abstract class PageSession extends WebClientSession<IPlanningPokerContext> {
	
	private static final PageController PAGE_SESSION_HELPER = new PageController();
	
	private static final PageSessionStyleCreator PAGE_SESSION_STYLE_CREATOR = new PageSessionStyleCreator();
	
	private final ch.nolix.coreapi.containerapi.singlecontainerapi.ISingleContainer<String> userIdContainer;
	
	private final ISingleContainer<?, ?> rootControl = new SingleContainer();
	
	protected PageSession(
		final ch.nolix.coreapi.containerapi.singlecontainerapi.ISingleContainer<String> userIdContainer
	) {
		
		GlobalValidator.assertThat(userIdContainer).thatIsNamed("user id container").isNotNull();
		
		this.userIdContainer = userIdContainer;
	}
	
	protected abstract IControl<?, ?> createMainControl(IDataController dataController);
	
	protected abstract void doRegistrations(IRoomChangeNotifier roomChangeNotifier);
	
	protected final String getUserId() {
		return userIdContainer.getOriElement();
	}
	
	protected final boolean hasUserId() {
		return userIdContainer.containsAny();
	}
	
	@Override
	protected final void initialize() {
		
		doRegistrations(getOriApplicationContext().getOriRoomChangeNotifier());
		
		getOriGUI()
		.pushLayerWithRootControl(rootControl)
		.setStyle(PAGE_SESSION_STYLE_CREATOR.createPageSessionStyle())
		.setRemoveLayerAction(this::noteSelfChange);
		
		fillUpRootControl();
	}
	
	protected abstract void noteSelfChange();
	
	protected final void refreshIfDoesNotHaveOpenDialog() {
		if (hasOpenDialog()) {
			refreshActually();
		}
	}
	
	private void fillUpRootControl() {
		try (final var dataController = getOriApplicationContext().createDataController()) {
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
							new HorizontalStack()
							.setVisibility(hasUserId())
							.addControl(
								new Label()
								.setText("you: "),
								new Button()
								.setText(PAGE_SESSION_HELPER.getUserName(userIdContainer, dataController))
								.setLeftMouseButtonPressAction(
									() -> PAGE_SESSION_HELPER.openEditUserNameDialog(getUserId(), this)
								)
							)
						)
					),
					createMainControl(dataController)
				)
			);
		}
	}
	
	private boolean hasOpenDialog() {
		return (getOriGUI().getOriLayers().getElementCount() < 2);
	}
	
	private void refreshActually() {
		
		fillUpRootControl();
		
		updateCounterpart();
	}
}
