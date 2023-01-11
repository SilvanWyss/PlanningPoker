package ch.nolix.planningpoker.business;

import ch.nolix.core.container.main.LinkedList;
import ch.nolix.coreapi.containerapi.mainapi.IContainer;
import ch.nolix.coreapi.programcontrolapi.triggeruniversalapi.CloseStateRequestableTriggerable;
import ch.nolix.planningpokerapi.businessapi.IEventController;

public final class EventController implements IEventController {
	
	private boolean isTriggeringRoomSubscribers;
	
	private boolean triggeringRoomSubscribersIsRequired;
	
	private final LinkedList<RoomSubscriber> roomSubscribers = new LinkedList<>();
	
	@Override
	public void noteRoomChange(final String roomIdentification) {
		if (!isTriggeringRoomSubscribers()) {
			
			isTriggeringRoomSubscribers = true;
			
			triggerRoomSubscribers(roomIdentification);
			
			while (triggeringRoomSubscribersIsRequired()) {
				
				triggeringRoomSubscribersIsRequired = false;
				
				triggerRoomSubscribers(roomIdentification);
			}
			
			isTriggeringRoomSubscribers = false;
		} else {
			triggeringRoomSubscribersIsRequired = true;
		}
	}

	@Override
	public void registerSubscriberForRoomChange(
		final String roomIdentification,
		final CloseStateRequestableTriggerable subscriber
	) {
		registerRoomSubscriber(RoomSubscriber.forRoomAndSubscriber(roomIdentification, subscriber));
	}
	
	private IContainer<RoomSubscriber> getRefRoomSubscribers() {
		return roomSubscribers;
	}
	
	private boolean isTriggeringRoomSubscribers() {
		return isTriggeringRoomSubscribers;
	}
	
	private void registerRoomSubscriber(final RoomSubscriber roomSubscriber) {
		roomSubscribers.addAtEnd(roomSubscriber);
	}
	
	private void removeClosedRoomSubscribers() {
		roomSubscribers.removeAll(RoomSubscriber::isClosed);
	}
	
	private void triggerRoomSubscribers(final String roomIdentification) {
		removeClosedRoomSubscribers();
		
		triggerRoomSubscribersOfRoom(roomIdentification);
	}
	
	private boolean triggeringRoomSubscribersIsRequired() {
		return triggeringRoomSubscribersIsRequired;
	}
	
	private void triggerRoomSubscribersOfRoom(final String roomIdentification) {
		for (final var rs : getRefRoomSubscribers()) {
			if (rs.getRoomIdentification().equals(roomIdentification)) {
				rs.trigger();
			}
		}
	}
}
