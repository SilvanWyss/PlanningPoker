package ch.nolix.planningpokerapi.frontendapi.mainapi;

import ch.nolix.coreapi.programcontrolapi.triggerapi.ITriggerableSubscriber;

public interface IRoomChangeNotifier {

  void noteRoomChange(String roomId);

  void registerRoomSubscriberIfNotRegistered(String roomId, ITriggerableSubscriber roomSubscriber);
}
