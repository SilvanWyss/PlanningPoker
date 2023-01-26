package ch.nolix.planningpoker.webapplication;

import ch.nolix.planningpokerapi.applicationcontextapi.IApplicationContext;
import ch.nolix.planningpokerapi.applicationcontextapi.IDataController;
import ch.nolix.system.application.webapplication.BackendWebClientSession;
import ch.nolix.system.webgui.control.Label;
import ch.nolix.system.webgui.linearcontainer.HorizontalStack;
import ch.nolix.system.webgui.linearcontainer.VerticalStack;
import ch.nolix.systemapi.webguiapi.controlapi.LabelRole;
import ch.nolix.systemapi.webguiapi.mainapi.IControl;

public abstract class PlanningPokerSession extends BackendWebClientSession<IApplicationContext> {
	
	protected abstract IControl<?, ?> createMainControl(IDataController dataController);
	
	protected final String getUserId() {
		return getRefParentClient().getSessionVariableValueByKey("userId");
	}
	
	protected final boolean hasUserId() {
		return getRefParentClient().containsSessionVariableWithKey("userId");
	}
	
	@Override
	protected void initialize() {
		
		final var applicationController = getRefApplicationContext().createApplicationController();
		
		try (final var dataController = applicationController.createDataController()) {
			getRefGUI().pushLayerWithRootControl(createRootControl(dataController));
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
		
		if (getRefParentClient().containsSessionVariableWithKey("userId")) {
			
			final var userId = getRefParentClient().getSessionVariableValueByKey("userId");
			
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
		
		final var user = dataController.getRefUserById(userId);
		
		return new Label().setText("you: " + user.getName());
	}
}
