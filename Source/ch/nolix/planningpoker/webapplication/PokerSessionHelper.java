package ch.nolix.planningpoker.webapplication;

import ch.nolix.core.commontype.commontypeconstant.StringCatalogue;
import ch.nolix.planningpokerapi.applicationcontextapi.IApplicationContext;
import ch.nolix.planningpokerapi.datamodelapi.IRoomVisit;
import ch.nolix.system.webgui.dialog.YesNoDialogFactory;

public final class PokerSessionHelper {
	
	private static final YesNoDialogFactory YES_NO_DIALOG_FACTORY = YesNoDialogFactory.INSTANCE;
	
	public void deleteEstimationAndUpdate(final String roomVisitId, final IApplicationContext applicationContext) {
		try (final var dataController = applicationContext.createDataController()) {
			
			final var roomVisit = dataController.getOriRoomVisitById(roomVisitId);
			roomVisit.deleteEstimation();
			dataController.saveChanges();
			
			final var room = roomVisit.getOriParentRoom();	
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
	
	public String getEstimationText(final IRoomVisit roomVisit) {
		
		if (roomVisit.getOriParentRoom().hasSetEstimationsVisible()) {
			return getEstimationTextWhenEstimationIsVisible(roomVisit);
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
	
	public void setEstimationInStoryPointsAndUpdate(
		final String roomVisitId,
		final int estimationInStoryPoints,
		final IApplicationContext applicationContext
	) {
		try (final var dataController = applicationContext.createDataController()) {
			
			final var roomVisit = dataController.getOriRoomVisitById(roomVisitId);
			roomVisit.setEstimationInStoryPoints(estimationInStoryPoints);
			dataController.saveChanges();
			
			final var room = roomVisit.getOriParentRoom();	
			applicationContext.getOriRoomChangeNotifier().noteRoomChange(room.getId());
		}
	}
	
	public void setInfiniteEstimationAndUpdate(
		final String roomVisitId,
		final IApplicationContext applicationContext
	) {
		try (final var dataController = applicationContext.createDataController()) {
			
			final var roomVisit = dataController.getOriRoomVisitById(roomVisitId);
			roomVisit.setInfiniteEstimation();
			dataController.saveChanges();
			
			final var room = roomVisit.getOriParentRoom();	
			applicationContext.getOriRoomChangeNotifier().noteRoomChange(room.getId());
		}
	}
	
	public void toggleEstimationVisibilityAndUpdate(final String roomId, final IApplicationContext applicationContext) {
		try (final var dataController = applicationContext.createDataController()) {
			
			final var room = dataController.getOriRoomById(roomId);
			
			if (room.hasSetEstimationsInvisible()) {
				room.setEstimationsVisible();
			} else {
				room.setEstimationsInvisible();
			}
			
			dataController.saveChanges();
			
			applicationContext.getOriRoomChangeNotifier().noteRoomChange(room.getId());
		}
	}
	
	private String getEstimationTextWhenEstimationIsVisible(final IRoomVisit roomVisit) {
		
		if (roomVisit.hasEstimationInStorypoints()) {
			return String.valueOf(roomVisit.getEstimationInStoryPoints());
		}
		
		if (roomVisit.hasInfiniteEstimation()) {
			return StringCatalogue.INFINITY;
		}
		
		return StringCatalogue.THIN_CROSS;
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
