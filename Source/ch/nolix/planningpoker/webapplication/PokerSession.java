package ch.nolix.planningpoker.webapplication;

import ch.nolix.coreapi.programcontrolapi.triggeruniversalapi.CloseStateRequestableTriggerable;
import ch.nolix.planningpokerapi.applicationcontextapi.IDataController;
import ch.nolix.planningpokerapi.datamodelapi.IRoomVisit;
import ch.nolix.system.webgui.control.Button;
import ch.nolix.system.webgui.control.Label;
import ch.nolix.system.webgui.linearcontainer.HorizontalStack;
import ch.nolix.system.webgui.linearcontainer.VerticalStack;
import ch.nolix.systemapi.webguiapi.controlapi.LabelRole;
import ch.nolix.systemapi.webguiapi.mainapi.IControl;

public final class PokerSession extends PageSession implements CloseStateRequestableTriggerable {
	
	private static final PokerSessionAssembler POKER_SESSION_ASSEMBLER = new PokerSessionAssembler();
	
	private static final PokerSessionHelper POKER_SESSION_HELPER = new PokerSessionHelper();
	
	@Override
	public void trigger() {
		
		initialize();
		
		updateCounterpart();
	}
	
	@Override
	protected IControl<?, ?> createMainControl(final IDataController dataController) {
		
		final var userId = getOriParentClient().getCookieValueByCookieNameOrNull("userId");
		final var user = dataController.getOriUserById(userId);
		final var roomVisit = user.getOriCurrentRoomVisit();
		final var room = roomVisit.getOriParentRoom();
		final var roomChangeNotifier = getOriApplicationContext().getOriRoomChangeNotifier();
		
		roomChangeNotifier.registerSubscriberForRoomChange(room.getId(), this);
		
		return createMainControl(roomVisit);
	}
	
	private IControl<?, ?> createMainControl(final IRoomVisit roomVisit) {
		return
		new VerticalStack()
		.addControl(
			new Label()
			.setRole(LabelRole.LEVEL1_HEADER)
			.setText("Room " + roomVisit.getOriParentRoom().getNumber()),
			new Label()
			.setText(POKER_SESSION_HELPER.getCaptainInfoText(roomVisit)),
			new Button()
			.setText("Show/hide estimations")
			.setVisibility(POKER_SESSION_HELPER.isAllowedToConfigureRoom(roomVisit, this))
			.setLeftMouseButtonPressAction(
				() ->
				POKER_SESSION_HELPER.toggleEstimationVisibilityAndUpdate(
					roomVisit.getOriParentRoom().getId(),
					getOriApplicationContext()
				)
			),
			new HorizontalStack()
			.addControl(
				new VerticalStack()
				.addControl(
					POKER_SESSION_ASSEMBLER.createEstimationCardsControl(roomVisit, getOriApplicationContext()),
					POKER_SESSION_ASSEMBLER.createEstimationsControl(roomVisit.getOriParentRoom())
				)
			)
		);
	}
}
