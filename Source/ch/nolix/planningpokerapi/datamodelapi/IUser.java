package ch.nolix.planningpokerapi.datamodelapi;

public interface IUser {
	
	IRoom createAndEnterNewRoom();
	
	void enterRoom(IRoom romm);
	
	void enterRoomByIdentification(String identification);
	
	String getName();
	
	IRoomVisit getRefRoomVisit();
	
	boolean isInRoom();
	
	void setName(String name);
}
