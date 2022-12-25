package ch.nolix.planningpokerapi.datamodelapi;

public interface IUser {
	
	String getName();
	
	IRoomVisit getRefCurrentRoomVisit();
	
	boolean isInRoom();
	
	void setName(String name);
}
