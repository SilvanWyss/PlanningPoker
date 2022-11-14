package ch.nolix.planningpoker.datamodelapi;

public interface IEstimation {
	
	IUser getRefParentUser();
	
	double getStoryPoints();
}
