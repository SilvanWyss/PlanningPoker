package ch.nolix.planningpoker.business;

import ch.nolix.core.errorcontrol.validator.GlobalValidator;
import ch.nolix.planningpokerapi.businessapi.ISubscriber;

public final class RoomSubscriber implements ISubscriber {
	
	public static RoomSubscriber forRoomAndSubscriber(final String roomIdentification, final ISubscriber subscriber) {
		return new RoomSubscriber(roomIdentification, subscriber);
	}
	
	private final String roomIdentification;
	
	private final ISubscriber subscriber;
	
	private RoomSubscriber(final String roomIdentification, final ISubscriber subscriber) {
		
		GlobalValidator.assertThat(roomIdentification).thatIsNamed("room identification").isNotBlank();
		GlobalValidator.assertThat(subscriber).thatIsNamed(ISubscriber.class).isNotNull();
		
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
	public void update() {
		subscriber.update();
	}
}
