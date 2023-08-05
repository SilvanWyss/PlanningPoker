package ch.nolix.planningpoker.webapplication.view;

import java.util.Locale;

import ch.nolix.core.commontype.commontypeconstant.StringCatalogue;
import ch.nolix.planningpoker.datamodel.dataevaluator.RoomVisitEvaluator;
import ch.nolix.planningpoker.logic.analysis.RoomAnalysis;
import ch.nolix.planningpoker.webapplication.controller.PokerController;
import ch.nolix.planningpokerapi.datamodelapi.schemaapi.IRoom;
import ch.nolix.planningpokerapi.datamodelapi.schemaapi.IRoomVisit;
import ch.nolix.planningpokerapi.logicapi.analysisapi.IRoomAnalysis;
import ch.nolix.planningpokerapi.logicapi.applicationcontextapi.IPlanningPokerContext;
import ch.nolix.system.webgui.atomiccontrol.Button;
import ch.nolix.system.webgui.container.Grid;
import ch.nolix.system.webgui.linearcontainer.HorizontalStack;
import ch.nolix.system.webgui.linearcontainer.VerticalStack;
import ch.nolix.systemapi.webguiapi.mainapi.IControl;

public final class PokerSessionAssembler {
	
	private static final PokerController POKER_SESSION_HELPER = new PokerController();
	
	private static final RoomVisitEvaluator ROOM_VISIT_EVALUATOR = new RoomVisitEvaluator();
	
	public IControl<?, ?> createEstimateCardsControl(
		final IRoomVisit roomVisit,
		final IPlanningPokerContext planningPokerContext
	) {
		return
		new VerticalStack()
		.addControl(
			new HorizontalStack()
			.addControl(
				createDeleteEstimateCardControl(roomVisit, planningPokerContext),
				createEstimateCardControl(roomVisit, 0, planningPokerContext),
				createEstimateCardControl(roomVisit, 0.5, planningPokerContext),
				createEstimateCardControl(roomVisit, 1, planningPokerContext),
				createEstimateCardControl(roomVisit, 2, planningPokerContext),
				createEstimateCardControl(roomVisit, 3, planningPokerContext),
				createEstimateCardControl(roomVisit, 5, planningPokerContext)
			),
			new HorizontalStack()
			.addControl(
				createEstimateCardControl(roomVisit, 8, planningPokerContext),
				createEstimateCardControl(roomVisit, 13, planningPokerContext),
				createEstimateCardControl(roomVisit, 21, planningPokerContext),
				createEstimateCardControl(roomVisit, 34, planningPokerContext),
				createEstimateCardControl(roomVisit, 55, planningPokerContext),
				createEstimateCardControl(roomVisit, 89, planningPokerContext),
				createInfiniteEstimateCardControl(roomVisit, planningPokerContext)
			)
		);
	}
	
	public IControl<?, ?> createEstimatesControl(final IRoom room) {
		
		final var estimatesGridContainer = new Grid();
		
		final var roomCreator = room.getStoredParentCreator();
		var rowIndex = 1;
		for (final var rv : room.getStoredRoomVisits()) {
			
			final var visitor = rv.getStoredVisitor();
			
			if (!visitor.hasId(roomCreator.getId()) || ROOM_VISIT_EVALUATOR.hasEstimate(rv)) {
				
				final var visitorName = visitor.getName();
				estimatesGridContainer.insertTextAtRowAndColumn(rowIndex, 1, visitorName);
				
				final var estimateText = POKER_SESSION_HELPER.getEstimateText(rv);
				estimatesGridContainer.insertTextAtRowAndColumn(rowIndex, 2, estimateText);
				
				rowIndex++;
			}
		}
		
		return estimatesGridContainer;
	}
	
	public IControl<?, ?> createRoomAnalysisControl(final IRoom room) {
		
		if (room.hasSetEstimatesVisible()) {
			return createRoomAnalysisControlWhenEstimatesAreVisible(room);
		}
		
		return new Grid();
	}
	
	private IControl<?, ?> createDeleteEstimateCardControl(
		final IRoomVisit roomVisit,
		final IPlanningPokerContext planningPokerContext
	) {
		
		final var deleteEstimateCardControl =
		new Button()
		.addToken("card")
		.setText("\u2715")
		.setLeftMouseButtonPressAction(
			() -> POKER_SESSION_HELPER.deleteEstimateAndUpdate(roomVisit.getId(), planningPokerContext)
		);
		
		if (!ROOM_VISIT_EVALUATOR.hasEstimate(roomVisit)) {
			deleteEstimateCardControl.addToken("activated_card");
		}
		
		return deleteEstimateCardControl;
	}
	
	private IControl<?, ?> createEstimateCardControl(
		final IRoomVisit roomVisit,
		final double estimateInStoryPoints,
		final IPlanningPokerContext planningPokerContext
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
				planningPokerContext
			)
		);
		
		if (roomVisit.hasEstimateInStorypoints() && roomVisit.getEstimateInStoryPoints() == estimateInStoryPoints) {
			estimateCardButton.addToken("activated_card");
		}
		
		return estimateCardButton;
	}
	
	private IControl<?, ?> createInfiniteEstimateCardControl(
		final IRoomVisit roomVisit,
		final IPlanningPokerContext planningPokerContext
	) {
		
		final var infiniteEstimateCardButton =
		new Button()
		.addToken("card")
		.setText(StringCatalogue.INFINITY)
		.setLeftMouseButtonPressAction(
			() -> POKER_SESSION_HELPER.setInfiniteEstimateAndUpdate(roomVisit.getId(), planningPokerContext)
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
		new Grid()
		.insertTextAtRowAndColumn(1, 1, StringCatalogue.LONG_LEFT_RIGHT_ARROW)
		.insertTextAtRowAndColumn(1, 2, rangeText)
		.insertTextAtRowAndColumn(2, 1, StringCatalogue.UPPERCASE_DELTA)
		.insertTextAtRowAndColumn(2, 2, differenceText)
		.insertTextAtRowAndColumn(3, 1, StringCatalogue.AVERAGE)
		.insertTextAtRowAndColumn(3, 2, averageText)
		.insertTextAtRowAndColumn(4, 1, StringCatalogue.AVERAGE + StringCatalogue.UPPERCASE_DELTA)
		.insertTextAtRowAndColumn(4, 2,  averageDeviationFromAverageInStoryPointsText);
	}
}
