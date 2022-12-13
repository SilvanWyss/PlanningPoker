package ch.nolix.planningpokerapi.datamodelapi;

public interface IEstimation {
	
	IUser getRefParentUser();
	
	double getStoryPoints();
}
