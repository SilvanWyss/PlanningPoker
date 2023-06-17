package ch.nolix.planningpoker.webapplication;

import ch.nolix.core.container.singlecontainer.SingleContainer;
import ch.nolix.core.errorcontrol.validator.GlobalValidator;
import ch.nolix.coreapi.programcontrolapi.triggeruniversalapi.Refreshable;
import ch.nolix.planningpokerapi.applicationcontextapi.IApplicationContext;
import ch.nolix.planningpokerapi.applicationcontextapi.IDataController;
import ch.nolix.planningpokerapi.applicationcontextapi.IRoomChangeNotifier;
import ch.nolix.system.application.webapplication.WebClientSession;
import ch.nolix.system.webgui.control.Button;
import ch.nolix.system.webgui.control.Label;
import ch.nolix.system.webgui.linearcontainer.HorizontalStack;
import ch.nolix.system.webgui.linearcontainer.VerticalStack;
import ch.nolix.systemapi.webguiapi.containerapi.ContainerRole;
import ch.nolix.systemapi.webguiapi.controlapi.LabelRole;
import ch.nolix.systemapi.webguiapi.mainapi.IControl;

public abstract class PageSession extends WebClientSession<IApplicationContext> implements Refreshable {
	
	private static final PageSessionHelper PAGE_SESSION_HELPER = new PageSessionHelper();
	
	private static final PageSessionStyleCreator PAGE_SESSION_STYLE_CREATOR = new PageSessionStyleCreator();
	
	private final SingleContainer<String> userIdContainer;
	
	protected PageSession(final SingleContainer<String> userIdContainer) {
		
		GlobalValidator.assertThat(userIdContainer).thatIsNamed("user id container").isNotNull();
		
		this.userIdContainer = userIdContainer;
	}
	
	public final String getUserId() {
		return userIdContainer.getOriElement();
	}
	
	public final boolean hasUserId() {
		return userIdContainer.containsAny();
	}
	
	@Override
	public final void refresh() {
		
		clearGui();
		
		fillUpGui();
		
		updateCounterpart();
	}
	
	protected abstract IControl<?, ?> createMainControl(IDataController dataController);
	
	protected abstract void doRegistrations(IRoomChangeNotifier roomChangeNotifier);
	
	@Override
	protected final void initialize() {
		
		doRegistrations(getOriApplicationContext().getOriRoomChangeNotifier());
		
		fillUpGui();
	}
	
	private void clearGui() {
		getOriGUI().clear();
	}
	
	private void fillUpGui() {
		try (final var dataController = getOriApplicationContext().createDataController()) {
			getOriGUI()
			.pushLayerWithRootControl(
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
							new Label()
							.setText(PAGE_SESSION_HELPER.getUserLabelText(userIdContainer, dataController)),
							new Button()
							.setVisibility(hasUserId())
							.setText("Edit user name")
							.setLeftMouseButtonPressAction(
								() -> PAGE_SESSION_HELPER.openEditUserNameDialog(getUserId(), this)
							)
						)
					),
					createMainControl(dataController)
				)
			)
			.setStyle(PAGE_SESSION_STYLE_CREATOR.createPageSessionStyle());
		}
	}
}
