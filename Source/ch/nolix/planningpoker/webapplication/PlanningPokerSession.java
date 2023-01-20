package ch.nolix.planningpoker.webapplication;

import ch.nolix.planningpokerapi.applicationcontextapi.IApplicationContext;
import ch.nolix.planningpokerapi.applicationcontextapi.IApplicationController;
import ch.nolix.system.application.webapplication.BackendWebClientSession;
import ch.nolix.system.webgui.control.Text;
import ch.nolix.system.webgui.linearcontainer.HorizontalStack;
import ch.nolix.system.webgui.linearcontainer.VerticalStack;
import ch.nolix.systemapi.webguiapi.controlapi.TextRole;
import ch.nolix.systemapi.webguiapi.mainapi.IControl;

public abstract class PlanningPokerSession extends BackendWebClientSession<IApplicationContext> {
	
	@Override
	protected void initialize() {
		
		final var applicationController = getRefApplicationContext().createApplicationController();
		
		getRefGUI().pushLayerWithRootControl(createRootControl(applicationController));
	}
	
	protected abstract IControl<?, ?> createMainControl(
		IApplicationController applicationController
	);
	
	private IControl<?, ?> createHeaderControl(final IApplicationController applicationController) {
		
		final var headerControl =
		new HorizontalStack()
		.addControl(
			new Text()
			.setRole(TextRole.TITLE)
			.setText(getApplicationName())
		);
		
		final var userId = getRefParentClient().getCookieValueByCookieNameOrNull("userid");
		
		if (userId != null) {
			headerControl.addControl(createUserControl(userId, applicationController));
		}
		
		return headerControl;
	}
	
	private IControl<?, ?> createRootControl(
		final IApplicationController applicationController
	) {
		return
		new VerticalStack()
		.addControl(
			createHeaderControl(applicationController),
			createMainControl(applicationController)
		);
	}
	
	private IControl<?, ?> createUserControl(final String userId, final IApplicationController applicationController) {
		
		final var user = applicationController.getRefDataController().getRefUserById(userId);
		
		return new Text().setText("you: " + user.getName());
	}
}
