package ch.nolix.planningpoker.datamodelapi;

import ch.nolix.coreapi.containerapi.mainapi.IContainer;

public interface IRoom {
	
	String getIdentification();
	
	IContainer<IEstimationRound> getRefEstimationRounds();
	
	IUser getRefParentCreator();
	
	IContainer<IRoomVisit> getRefVisits();
	
	boolean hasSetEstimationsInvisible();
	
	boolean hasSetEstimationsVisible();
	
	void setEstimationsInvisible();
	
	void setEstimationsVisible();
}
