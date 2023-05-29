package ch.nolix.planningpokerapi.datamodelapi;

public interface IRoomVisit {
	
	void deleteEstimation();
	
	double getEstimationInStoryPoints();
	
	IRoom getOriParentRoom();
	
	IUser getOriVisitor();
	
	boolean hasEstimationInStorypoints();
	
	boolean hasInfiniteEstimation();
	
	void setEstimationInStoryPoints(double estimationInStoryPoints);
	
	void setInfiniteEstimation();
}
