package ch.nolix.planningpoker.webapplication.view;

import ch.nolix.core.container.singlecontainer.SingleContainer;
import ch.nolix.planningpoker.webapplication.selectroomcomponent.SelectRoomComponent;
import ch.nolix.planningpokerapi.logicapi.applicationcontextapi.IDataAdapter;
import ch.nolix.planningpokerapi.logicapi.applicationcontextapi.IRoomChangeNotifier;
import ch.nolix.systemapi.webguiapi.mainapi.IControl;

public final class SelectRoomSession extends PageSession {
	
	public static SelectRoomSession withUserId(final String userId) {
		return new SelectRoomSession(userId);
	}
	
	private SelectRoomSession(final String userId) {
		super(new SingleContainer<>(userId));
	}
	
	@Override
	protected IControl<?, ?> createMainControl(final IDataAdapter dataAdapter) {
		return new SelectRoomComponent(getUserId(), this).getStoredControl();
	}
	
	@Override
	protected void doRegistrations(final IRoomChangeNotifier roomChangeNotifier) {
		//Does nothing.
	}
	
	@Override
	protected void noteSelfChange() {
		refreshIfDoesNotHaveOpenDialog();
	}
}
