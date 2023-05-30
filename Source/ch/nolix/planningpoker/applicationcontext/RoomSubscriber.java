package ch.nolix.planningpoker.applicationcontext;

import ch.nolix.core.errorcontrol.validator.GlobalValidator;
import ch.nolix.coreapi.programcontrolapi.triggeruniversalapi.CloseStateRequestableTriggerable;

public final class RoomSubscriber implements CloseStateRequestableTriggerable {
	
	public static RoomSubscriber forRoomAndSubscriber(
		final String roomIdentification,
		final CloseStateRequestableTriggerable subscriber
	) {
		return new RoomSubscriber(roomIdentification, subscriber);
	}
	
	private final String roomId;
	
	private final CloseStateRequestableTriggerable subscriber;
	
	private RoomSubscriber(final String roomNumber, final CloseStateRequestableTriggerable subscriber) {
		
		GlobalValidator.assertThat(roomNumber).thatIsNamed("room number").isNotBlank();
		GlobalValidator.assertThat(subscriber).thatIsNamed("subscriber").isNotNull();
		
		this.roomId = roomNumber;
		this.subscriber = subscriber;
	}
	
	@Override
	public boolean isClosed() {
		return subscriber.isClosed();
	}
	
	public String getRoomId() {
		return roomId;
	}
	
	@Override
	public void trigger() {
		subscriber.trigger();
	}
}
