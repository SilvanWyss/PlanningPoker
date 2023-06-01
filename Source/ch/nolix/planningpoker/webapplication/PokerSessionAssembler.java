package ch.nolix.planningpoker.webapplication;

import ch.nolix.planningpokerapi.datamodelapi.IRoom;
import ch.nolix.planningpokerapi.datamodelapi.IRoomVisit;
import ch.nolix.system.webgui.container.GridContainer;
import ch.nolix.system.webgui.control.Button;
import ch.nolix.system.webgui.linearcontainer.HorizontalStack;
import ch.nolix.systemapi.webguiapi.mainapi.IControl;

public final class PokerSessionAssembler {
	
	private static final PokerSessionHelper POKER_SESSION_HELPER = new PokerSessionHelper();
	
	public IControl<?, ?> createEstimationCardsControl(final IRoomVisit roomVisit) {
		return
		new HorizontalStack()
		.addControl(createEstimationCardControl(roomVisit, 0))
		.addControl(createEstimationCardControl(roomVisit, 1))
		.addControl(createEstimationCardControl(roomVisit, 2))
		.addControl(createEstimationCardControl(roomVisit, 3))
		.addControl(createEstimationCardControl(roomVisit, 5))
		.addControl(createEstimationCardControl(roomVisit, 8))
		.addControl(createEstimationCardControl(roomVisit, 13))
		.addControl(createEstimationCardControl(roomVisit, 21))
		.addControl(createEstimationCardControl(roomVisit, 34))
		.addControl(createEstimationCardControl(roomVisit, 55))
		.addControl(createEstimationCardControl(roomVisit, 89));
	}
	
	public IControl<?, ?> createEstimationsControl(final IRoom room) {
		
		final var estimationsGridContainer = new GridContainer();
		
		var rowIndex = 1;
		for (final var rv : room.getOriRoomVisits()) {
			
			final var visitorName = rv.getOriVisitor().getName();
			estimationsGridContainer.insertTextAtRowAndColumn(rowIndex, 1, visitorName);
			
			final var estimationText = POKER_SESSION_HELPER.getEstimationText(rv);
			estimationsGridContainer.insertTextAtRowAndColumn(rowIndex, 2, estimationText);
			
			rowIndex++;
		}
		
		return estimationsGridContainer;
	}
	
	private IControl<?, ?> createEstimationCardControl(final IRoomVisit roomVisit, final int estimationInStoryPoints) {
		return
		new Button()
		.setText(String.valueOf(estimationInStoryPoints))
		.setLeftMouseButtonPressAction(() -> POKER_SESSION_HELPER.getEstimationText(roomVisit));
	}
}
