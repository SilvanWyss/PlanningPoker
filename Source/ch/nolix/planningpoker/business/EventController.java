package ch.nolix.planningpoker.business;

import ch.nolix.core.container.main.LinkedList;
import ch.nolix.coreapi.containerapi.mainapi.IContainer;
import ch.nolix.planningpokerapi.businessapi.IEventController;
import ch.nolix.planningpokerapi.businessapi.ISubscriber;

public final class EventController implements IEventController {
	
	private boolean isUpdatingRoomSubscribers;
	
	private boolean updatingRoomSubscribersIsRequired;
	
	private final LinkedList<RoomSubscriber> roomSubscribers = new LinkedList<>();
	
	@Override
	public void noteRoomChange(final String roomIdentification) {
		if (!isUpdatingRoomSubscribers()) {
			
			isUpdatingRoomSubscribers = true;
			
			updateRoomSubscribers(roomIdentification);
			
			while (updatingRoomSubscribersIsRequired()) {
				
				updatingRoomSubscribersIsRequired = false;
				
				updateRoomSubscribers(roomIdentification);
			}
			
			isUpdatingRoomSubscribers = false;
		} else {
			updatingRoomSubscribersIsRequired = true;
		}
	}

	@Override
	public void registerSubscriberForRoomChange(final String roomIdentification, final ISubscriber subscriber) {
		registerRoomSubscriber(RoomSubscriber.forRoomAndSubscriber(roomIdentification, subscriber));
	}
	
	private IContainer<RoomSubscriber> getRefRoomSubscribers() {
		return roomSubscribers;
	}
	
	private boolean isUpdatingRoomSubscribers() {
		return isUpdatingRoomSubscribers;
	}
	
	private void registerRoomSubscriber(final RoomSubscriber roomSubscriber) {
		roomSubscribers.addAtEnd(roomSubscriber);
	}
	
	private void removeClosedRoomSubscribers() {
		roomSubscribers.removeAll(RoomSubscriber::isClosed);
	}
	
	private void updateRoomSubscribers(final String roomIdentification) {
		removeClosedRoomSubscribers();
		
		updateRoomSubscribersOfRoom(roomIdentification);
	}
	
	private boolean updatingRoomSubscribersIsRequired() {
		return updatingRoomSubscribersIsRequired;
	}

	private void updateRoomSubscribersOfRoom(final String roomIdentification) {
		for (final var rs : getRefRoomSubscribers()) {
			if (rs.getRoomIdentification().equals(roomIdentification)) {
				rs.update();
			}
		}
	}
}
