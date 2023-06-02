package ch.nolix.planningpokerapi.datamodelapi;

import ch.nolix.coreapi.attributeapi.mandatoryattributeuniversalapi.Identified;
import ch.nolix.coreapi.containerapi.baseapi.IContainer;

public interface IRoom extends Identified {
	
	void addVisitor(IUser visitor);
	
	String getNumber();
	
	IContainer<? extends IEstimationRound> getOriEstimationRounds();
	
	IUser getOriParentCreator();
	
	IContainer<? extends IRoomVisit> getOriRoomVisits();
	
	boolean hasSetEstimationsInvisible();
	
	boolean hasSetEstimationsVisible();
	
	void removeVisitor(IUser visitor);
	
	void setEstimationsInvisible();
	
	void setEstimationsVisible();
}
