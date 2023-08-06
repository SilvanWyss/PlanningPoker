package ch.nolix.planningpoker.webapplication.userlinecomponent;

import ch.nolix.planningpokerapi.logicapi.applicationcontextapi.IDataAdapter;
import ch.nolix.planningpokerapi.logicapi.applicationcontextapi.IPlanningPokerContext;
import ch.nolix.system.application.component.ComponentWithDataAdapter;
import ch.nolix.system.application.webapplication.WebClientSession;
import ch.nolix.system.webgui.atomiccontrol.Button;
import ch.nolix.system.webgui.atomiccontrol.Label;
import ch.nolix.system.webgui.linearcontainer.HorizontalStack;
import ch.nolix.systemapi.webguiapi.mainapi.IControl;

public final class UserLineComponent
extends ComponentWithDataAdapter<UserLineController, IPlanningPokerContext, IDataAdapter> {
	
	public UserLineComponent(
		final String userId,
		final WebClientSession<IPlanningPokerContext> session,
		final IDataAdapter initialDataAdapter
	) {
		super(new UserLineController(userId, session), initialDataAdapter);
	}
	
	@Override
	protected IControl<?, ?> createControl(
		final UserLineController userLineController,
		final IDataAdapter dataAdapter
	) {
		return
		new HorizontalStack()
		.addControl(
			new Label()
			.setText("you: "),
			new Button()
			.setText(userLineController.getLoggedInUserName(dataAdapter))
			.setLeftMouseButtonPressAction(userLineController::openEditUserNameDialog)
		);
	}
	
	@Override
	protected void doRegistrations(final UserLineController footerController) {
		//Does nothing.
	}
}
