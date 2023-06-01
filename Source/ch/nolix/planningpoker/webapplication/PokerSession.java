package ch.nolix.planningpoker.webapplication;

import ch.nolix.planningpokerapi.applicationcontextapi.IDataController;
import ch.nolix.planningpokerapi.datamodelapi.IRoomVisit;
import ch.nolix.system.webgui.control.Label;
import ch.nolix.system.webgui.linearcontainer.HorizontalStack;
import ch.nolix.system.webgui.linearcontainer.VerticalStack;
import ch.nolix.systemapi.webguiapi.controlapi.LabelRole;
import ch.nolix.systemapi.webguiapi.mainapi.IControl;

public final class PokerSession extends PageSession {
	
	private static final PokerSessionAssembler POKER_SESSION_ASSEMBLER = new PokerSessionAssembler();
	
	private static final PokerSessionHelper POKER_SESSION_HELPER = new PokerSessionHelper();
	
	@Override
	protected IControl<?, ?> createMainControl(final IDataController dataController) {
		
		final var userId = getOriParentClient().getCookieValueByCookieNameOrNull("userId");
		final var user = dataController.getOriUserById(userId);
		final var roomVisit = user.getOriCurrentRoomVisit();
		
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
			new HorizontalStack()
			.addControl(
				new VerticalStack()
				.addControl(
					POKER_SESSION_ASSEMBLER.createEstimationCardsControl(roomVisit),
					POKER_SESSION_ASSEMBLER.createEstimationsControl(roomVisit.getOriParentRoom())
				)
			)
		);
	}
}
