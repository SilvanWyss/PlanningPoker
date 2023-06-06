package ch.nolix.planningpokerapi.datamodelapi;

import ch.nolix.coreapi.attributeapi.mandatoryattributeuniversalapi.Identified;
import ch.nolix.coreapi.containerapi.baseapi.IContainer;

public interface IRoom extends Identified {
	
	void addRoomVisit(IRoomVisit roomVisit);
	
	String getNumber();
	
	IContainer<? extends IEstimateRound> getOriEstimateRounds();
	
	IUser getOriParentCreator();
	
	IContainer<? extends IRoomVisit> getOriRoomVisits();
	
	boolean hasSetEstimatesInvisible();
	
	boolean hasSetEstimatesVisible();
	
	void removeRoomVisit(IRoomVisit roomVisit);
	
	void setEstimatesInvisible();
	
	void setEstimatesVisible();
}
