package ch.nolix.planningpoker.frontend.main;

import ch.nolix.core.errorcontrol.validator.Validator;
import ch.nolix.coreapi.programcontrolapi.triggerapi.ITriggerableSubscriber;

public final class RoomSubscriberWrapper implements ITriggerableSubscriber {

  private final String roomId;

  private final ITriggerableSubscriber roomSubscriber;

  private RoomSubscriberWrapper(final String roomId, final ITriggerableSubscriber roomSubscriber) {

    Validator.assertThat(roomId).thatIsNamed("room id").isNotBlank();
    Validator.assertThat(roomSubscriber).thatIsNamed("roomSubscriber").isNotNull();

    this.roomId = roomId;
    this.roomSubscriber = roomSubscriber;
  }

  public static RoomSubscriberWrapper forRoomAndSubscriber(
    final String roomId,
    final ITriggerableSubscriber roomSubscriber) {
    return new RoomSubscriberWrapper(roomId, roomSubscriber);
  }

  public boolean containsRoomSubscriber(final ITriggerableSubscriber roomSubscriber) {
    return (this.roomSubscriber == roomSubscriber);
  }

  public String getRoomId() {
    return roomId;
  }

  @Override
  public boolean isAlive() {
    return roomSubscriber.isAlive();
  }

  @Override
  public void trigger() {
    roomSubscriber.trigger();
  }
}
