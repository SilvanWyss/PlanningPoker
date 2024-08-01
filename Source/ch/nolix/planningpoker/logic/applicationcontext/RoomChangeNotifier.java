package ch.nolix.planningpoker.logic.applicationcontext;

import ch.nolix.core.container.linkedlist.LinkedList;
import ch.nolix.coreapi.containerapi.baseapi.IContainer;
import ch.nolix.coreapi.programcontrolapi.triggerapi.ITriggerableSubscriber;
import ch.nolix.planningpokerapi.logicapi.applicationcontextapi.IRoomChangeNotifier;

public final class RoomChangeNotifier implements IRoomChangeNotifier {

  private final LinkedList<RoomSubscriberWrapper> roomSubscriberWrappers = LinkedList.createEmpty();

  @Override
  public void noteRoomChange(final String roomId) {

    removeOutdatedRoomSubscribers();

    triggerRoomSubscribersOfRoom(roomId);
  }

  @Override
  public void registerRoomSubscriberIfNotRegistered(
    final String roomId,
    final ITriggerableSubscriber roomSubscriber) {
    if (!hasRegisteredRoomSubscriber(roomSubscriber)) {
      registerRoomSubscriber(roomId, roomSubscriber);
    }
  }

  private IContainer<RoomSubscriberWrapper> getStoredRoomSubscribers() {
    return roomSubscriberWrappers;
  }

  private boolean hasRegisteredRoomSubscriber(final ITriggerableSubscriber roomSubscriber) {
    return roomSubscriberWrappers.containsAny(rsw -> rsw.containsRoomSubscriber(roomSubscriber));
  }

  private void registerRoomSubscriber(final String roomId, final ITriggerableSubscriber roomSubscriber) {
    registerRoomSubscriber(RoomSubscriberWrapper.forRoomAndSubscriber(roomId, roomSubscriber));
  }

  private void registerRoomSubscriber(final RoomSubscriberWrapper roomSubscriber) {
    roomSubscriberWrappers.addAtEnd(roomSubscriber);
  }

  private void removeOutdatedRoomSubscribers() {
    roomSubscriberWrappers.removeAll(ITriggerableSubscriber::isOutdated);
  }

  private void triggerRoomSubscribersOfRoom(final String roomId) {
    for (final var rs : getStoredRoomSubscribers()) {
      if (rs.getRoomId().equals(roomId)) {
        rs.trigger();
      }
    }
  }
}
