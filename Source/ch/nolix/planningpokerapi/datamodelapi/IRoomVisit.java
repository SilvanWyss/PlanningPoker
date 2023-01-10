package ch.nolix.planningpokerapi.datamodelapi;

public interface IRoomVisit {
	
	void deleteEstimation();
	
	double getEstimationInStoryPoints();
	
	IRoom getRefParentRoom();
	
	IUser getRefVisitor();
	
	boolean hasEstimationInStorypoints();
	
	boolean hasInfiniteEstimation();
	
	void setEstimationInStoryPoints(double estimationInStoryPoints);
	
	void setInfiniteEstimation();
}
