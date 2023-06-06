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
	
	public IControl<?, ?> createEstimateCardsControl(
		final IRoomVisit roomVisit,
		final IApplicationContext applicationContext
	) {
		return
		new HorizontalStack()
		.addControl(
			createDeleteEstimateCardControl(roomVisit, applicationContext),
			createEstimateCardControl(roomVisit, 0, applicationContext),
			createEstimateCardControl(roomVisit, 1, applicationContext),
			createEstimateCardControl(roomVisit, 2, applicationContext),
			createEstimateCardControl(roomVisit, 3, applicationContext),
			createEstimateCardControl(roomVisit, 5, applicationContext),
			createEstimateCardControl(roomVisit, 8, applicationContext),
			createEstimateCardControl(roomVisit, 13, applicationContext),
			createEstimateCardControl(roomVisit, 21, applicationContext),
			createEstimateCardControl(roomVisit, 34, applicationContext),
			createEstimateCardControl(roomVisit, 55, applicationContext),
			createEstimateCardControl(roomVisit, 89, applicationContext),
			createInfiniteEstimateCardControl(roomVisit, applicationContext)
		);
	}
	
	public IControl<?, ?> createEstimatesControl(final IRoom room) {
		
		final var estimatesGridContainer = new GridContainer();
		
		var rowIndex = 1;
		for (final var rv : room.getOriRoomVisits()) {
			
			final var visitorName = rv.getOriVisitor().getName();
			estimatesGridContainer.insertTextAtRowAndColumn(rowIndex, 1, visitorName);
			
			final var estimateText = POKER_SESSION_HELPER.getEstimateText(rv);
			estimatesGridContainer.insertTextAtRowAndColumn(rowIndex, 2, estimateText);
			
			rowIndex++;
		}
		
		return estimatesGridContainer;
	}
	
	private IControl<?, ?> createDeleteEstimateCardControl(
		final IRoomVisit roomVisit,
		final IApplicationContext applicationContext
	) {
		return
		new Button()
		.setText("\u2715")
		.setLeftMouseButtonPressAction(
			() -> POKER_SESSION_HELPER.deleteEstimateAndUpdate(roomVisit.getId(), applicationContext)
		);
	}
	
	private IControl<?, ?> createEstimateCardControl(
		final IRoomVisit roomVisit,
		final int estimateInStoryPoints,
		final IApplicationContext applicationContext
	) {
		
		final var estimateCardButton =
		new Button()
		.setText(String.valueOf(estimateInStoryPoints))
		.setLeftMouseButtonPressAction(
			() ->
			POKER_SESSION_HELPER.setEstimateInStoryPointsAndUpdate(
				roomVisit.getId(),
				estimateInStoryPoints,
				applicationContext
			)
		);
		
		if (roomVisit.hasEstimateInStorypoints() && roomVisit.getEstimateInStoryPoints() == estimateInStoryPoints) {
			estimateCardButton.setToken("currentEstimate");
		}
		
		return estimateCardButton;
	}
	
	private IControl<?, ?> createInfiniteEstimateCardControl(
		final IRoomVisit roomVisit,
		final IApplicationContext applicationContext
	) {
		
		final var infiniteEstimateCardButton =
		new Button()
		.setText(StringCatalogue.INFINITY)
		.setLeftMouseButtonPressAction(
			() -> POKER_SESSION_HELPER.setInfiniteEstimateAndUpdate(roomVisit.getId(), applicationContext)
		);
		
		if (roomVisit.hasInfiniteEstimate()) {
			infiniteEstimateCardButton.setToken("currentEstimate");
		}
		
		return infiniteEstimateCardButton;
	}
}
