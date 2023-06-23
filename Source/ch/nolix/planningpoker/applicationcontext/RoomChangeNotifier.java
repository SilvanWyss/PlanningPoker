package ch.nolix.planningpoker.applicationcontext;

import ch.nolix.core.container.linkedlist.LinkedList;
import ch.nolix.coreapi.containerapi.baseapi.IContainer;
import ch.nolix.planningpokerapi.logicapi.applicationcontextapi.IRoomChangeNotifier;
import ch.nolix.planningpokerapi.logicapi.applicationcontextapi.IRoomSubscriber;

public final class RoomChangeNotifier implements IRoomChangeNotifier {
	
	private final LinkedList<RoomSubscriberWrapper> roomSubscriberWrappers = new LinkedList<>();
	
	@Override
	public void noteRoomChange(final String roomId) {
		
		removeInactiveRoomSubscribers();
		
		triggerRoomSubscribersOfRoom(roomId);
	}
	
	@Override
	public void registerRoomSubscriberIfNotRegistered(final String roomId, final IRoomSubscriber roomSubscriber) {
		if (!hasRegisteredRoomSubscriber(roomSubscriber)) {
			registerRoomSubscriber(roomId, roomSubscriber);
		}
	}
	
	private IContainer<RoomSubscriberWrapper> getOriRoomSubscribers() {
		return roomSubscriberWrappers;
	}
	
	private boolean hasRegisteredRoomSubscriber(final IRoomSubscriber roomSubscriber) {
		return roomSubscriberWrappers.containsAny(rsw -> rsw.containsRoomSubscriber(roomSubscriber));
	}
	
	private void registerRoomSubscriber(final String roomId, final IRoomSubscriber roomSubscriber) {
		registerRoomSubscriber(RoomSubscriberWrapper.forRoomAndSubscriber(roomId, roomSubscriber));
	}
	
	private void registerRoomSubscriber(final RoomSubscriberWrapper roomSubscriber) {
		roomSubscriberWrappers.addAtEnd(roomSubscriber);
	}
	
	private void removeInactiveRoomSubscribers() {
		roomSubscriberWrappers.removeAll(rsw -> !rsw.isActive());
	}
	
	private void triggerRoomSubscribersOfRoom(final String roomId) {
		for (final var rs : getOriRoomSubscribers()) {
			if (rs.getRoomId().equals(roomId)) {
				rs.trigger();
			}
		}
	}
}
