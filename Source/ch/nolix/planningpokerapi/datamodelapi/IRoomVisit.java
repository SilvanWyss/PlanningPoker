package ch.nolix.planningpokerapi.datamodelapi;

import ch.nolix.coreapi.attributeapi.mandatoryattributeuniversalapi.Identified;

public interface IRoomVisit extends Identified {
	
	void deleteEstimation();
	
	double getEstimationInStoryPoints();
	
	IRoom getOriParentRoom();
	
	IUser getOriVisitor();
	
	boolean hasEstimationInStorypoints();
	
	boolean hasInfiniteEstimation();
	
	void setEstimationInStoryPoints(double estimationInStoryPoints);
	
	void setInfiniteEstimation();
}
