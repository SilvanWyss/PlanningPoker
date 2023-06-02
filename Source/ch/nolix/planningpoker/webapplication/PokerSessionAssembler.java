package ch.nolix.planningpoker.webapplication;

import ch.nolix.core.commontype.commontypeconstant.StringCatalogue;
import ch.nolix.planningpokerapi.applicationcontextapi.IApplicationContext;
import ch.nolix.planningpokerapi.datamodelapi.IRoom;
import ch.nolix.planningpokerapi.datamodelapi.IRoomVisit;
import ch.nolix.system.webgui.container.GridContainer;
import ch.nolix.system.webgui.control.Button;
import ch.nolix.system.webgui.linearcontainer.HorizontalStack;
import ch.nolix.systemapi.webguiapi.mainapi.IControl;

public final class PokerSessionAssembler {
	
	private static final PokerSessionHelper POKER_SESSION_HELPER = new PokerSessionHelper();
	
	public IControl<?, ?> createEstimationCardsControl(
		final IRoomVisit roomVisit,
		final IApplicationContext applicationContext
	) {
		return
		new HorizontalStack()
		.addControl(
			createDeleteEstimationCardControl(roomVisit, applicationContext),
			createEstimationCardControl(roomVisit, 0, applicationContext),
			createEstimationCardControl(roomVisit, 1, applicationContext),
			createEstimationCardControl(roomVisit, 2, applicationContext),
			createEstimationCardControl(roomVisit, 3, applicationContext),
			createEstimationCardControl(roomVisit, 5, applicationContext),
			createEstimationCardControl(roomVisit, 8, applicationContext),
			createEstimationCardControl(roomVisit, 13, applicationContext),
			createEstimationCardControl(roomVisit, 21, applicationContext),
			createEstimationCardControl(roomVisit, 34, applicationContext),
			createEstimationCardControl(roomVisit, 55, applicationContext),
			createEstimationCardControl(roomVisit, 89, applicationContext),
			createInfiniteEstimationCardControl(roomVisit, applicationContext)
		);
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
	
	private IControl<?, ?> createDeleteEstimationCardControl(
		final IRoomVisit roomVisit,
		final IApplicationContext applicationContext
	) {
		return
		new Button()
		.setText("\u2715")
		.setLeftMouseButtonPressAction(
			() -> POKER_SESSION_HELPER.deleteEstimationAndUpdate(roomVisit.getId(), applicationContext)
		);
	}
	
	private IControl<?, ?> createEstimationCardControl(
		final IRoomVisit roomVisit,
		final int estimationInStoryPoints,
		final IApplicationContext applicationContext
	) {
		
		final var estimationCardButton =
		new Button()
		.setText(String.valueOf(estimationInStoryPoints))
		.setLeftMouseButtonPressAction(
			() ->
			POKER_SESSION_HELPER.setEstimationInStoryPointsAndUpdate(
				roomVisit.getId(),
				estimationInStoryPoints,
				applicationContext
			)
		);
		
		if (roomVisit.hasEstimationInStorypoints() && roomVisit.getEstimationInStoryPoints() == estimationInStoryPoints) {
			estimationCardButton.setToken("currentEstimation");
		}
		
		return estimationCardButton;
	}
	
	private IControl<?, ?> createInfiniteEstimationCardControl(
		final IRoomVisit roomVisit,
		final IApplicationContext applicationContext
	) {
		
		final var infiniteEstimationCardButton =
		new Button()
		.setText(StringCatalogue.INFINITY)
		.setLeftMouseButtonPressAction(
			() -> POKER_SESSION_HELPER.setInfiniteEstimationAndUpdate(roomVisit.getId(), applicationContext)
		);
		
		if (roomVisit.hasInfiniteEstimation()) {
			infiniteEstimationCardButton.setToken("currentEstimation");
		}
		
		return infiniteEstimationCardButton;
	}
}
