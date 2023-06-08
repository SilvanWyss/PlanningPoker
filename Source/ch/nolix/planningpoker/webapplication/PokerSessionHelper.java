package ch.nolix.planningpoker.webapplication;

import ch.nolix.core.commontype.commontypeconstant.StringCatalogue;
import ch.nolix.planningpokerapi.applicationcontextapi.IApplicationContext;
import ch.nolix.planningpokerapi.datamodelapi.IRoomVisit;
import ch.nolix.system.webgui.dialog.YesNoDialogFactory;

public final class PokerSessionHelper {
	
	private static final YesNoDialogFactory YES_NO_DIALOG_FACTORY = YesNoDialogFactory.INSTANCE;
	
	public void deleteEstimateAndUpdate(final String roomVisitId, final IApplicationContext applicationContext) {
		try (final var dataController = applicationContext.createDataController()) {
			
			final var roomVisit = dataController.getOriRoomVisitById(roomVisitId);
			roomVisit.deleteEstimate();
			dataController.saveChanges();
			
			final var room = roomVisit.getOriParentRoom();	
			applicationContext.getOriRoomChangeNotifier().noteRoomChange(room.getId());
		}
	}
	
	public void deleteEstimatesAndUpdate(final String roomId, final IApplicationContext applicationContext) {
		try (final var dataController = applicationContext.createDataController()) {
			
			final var room = dataController.getOriRoomById(roomId);
			room.setEstimatesInvisible();
			room.getOriRoomVisits().forEach(IRoomVisit::deleteEstimate);
			dataController.saveChanges();
			
			applicationContext.getOriRoomChangeNotifier().noteRoomChange(room.getId());
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
	
	public String getEstimateText(final IRoomVisit roomVisit) {
		
		if (roomVisit.getOriParentRoom().hasSetEstimatesVisible()) {
			return getEstimateTextWhenEstimateIsVisible(roomVisit);
		}
		
		return StringCatalogue.QUESTION_MARK;
	}
	
	public boolean isAllowedToConfigureRoom(final IRoomVisit roomVisit, final PokerSession session) {
		
		final var userId = session.getOriParentClient().getCookieValueByCookieNameOrNull("userId");
		
		return roomVisit.getOriParentRoom().getOriParentCreator().hasId(userId);
	}
	
	public void openGoToOtherRoomDialog(final String userId, final PokerSession session) {
		session.getOriGUI().pushLayer(
			YES_NO_DIALOG_FACTORY.createYesNoDialogWithYesNoQuestionAndConfirmAction(
				"Do you really want to leave the current room?",
				() -> goToOtherRoomAndUpdate(userId, session)
			)
		);
	}
	
	public void setEstimateInStoryPointsAndUpdate(
		final String roomVisitId,
		final int estimateInStoryPoints,
		final IApplicationContext applicationContext
	) {
		try (final var dataController = applicationContext.createDataController()) {
			
			final var roomVisit = dataController.getOriRoomVisitById(roomVisitId);
			roomVisit.setEstimateInStoryPoints(estimateInStoryPoints);
			dataController.saveChanges();
			
			final var room = roomVisit.getOriParentRoom();	
			applicationContext.getOriRoomChangeNotifier().noteRoomChange(room.getId());
		}
	}
	
	public void setInfiniteEstimateAndUpdate(
		final String roomVisitId,
		final IApplicationContext applicationContext
	) {
		try (final var dataController = applicationContext.createDataController()) {
			
			final var roomVisit = dataController.getOriRoomVisitById(roomVisitId);
			roomVisit.setInfiniteEstimate();
			dataController.saveChanges();
			
			final var room = roomVisit.getOriParentRoom();	
			applicationContext.getOriRoomChangeNotifier().noteRoomChange(room.getId());
		}
	}
	
	public void toggleEstimateVisibilityAndUpdate(final String roomId, final IApplicationContext applicationContext) {
		try (final var dataController = applicationContext.createDataController()) {
			
			final var room = dataController.getOriRoomById(roomId);
			
			if (room.hasSetEstimatesInvisible()) {
				room.setEstimatesVisible();
			} else {
				room.setEstimatesInvisible();
			}
			
			dataController.saveChanges();
			
			applicationContext.getOriRoomChangeNotifier().noteRoomChange(room.getId());
		}
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
		
		return String.valueOf((int)estimateInStoryPoints);
	}
	
	private void goToOtherRoomAndUpdate(final String userId, final PokerSession session) {
		
		final var applicationContext = session.getOriApplicationContext();
		
		try (final var dataController = applicationContext.createDataController()) {
			
			final var user = dataController.getOriUserById(userId);
			user.getOriCurrentRoomVisit().getOriParentRoom();
			dataController.leaveRoom(user);
			
			dataController.saveChanges();
		}
		
		session.setNext(new CreateRoomSession());
	}
}
