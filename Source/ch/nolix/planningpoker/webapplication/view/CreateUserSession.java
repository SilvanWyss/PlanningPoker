package ch.nolix.planningpoker.webapplication.view;

import ch.nolix.core.container.singlecontainer.SingleContainer;
import ch.nolix.planningpoker.webapplication.createusercomponent.CreateUserComponent;
import ch.nolix.planningpokerapi.logicapi.applicationcontextapi.IDataAdapter;
import ch.nolix.systemapi.webguiapi.mainapi.IControl;

public final class CreateUserSession extends PageSession {
	
	public CreateUserSession() {
		super(new SingleContainer<>());
	}
	
	@Override
	protected IControl<?, ?> createMainControl(final IDataAdapter dataAdapter) {
		return
		new CreateUserComponent(
			this,
			SelectRoomSession::withUserId,
			PokerSession::withUserIdAndRoomId
		)
		.getStoredControl();
	}
	
	@Override
	protected void noteSelfChange() {
		refreshIfDoesNotHaveOpenDialog();
	}
}
