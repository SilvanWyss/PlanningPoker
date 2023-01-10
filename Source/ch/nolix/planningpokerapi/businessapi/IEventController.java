package ch.nolix.planningpokerapi.businessapi;

public interface IEventController {
	
	void noteRoomChange(String roomIdentification);
	
	void registerSubscriberForRoomChange(String roomIdentification, ISubscriber subscriber);
}
