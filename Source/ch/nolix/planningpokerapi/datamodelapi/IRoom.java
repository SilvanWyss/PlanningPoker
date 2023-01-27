package ch.nolix.planningpokerapi.datamodelapi;

import ch.nolix.coreapi.attributeapi.mandatoryattributeuniversalapi.IdentifiedByString;
import ch.nolix.coreapi.containerapi.mainapi.IContainer;

public interface IRoom extends IdentifiedByString {
	
	void addVisitor(IUser visitor);
	
	String getIdentification();
	
	IContainer<? extends IEstimationRound> getRefEstimationRounds();
	
	IUser getRefParentCreator();
	
	IContainer<? extends IRoomVisit> getRefVisits();
	
	boolean hasSetEstimationsInvisible();
	
	boolean hasSetEstimationsVisible();
	
	void setEstimationsInvisible();
	
	void setEstimationsVisible();
}
