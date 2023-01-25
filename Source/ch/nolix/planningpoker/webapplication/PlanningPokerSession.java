package ch.nolix.planningpoker.webapplication;

import ch.nolix.core.errorcontrol.exception.WrapperException;
import ch.nolix.planningpokerapi.applicationcontextapi.IApplicationContext;
import ch.nolix.planningpokerapi.applicationcontextapi.IApplicationController;
import ch.nolix.system.application.webapplication.BackendWebClientSession;
import ch.nolix.system.webgui.control.Label;
import ch.nolix.system.webgui.linearcontainer.HorizontalStack;
import ch.nolix.system.webgui.linearcontainer.VerticalStack;
import ch.nolix.systemapi.webguiapi.controlapi.LabelRole;
import ch.nolix.systemapi.webguiapi.mainapi.IControl;

public abstract class PlanningPokerSession extends BackendWebClientSession<IApplicationContext> {
	
	protected abstract IControl<?, ?> createMainControl(
		IApplicationController applicationController
	);
	
	protected final String getUserId() {
		return getRefParentClient().getSessionVariableValueByKey("userId");
	}
	
	protected final boolean hasUserId() {
		return getRefParentClient().containsSessionVariableWithKey("userId");
	}
	
	@Override
	protected void initialize() {
		try (final var applicationController = getRefApplicationContext().createApplicationController()) {
			getRefGUI().pushLayerWithRootControl(createRootControl(applicationController));
		} catch (final Exception exception) {
			throw WrapperException.forError(exception);
		}
	}
	
	private IControl<?, ?> createHeaderControl(final IApplicationController applicationController) {
		
		final var headerControl =
		new HorizontalStack()
		.addControl(
			new Label()
			.setRole(LabelRole.TITLE)
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
		
		return new Label().setText("you: " + user.getName());
	}
}
