package ch.nolix.planningpoker.applicationcontext;

import ch.nolix.core.container.linkedlist.LinkedList;
import ch.nolix.coreapi.containerapi.baseapi.IContainer;
import ch.nolix.coreapi.programcontrolapi.triggeruniversalapi.CloseStateRequestableTriggerable;
import ch.nolix.planningpokerapi.applicationcontextapi.IRoomChangeNotifier;

public final class RoomChangeNotifier implements IRoomChangeNotifier {
	
	private boolean isTriggeringRoomSubscribers;
	
	private boolean triggeringRoomSubscribersIsRequired;
	
	private final LinkedList<RoomSubscriber> roomSubscribers = new LinkedList<>();
	
	@Override
	public void noteRoomChange(final String roomId) {
		if (!isTriggeringRoomSubscribers()) {
			
			isTriggeringRoomSubscribers = true;
			
			triggerRoomSubscribers(roomId);
			
			while (triggeringRoomSubscribersIsRequired()) {
				
				triggeringRoomSubscribersIsRequired = false;
				
				triggerRoomSubscribers(roomId);
			}
			
			isTriggeringRoomSubscribers = false;
		} else {
			triggeringRoomSubscribersIsRequired = true;
		}
	}

	@Override
	public void registerSubscriberForRoomChange(
		final String roomId,
		final CloseStateRequestableTriggerable subscriber
	) {
		registerRoomSubscriber(RoomSubscriber.forRoomAndSubscriber(roomId, subscriber));
	}
	
	private IContainer<RoomSubscriber> getOriRoomSubscribers() {
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
	
	private void triggerRoomSubscribers(final String roomId) {
		removeClosedRoomSubscribers();
		
		triggerRoomSubscribersOfRoom(roomId);
	}
	
	private boolean triggeringRoomSubscribersIsRequired() {
		return triggeringRoomSubscribersIsRequired;
	}
	
	private void triggerRoomSubscribersOfRoom(final String roomId) {
		for (final var rs : getOriRoomSubscribers()) {
			if (rs.getRoomId().equals(roomId)) {
				rs.trigger();
			}
		}
	}
}