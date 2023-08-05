package ch.nolix.planningpokerapi.datamodelapi.schemaapi;

import ch.nolix.coreapi.attributeapi.mandatoryattributeapi.Identified;

public interface IRoomVisit extends Identified {
	
	void deleteEstimate();
	
	double getEstimateInStoryPoints();
	
	IRoom getOriParentRoom();
	
	IUser getOriVisitor();
	
	boolean hasEstimateInStorypoints();
	
	boolean hasInfiniteEstimate();
	
	void setEstimateInStoryPoints(double estimateInStoryPoints);
	
	void setInfiniteEstimate();
}
