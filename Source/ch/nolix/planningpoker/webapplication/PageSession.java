package ch.nolix.planningpoker.webapplication;

import ch.nolix.planningpokerapi.applicationcontextapi.IApplicationContext;
import ch.nolix.planningpokerapi.applicationcontextapi.IDataController;
import ch.nolix.system.application.webapplication.BackendWebClientSession;
import ch.nolix.system.webgui.control.Label;
import ch.nolix.system.webgui.linearcontainer.HorizontalStack;
import ch.nolix.system.webgui.linearcontainer.VerticalStack;
import ch.nolix.systemapi.webguiapi.controlapi.LabelRole;
import ch.nolix.systemapi.webguiapi.mainapi.IControl;

public abstract class PageSession extends BackendWebClientSession<IApplicationContext> {
	
	protected abstract IControl<?, ?> createMainControl(IDataController dataController);
	
	protected final String getUserId() {
		return getOriParentClient().getCookieValueByCookieNameOrNull("userId");
	}
	
	protected final boolean hasUserId() {
		return getOriParentClient().containsSessionVariableWithKey("userId");
	}
	
	@Override
	protected void initialize() {
		try (final var dataController = getOriApplicationContext().createDataController()) {
			getOriGUI().pushLayerWithRootControl(createRootControl(dataController));
		}
	}
	
	private IControl<?, ?> createHeaderControl(final IDataController dataController) {
		
		final var headerControl =
		new HorizontalStack()
		.addControl(
			new Label()
			.setRole(LabelRole.TITLE)
			.setText(getApplicationName())
		);
		
		if (getOriParentClient().containsSessionVariableWithKey("userId")) {
			
			final var userId = getOriParentClient().getSessionVariableValueByKey("userId");
			
			headerControl.addControl(createUserControl(userId, dataController));
		}
		
		return headerControl;
	}
	
	private IControl<?, ?> createRootControl(final IDataController dataController) {
		return
		new VerticalStack()
		.addControl(
			createHeaderControl(dataController),
			createMainControl(dataController)
		);
	}
	
	private IControl<?, ?> createUserControl(final String userId, final IDataController dataController) {
		
		final var user = dataController.getOriUserById(userId);
		
		return new Label().setText("you: " + user.getName());
	}
}
