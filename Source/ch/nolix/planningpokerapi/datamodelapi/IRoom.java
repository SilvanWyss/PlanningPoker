package ch.nolix.planningpokerapi.datamodelapi;

import ch.nolix.coreapi.attributeapi.mandatoryattributeuniversalapi.Identified;
import ch.nolix.coreapi.containerapi.baseapi.IContainer;

public interface IRoom extends Identified {
	
	void addVisitor(IUser visitor);
	
	String getIdentification();
	
	IContainer<? extends IEstimationRound> getOriEstimationRounds();
	
	IUser getOriParentCreator();
	
	IContainer<? extends IRoomVisit> getOriVisits();
	
	boolean hasSetEstimationsInvisible();
	
	boolean hasSetEstimationsVisible();
	
	void setEstimationsInvisible();
	
	void setEstimationsVisible();
}
