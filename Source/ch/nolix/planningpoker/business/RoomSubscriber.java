package ch.nolix.planningpoker.business;

import ch.nolix.core.errorcontrol.validator.GlobalValidator;
import ch.nolix.coreapi.programcontrolapi.triggeruniversalapi.CloseStateRequestableTriggerable;

public final class RoomSubscriber implements CloseStateRequestableTriggerable {
	
	public static RoomSubscriber forRoomAndSubscriber(
		final String roomIdentification,
		final CloseStateRequestableTriggerable subscriber
	) {
		return new RoomSubscriber(roomIdentification, subscriber);
	}
	
	private final String roomIdentification;
	
	private final CloseStateRequestableTriggerable subscriber;
	
	private RoomSubscriber(final String roomIdentification, final CloseStateRequestableTriggerable subscriber) {
		
		GlobalValidator.assertThat(roomIdentification).thatIsNamed("room identification").isNotBlank();
		GlobalValidator.assertThat(subscriber).thatIsNamed("subscriber").isNotNull();
		
		this.roomIdentification = roomIdentification;
		this.subscriber = subscriber;
	}
	
	@Override
	public boolean isClosed() {
		return subscriber.isClosed();
	}
	
	public String getRoomIdentification() {
		return roomIdentification;
	}
	
	@Override
	public void trigger() {
		subscriber.trigger();
	}
}
