package ch.nolix.planningpoker.webapplication;

import java.util.Locale;

import ch.nolix.core.commontype.commontypeconstant.StringCatalogue;
import ch.nolix.planningpoker.analysis.RoomAnalysis;
import ch.nolix.planningpoker.dataevaluator.RoomVisitEvaluator;
import ch.nolix.planningpokerapi.analysisapi.IRoomAnalysis;
import ch.nolix.planningpokerapi.applicationcontextapi.IApplicationContext;
import ch.nolix.planningpokerapi.datamodelapi.IRoom;
import ch.nolix.planningpokerapi.datamodelapi.IRoomVisit;
import ch.nolix.system.webgui.container.GridContainer;
import ch.nolix.system.webgui.control.Button;
import ch.nolix.system.webgui.linearcontainer.HorizontalStack;
import ch.nolix.system.webgui.linearcontainer.VerticalStack;
import ch.nolix.systemapi.webguiapi.mainapi.IControl;

public final class PokerSessionAssembler {
	
	private static final PokerSessionHelper POKER_SESSION_HELPER = new PokerSessionHelper();
	
	private static final RoomVisitEvaluator ROOM_VISIT_EVALUATOR = new RoomVisitEvaluator();
	
	public IControl<?, ?> createEstimateCardsControl(
		final IRoomVisit roomVisit,
		final IApplicationContext applicationContext
	) {
		return
		new VerticalStack()
		.addControl(
			new HorizontalStack()
			.addControl(
				createDeleteEstimateCardControl(roomVisit, applicationContext),
				createEstimateCardControl(roomVisit, 0, applicationContext),
				createEstimateCardControl(roomVisit, 0.5, applicationContext),
				createEstimateCardControl(roomVisit, 1, applicationContext),
				createEstimateCardControl(roomVisit, 2, applicationContext),
				createEstimateCardControl(roomVisit, 3, applicationContext),
				createEstimateCardControl(roomVisit, 5, applicationContext)
			),
			new HorizontalStack()
			.addControl(
				createEstimateCardControl(roomVisit, 8, applicationContext),
				createEstimateCardControl(roomVisit, 13, applicationContext),
				createEstimateCardControl(roomVisit, 21, applicationContext),
				createEstimateCardControl(roomVisit, 34, applicationContext),
				createEstimateCardControl(roomVisit, 55, applicationContext),
				createEstimateCardControl(roomVisit, 89, applicationContext),
				createInfiniteEstimateCardControl(roomVisit, applicationContext)
			)
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
	
	public IControl<?, ?> createRoomAnalysisControl(final IRoom room) {
		
		if (room.hasSetEstimatesVisible()) {
			return createRoomAnalysisControlWhenEstimatesAreVisible(room);
		}
		
		return new GridContainer();
	}
	
	private IControl<?, ?> createDeleteEstimateCardControl(
		final IRoomVisit roomVisit,
		final IApplicationContext applicationContext
	) {
		
		final var deleteEstimateCardControl =
		new Button()
		.addToken("card")
		.setText("\u2715")
		.setLeftMouseButtonPressAction(
			() -> POKER_SESSION_HELPER.deleteEstimateAndUpdate(roomVisit.getId(), applicationContext)
		);
		
		if (!ROOM_VISIT_EVALUATOR.hasAnyEstimation(roomVisit)) {
			deleteEstimateCardControl.addToken("activated_card");
		}
		
		return deleteEstimateCardControl;
	}
	
	private IControl<?, ?> createEstimateCardControl(
		final IRoomVisit roomVisit,
		final double estimateInStoryPoints,
		final IApplicationContext applicationContext
	) {
		
		final var estimateCardButton =
		new Button()
		.addToken("card")
		.setText(POKER_SESSION_HELPER.getEstimateCardText(estimateInStoryPoints))
		.setLeftMouseButtonPressAction(
			() ->
			POKER_SESSION_HELPER.setEstimateInStoryPointsAndUpdate(
				roomVisit.getId(),
				estimateInStoryPoints,
				applicationContext
			)
		);
		
		if (roomVisit.hasEstimateInStorypoints() && roomVisit.getEstimateInStoryPoints() == estimateInStoryPoints) {
			estimateCardButton.addToken("activated_card");
		}
		
		return estimateCardButton;
	}
	
	private IControl<?, ?> createInfiniteEstimateCardControl(
		final IRoomVisit roomVisit,
		final IApplicationContext applicationContext
	) {
		
		final var infiniteEstimateCardButton =
		new Button()
		.addToken("card")
		.setText(StringCatalogue.INFINITY)
		.setLeftMouseButtonPressAction(
			() -> POKER_SESSION_HELPER.setInfiniteEstimateAndUpdate(roomVisit.getId(), applicationContext)
		);
		
		if (roomVisit.hasInfiniteEstimate()) {
			infiniteEstimateCardButton.addToken("activated_card");
		}
		
		return infiniteEstimateCardButton;
	}
	
	private IControl<?, ?> createRoomAnalysisControlWhenEstimatesAreVisible(final IRoom room) {
		
		final var roomAnalysis = RoomAnalysis.forRoom(room);
		
		return createRoomAnalysisControlWhenEstimatesAreVisible(roomAnalysis);
	}
	
	private IControl<?, ?> createRoomAnalysisControlWhenEstimatesAreVisible(final IRoomAnalysis roomAnalysis) {
		
		final var minEstimateInStoryPoints = roomAnalysis.getMinEstimateInStoryPointsOrZero();
		final var maxestimateInStoryPoints = roomAnalysis.getMaxEstimateInStoryPointsOrZero();
		
		final var averageText =
		String.format(Locale.ENGLISH, "%.1f", roomAnalysis.getAverageEstimateInStoryPointsOrZero());
		
		final var averageDeviationFromAverageInStoryPointsText =
		String.format(
			Locale.ENGLISH,
			"%.1f",
			roomAnalysis.getAverageDeviationFromAverageEstimateInStoryPointsOrZero()
		);
		
		final var rangeText =
		String.format(Locale.ENGLISH, "%.1f - %.1f", minEstimateInStoryPoints, maxestimateInStoryPoints);
		
		final var differenceText =
		String.format(Locale.ENGLISH, "%.1f", maxestimateInStoryPoints - minEstimateInStoryPoints);
		
		return
		new GridContainer()
		.insertTextAtRowAndColumn(1, 1, StringCatalogue.AVERAGE)
		.insertTextAtRowAndColumn(1, 2, averageText)
		.insertTextAtRowAndColumn(2, 1, StringCatalogue.AVERAGE + StringCatalogue.UPPERCASE_DELTA)
		.insertTextAtRowAndColumn(2, 2,  averageDeviationFromAverageInStoryPointsText)
		.insertTextAtRowAndColumn(3, 1, StringCatalogue.LONG_LEFT_RIGHT_ARROW)
		.insertTextAtRowAndColumn(3, 2, rangeText)
		.insertTextAtRowAndColumn(4, 1, StringCatalogue.UPPERCASE_DELTA)
		.insertTextAtRowAndColumn(4, 2, differenceText);
	}
}
