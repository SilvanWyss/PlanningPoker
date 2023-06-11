package ch.nolix.planningpoker.webapplication;

import ch.nolix.core.container.singlecontainer.SingleContainer;
import ch.nolix.core.errorcontrol.validator.GlobalValidator;
import ch.nolix.planningpokerapi.applicationcontextapi.IApplicationContext;
import ch.nolix.planningpokerapi.applicationcontextapi.IDataController;
import ch.nolix.planningpokerapi.applicationcontextapi.IRoomChangeNotifier;
import ch.nolix.system.application.webapplication.BackendWebClientSession;
import ch.nolix.system.webgui.control.Label;
import ch.nolix.system.webgui.linearcontainer.VerticalStack;
import ch.nolix.systemapi.webguiapi.containerapi.ContainerRole;
import ch.nolix.systemapi.webguiapi.controlapi.LabelRole;
import ch.nolix.systemapi.webguiapi.mainapi.IControl;

public abstract class PageSession extends BackendWebClientSession<IApplicationContext> {
	
	private static final PageSessionHelper PAGE_SESSION_HELPER = new PageSessionHelper();
	
	private final SingleContainer<String> userIdContainer;
	
	protected PageSession(final SingleContainer<String> userIdContainer) {
		
		GlobalValidator.assertThat(userIdContainer).thatIsNamed("user id container").isNotNull();
		
		this.userIdContainer = userIdContainer;
	}
	
	public String getUserId() {
		return userIdContainer.getOriElement();
	}
	
	public boolean hasUserId() {
		return userIdContainer.containsAny();
	}
	
	protected abstract IControl<?, ?> createMainControl(IDataController dataController);
	
	protected abstract void doRegistrations(IRoomChangeNotifier roomChangeNotifier);
	
	@Override
	protected void initialize() {
		
		doRegistrations(getOriApplicationContext().getOriRoomChangeNotifier());
		
		try (final var dataController = getOriApplicationContext().createDataController()) {
			getOriGUI()
			.pushLayerWithRootControl(
				new VerticalStack()
				.setRole(ContainerRole.OVERALL_CONTAINER)
				.addControl(
					new VerticalStack()
					.addControl(
						new Label()
						.setRole(LabelRole.TITLE)
						.setText(getApplicationName()),
						new Label()
						.setText(PAGE_SESSION_HELPER.getUserLabelText(dataController, this))
					),
					createMainControl(dataController)
				)
			);
		}
	}
}
