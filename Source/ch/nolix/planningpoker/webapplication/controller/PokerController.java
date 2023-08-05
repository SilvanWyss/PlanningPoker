package ch.nolix.planningpoker.webapplication.controller;

import java.util.Locale;

import ch.nolix.core.commontype.commontypeconstant.StringCatalogue;
import ch.nolix.planningpoker.datamodel.dataevaluator.RoomEvaluator;
import ch.nolix.planningpoker.datamodel.dataevaluator.RoomVisitEvaluator;
import ch.nolix.planningpokerapi.datamodelapi.dataevaluatorapi.IRoomEvaluator;
import ch.nolix.planningpokerapi.datamodelapi.dataevaluatorapi.IRoomVisitEvaluator;
import ch.nolix.planningpokerapi.datamodelapi.schemaapi.IRoom;
import ch.nolix.planningpokerapi.datamodelapi.schemaapi.IRoomVisit;
import ch.nolix.planningpokerapi.logicapi.applicationcontextapi.IPlanningPokerContext;
import ch.nolix.planningpokerapi.webapplicationapi.sessionfactoryapi.ISelectRoomSessionFactory;
import ch.nolix.system.application.webapplication.WebClientSession;
import ch.nolix.template.webgui.dialog.ShowValueDialogBuilder;
import ch.nolix.template.webgui.dialog.YesNoDialogBuilder;

public final class PokerController {
	
	private static final IRoomEvaluator ROOM_EVALUATOR = new RoomEvaluator();
	
	private static final RoomHyperlinkCreator ROOM_HYPERLINK_CREATOR = new RoomHyperlinkCreator();
	
	private static final IRoomVisitEvaluator ROOM_VISIT_EVALUATOR = new RoomVisitEvaluator();
	
	public void deleteEstimateAndUpdate(final String roomVisitId, final IPlanningPokerContext planningPokerContext) {
		try (final var databaseAdapter = planningPokerContext.createDatabaseAdapter()) {
			
			final var roomVisit = databaseAdapter.getStoredRoomVisitById(roomVisitId);
			roomVisit.deleteEstimate();
			databaseAdapter.saveChanges();
			
			final var room = roomVisit.getStoredParentRoom();	
			planningPokerContext.getStoredRoomChangeNotifier().noteRoomChange(room.getId());
		}
	}
	
	public String getCaptainInfoText(final IRoomVisit roomVisit) {
		
		final var roomCreator = roomVisit.getStoredParentRoom().getStoredParentCreator();
		final var user = roomVisit.getStoredVisitor();
		
		if (roomCreator.hasId(user.getId())) {
			return "You are the captain.";
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
		
		if (roomVisit.getStoredParentRoom().hasSetEstimatesVisible()) {
			return getEstimateTextWhenEstimateIsVisible(roomVisit);
		}
		
		return getEstimateTextWhenEstimateIsInvisible(roomVisit);
	}
	
	public boolean isAllowedToConfigureRoom(final IRoomVisit roomVisit) {
		
		final var visitor = roomVisit.getStoredVisitor();
		final var room = roomVisit.getStoredParentRoom();
		final var roomCreator = room.getStoredParentCreator();
		
		return visitor.hasId(roomCreator.getId());
	}
	
	public void openDeleteEstimatesDialog(
		final String roomId,
		final WebClientSession<IPlanningPokerContext> webClientSession
	) {
		
		final var applicationContext = webClientSession.getStoredApplicationContext();
		
		try (final var databaseAdapter = applicationContext.createDatabaseAdapter()) {
			
			final var room = databaseAdapter.getStoredRoomById(roomId);
			
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
		new YesNoDialogBuilder()
		.setYesNoQuestion("Do you really want to leave the current room?")
		.setConfirmAction(() -> goToOtherRoomAndUpdate(userId, webClientSession, selectRoomSessionFactory))
		.build();
		
		webClientSession.getStoredGui().pushLayer(goToOtherRoomDialog);
	}
	
	public void openShareRoomDialog(final IRoom room, final WebClientSession<IPlanningPokerContext> webClientSession) {
		
		final var application = webClientSession.getStoredParentClient().getStoredParentApplication();
		
		final var roomHyperlink = ROOM_HYPERLINK_CREATOR.createHyperlinkToRoom(room, application);
		
		final var shareRoomDialog =
		new ShowValueDialogBuilder()
		.setValueName("Link to room")
		.setValue(roomHyperlink)
		.setValueCopier(v -> webClientSession.getStoredGui().onFrontEnd().writeTextToClipboard(v))
		.build();
				
		webClientSession.getStoredGui().pushLayer(shareRoomDialog);
	}
	
	public void setEstimateInStoryPointsAndUpdate(
		final String roomVisitId,
		final double estimateInStoryPoints,
		final IPlanningPokerContext planningPokerContext
	) {
		try (final var databaseAdapter = planningPokerContext.createDatabaseAdapter()) {
			
			final var roomVisit = databaseAdapter.getStoredRoomVisitById(roomVisitId);
			roomVisit.setEstimateInStoryPoints(estimateInStoryPoints);
			databaseAdapter.saveChanges();
			
			final var room = roomVisit.getStoredParentRoom();	
			planningPokerContext.getStoredRoomChangeNotifier().noteRoomChange(room.getId());
		}
	}
	
	public void setInfiniteEstimateAndUpdate(
		final String roomVisitId,
		final IPlanningPokerContext planningPokerContext
	) {
		try (final var databaseAdapter = planningPokerContext.createDatabaseAdapter()) {
			
			final var roomVisit = databaseAdapter.getStoredRoomVisitById(roomVisitId);
			roomVisit.setInfiniteEstimate();
			databaseAdapter.saveChanges();
			
			final var room = roomVisit.getStoredParentRoom();	
			planningPokerContext.getStoredRoomChangeNotifier().noteRoomChange(room.getId());
		}
	}
	
	public void toggleEstimateVisibilityAndUpdate(final String roomId, final IPlanningPokerContext planningPokerContext) {
		try (final var databaseAdapter = planningPokerContext.createDatabaseAdapter()) {
			
			final var room = databaseAdapter.getStoredRoomById(roomId);
			
			if (room.hasSetEstimatesInvisible()) {
				room.setEstimatesVisible();
			} else {
				room.setEstimatesInvisible();
			}
			
			databaseAdapter.saveChanges();
			
			planningPokerContext.getStoredRoomChangeNotifier().noteRoomChange(room.getId());
		}
	}
	
	private void deleteEstimatesAndUpdate(final String roomId, final IPlanningPokerContext planningPokerContext) {
		try (final var databaseAdapter = planningPokerContext.createDatabaseAdapter()) {
			
			final var room = databaseAdapter.getStoredRoomById(roomId);
			room.getStoredRoomVisits().forEach(IRoomVisit::deleteEstimate);
			room.setEstimatesInvisible();
			databaseAdapter.saveChanges();
			
			planningPokerContext.getStoredRoomChangeNotifier().noteRoomChange(room.getId());
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
		
		final var applicationContext = webClientSession.getStoredApplicationContext();
		
		try (final var databaseAdapter = applicationContext.createDatabaseAdapter()) {
			
			final var user = databaseAdapter.getStoredUserById(userId);
			final var room = user.getStoredCurrentRoomVisit().getStoredParentRoom();
			databaseAdapter.leaveRoom(user);
			
			databaseAdapter.saveChanges();
			
			webClientSession.setNext(selectRoomSessionFactory.createSelectRoomSessionWihtUserId(userId));
			
			applicationContext.getStoredRoomChangeNotifier().noteRoomChange(room.getId());
		}
	}
	
	private void openDeleteEstimatesDialogWhenRoomContainsEstimates(
		final String roomId,
		final WebClientSession<IPlanningPokerContext> webClientSession
	) {
		
		final var deleteEstimateDialog =
		new YesNoDialogBuilder()
		.setYesNoQuestion("Do you want to delete all estimates?")
		.setConfirmAction(() -> deleteEstimatesAndUpdate(roomId, webClientSession.getStoredApplicationContext()))
		.build();
		
		webClientSession.getStoredGui().pushLayer(
			deleteEstimateDialog
		);
	}
}
