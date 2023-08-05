package ch.nolix.planningpokerapi.datamodelapi.schemaapi;

import ch.nolix.coreapi.attributeapi.mandatoryattributeapi.Identified;
import ch.nolix.coreapi.containerapi.baseapi.IContainer;

public interface IRoom extends Identified {
	
	void addRoomVisit(IRoomVisit roomVisit);
	
	String getNumber();
	
	IUser getOriParentCreator();
	
	IContainer<? extends IRoomVisit> getOriRoomVisits();
	
	boolean hasSetEstimatesInvisible();
	
	boolean hasSetEstimatesVisible();
	
	void removeRoomVisit(IRoomVisit roomVisit);
	
	void setEstimatesInvisible();
	
	void setEstimatesVisible();
}
