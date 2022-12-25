package ch.nolix.planningpokerapi.datamodelapi;

public interface IRoomVisit {
	
	void deleteEstimation();
	
	double getEstimationInStoryPoints();
	
	IRoom getRefParentRoom();
	
	IUser getRefVisitor();
	
	boolean hasEstimation();
	
	void setEstimationInStoryPoints(double estimationInStoryPoints);
}
