package ch.nolix.planningpoker.webapplication.controller;

import java.util.Locale;

import ch.nolix.core.commontype.commontypeconstant.StringCatalogue;
import ch.nolix.planningpoker.datamodel.dataevaluator.RoomEvaluator;
import ch.nolix.planningpoker.datamodel.dataevaluator.RoomVisitEvaluator;
import ch.nolix.planningpokerapi.applicationcontextapi.IPlanningPokerContext;
import ch.nolix.planningpokerapi.datamodelapi.dataevaluatorapi.IRoomEvaluator;
import ch.nolix.planningpokerapi.datamodelapi.dataevaluatorapi.IRoomVisitEvaluator;
import ch.nolix.planningpokerapi.datamodelapi.schemaapi.IRoom;
import ch.nolix.planningpokerapi.datamodelapi.schemaapi.IRoomVisit;
import ch.nolix.planningpokerapi.webapplicationapi.sessionfactoryapi.ISelectRoomSessionFactory;
import ch.nolix.system.application.webapplication.WebClientSession;
import ch.nolix.system.webgui.dialog.ShowValueDialogFactory;
import ch.nolix.system.webgui.dialog.YesNoDialogFactory;

public final class PokerController {
	
	private static final IRoomEvaluator ROOM_EVALUATOR = new RoomEvaluator();
	
	private static final RoomHyperlinkCreator ROOM_HYPERLINK_CREATOR = new RoomHyperlinkCreator();
	
	private static final IRoomVisitEvaluator ROOM_VISIT_EVALUATOR = new RoomVisitEvaluator();
		
	private static final ShowValueDialogFactory SHOW_VALUE_DIALOG_FACTORY = new ShowValueDialogFactory();
	
	private static final YesNoDialogFactory YES_NO_DIALOG_FACTORY = YesNoDialogFactory.INSTANCE;
	
	public void deleteEstimateAndUpdate(final String roomVisitId, final IPlanningPokerContext planningPokerContext) {
		try (final var dataController = planningPokerContext.createDataController()) {
			
			final var roomVisit = dataController.getOriRoomVisitById(roomVisitId);
			roomVisit.deleteEstimate();
			dataController.saveChanges();
			
			final var room = roomVisit.getOriParentRoom();	
			planningPokerContext.getOriRoomChangeNotifier().noteRoomChange(room.getId());
		}
	}
	
	public String getCaptainInfoText(final IRoomVisit roomVisit) {
		
		final var roomCreator = roomVisit.getOriParentRoom().getOriParentCreator();
		final var user = roomVisit.getOriVisitor();
		
		if (roomCreator.hasId(user.getId())) {
			return "You are our captain.";
		}
		
		return (roomCreator.getName() + " is our captain.");
	}
	
	public String getEstimateCardText(final double estimateInStoryPoints) {
		
		if (estimateInStoryPoints == 0.5) {
			return "0.5";
		}
		
		return String.format(Locale.ENGLISH, "%.0f", estimateInStoryPoints);
	}
	
	public String getEstimateText(final IRoomVisit roomVisit) {
		
		if (roomVisit.getOriParentRoom().hasSetEstimatesVisible()) {
			return getEstimateTextWhenEstimateIsVisible(roomVisit);
		}
		
		return getEstimateTextWhenEstimateIsInvisible(roomVisit);
	}
	
	public boolean isAllowedToConfigureRoom(final IRoomVisit roomVisit) {
		
		final var visitor = roomVisit.getOriVisitor();
		final var room = roomVisit.getOriParentRoom();
		final var roomCreator = room.getOriParentCreator();
		
		return visitor.hasId(roomCreator.getId());
	}
	
	public void openDeleteEstimatesDialog(
		final String roomId,
		final WebClientSession<IPlanningPokerContext> webClientSession
	) {
		
		final var applicationContext = webClientSession.getOriApplicationContext();
		
		try (final var dataController = applicationContext.createDataController()) {
			
			final var room = dataController.getOriRoomById(roomId);
			
			if (ROOM_EVALUATOR.containsEstimate(room)) {
				openDeleteEstimatesDialogWhenRoomContainsEstimates(roomId, webClientSession);
			}
		}
	}
	
	public void openGoToOtherRoomDialog(
		final String userId,
		final WebClientSession<IPlanningPokerContext> webClientSession,
		final ISelectRoomSessionFactory selectRoomSessionFactory
	) {
		
		final var goToOtherRoomDialog =
		YES_NO_DIALOG_FACTORY.createYesNoDialogWithYesNoQuestionAndConfirmAction(
			"Do you really want to leave the current room?",
			() -> goToOtherRoomAndUpdate(userId, webClientSession, selectRoomSessionFactory)
		);
		
		webClientSession.getOriGUI().pushLayer(goToOtherRoomDialog);
	}
	
	public void openShareRoomDialog(final IRoom room, final WebClientSession<IPlanningPokerContext> webClientSession) {
		
		final var application = webClientSession.getOriParentClient().getOriParentApplication();
		
		final var roomHyperlink = ROOM_HYPERLINK_CREATOR.createHyperlinkToRoom(room, application);
		
		final var shareRoomDialog =
		SHOW_VALUE_DIALOG_FACTORY.createShowValueDialogForValueNameAndValue("Link to room", roomHyperlink);
		
		webClientSession.getOriGUI().pushLayer(shareRoomDialog);
	}
	
	public void setEstimateInStoryPointsAndUpdate(
		final String roomVisitId,
		final double estimateInStoryPoints,
		final IPlanningPokerContext planningPokerContext
	) {
		try (final var dataController = planningPokerContext.createDataController()) {
			
			final var roomVisit = dataController.getOriRoomVisitById(roomVisitId);
			roomVisit.setEstimateInStoryPoints(estimateInStoryPoints);
			dataController.saveChanges();
			
			final var room = roomVisit.getOriParentRoom();	
			planningPokerContext.getOriRoomChangeNotifier().noteRoomChange(room.getId());
		}
	}
	
	public void setInfiniteEstimateAndUpdate(
		final String roomVisitId,
		final IPlanningPokerContext planningPokerContext
	) {
		try (final var dataController = planningPokerContext.createDataController()) {
			
			final var roomVisit = dataController.getOriRoomVisitById(roomVisitId);
			roomVisit.setInfiniteEstimate();
			dataController.saveChanges();
			
			final var room = roomVisit.getOriParentRoom();	
			planningPokerContext.getOriRoomChangeNotifier().noteRoomChange(room.getId());
		}
	}
	
	public void toggleEstimateVisibilityAndUpdate(final String roomId, final IPlanningPokerContext planningPokerContext) {
		try (final var dataController = planningPokerContext.createDataController()) {
			
			final var room = dataController.getOriRoomById(roomId);
			
			if (room.hasSetEstimatesInvisible()) {
				room.setEstimatesVisible();
			} else {
				room.setEstimatesInvisible();
			}
			
			dataController.saveChanges();
			
			planningPokerContext.getOriRoomChangeNotifier().noteRoomChange(room.getId());
		}
	}
	
	private void deleteEstimatesAndUpdate(final String roomId, final IPlanningPokerContext planningPokerContext) {
		try (final var dataController = planningPokerContext.createDataController()) {
			
			final var room = dataController.getOriRoomById(roomId);
			room.getOriRoomVisits().forEach(IRoomVisit::deleteEstimate);
			room.setEstimatesInvisible();
			dataController.saveChanges();
			
			planningPokerContext.getOriRoomChangeNotifier().noteRoomChange(room.getId());
		}
	}
	
	private String getEstimateTextWhenEstimateIsInvisible(final IRoomVisit roomVisit) {
		
		if (ROOM_VISIT_EVALUATOR.hasEstimate(roomVisit)) {
			return StringCatalogue.QUESTION_MARK;
		}
		
		return StringCatalogue.THIN_CROSS;
	}
	
	private String getEstimateTextWhenEstimateIsVisible(final IRoomVisit roomVisit) {
		
		if (roomVisit.hasEstimateInStorypoints()) {
			return getEstimateTextWhenEstimateIsVisibleAndHasEstimateInStoryPoints(roomVisit);
		}
		
		if (roomVisit.hasInfiniteEstimate()) {
			return StringCatalogue.INFINITY;
		}
		
		return StringCatalogue.THIN_CROSS;
	}
	
	private String getEstimateTextWhenEstimateIsVisibleAndHasEstimateInStoryPoints(
		final IRoomVisit roomVisit
	) {
		final var estimateInStoryPoints = roomVisit.getEstimateInStoryPoints();
		
		if (estimateInStoryPoints == 0.5) {
			return "0.5";
		}
		
		return String.format(Locale.ENGLISH, "%.0f", estimateInStoryPoints);
	}
	
	private void goToOtherRoomAndUpdate(
		final String userId,
		final WebClientSession<IPlanningPokerContext> webClientSession,
		final ISelectRoomSessionFactory selectRoomSessionFactory
	) {
		
		final var applicationContext = webClientSession.getOriApplicationContext();
		
		try (final var dataController = applicationContext.createDataController()) {
			
			final var user = dataController.getOriUserById(userId);
			final var room = user.getOriCurrentRoomVisit().getOriParentRoom();
			dataController.leaveRoom(user);
			
			dataController.saveChanges();
			
			webClientSession.setNext(selectRoomSessionFactory.createSelectRoomSessionWihtUserId(userId));
			
			applicationContext.getOriRoomChangeNotifier().noteRoomChange(room.getId());
		}
	}
	
	private void openDeleteEstimatesDialogWhenRoomContainsEstimates(
		final String roomId,
		final WebClientSession<IPlanningPokerContext> webClientSession
	) {
		
		final var deleteEstimateDialog =
		YES_NO_DIALOG_FACTORY.createYesNoDialogWithYesNoQuestionAndConfirmAction(
			"Do you want to delete all estimates?",
			() -> deleteEstimatesAndUpdate(roomId, webClientSession.getOriApplicationContext())
		);
		
		webClientSession.getOriGUI().pushLayer(
			deleteEstimateDialog
		);
	}
}
