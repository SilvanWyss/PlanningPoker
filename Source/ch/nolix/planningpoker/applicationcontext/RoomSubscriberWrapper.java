package ch.nolix.planningpoker.applicationcontext;

import ch.nolix.core.errorcontrol.validator.GlobalValidator;
import ch.nolix.planningpokerapi.logicapi.applicationcontextapi.IRoomSubscriber;

public final class RoomSubscriberWrapper implements IRoomSubscriber {
	
	public static RoomSubscriberWrapper forRoomAndSubscriber(
		final String roomId,
		final IRoomSubscriber roomSubscriber
	) {
		return new RoomSubscriberWrapper(roomId, roomSubscriber);
	}
	
	private final String roomId;
	
	private final IRoomSubscriber roomSubscriber;
	
	private RoomSubscriberWrapper(final String roomId, final IRoomSubscriber roomSubscriber) {
		
		GlobalValidator.assertThat(roomId).thatIsNamed("room id").isNotBlank();
		GlobalValidator.assertThat(roomSubscriber).thatIsNamed("roomSubscriber").isNotNull();
		
		this.roomId = roomId;
		this.roomSubscriber = roomSubscriber;
	}
	
	public boolean containsRoomSubscriber(final IRoomSubscriber roomSubscriber) {
		return (this.roomSubscriber == roomSubscriber);
	}
	
	public String getRoomId() {
		return roomId;
	}
	
	@Override
	public boolean isActive() {
		return roomSubscriber.isActive();
	}
	
	@Override
	public void trigger() {
		roomSubscriber.trigger();
	}
}
