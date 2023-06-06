package ch.nolix.planningpokerapi.datamodelapi;

public interface IEstimate {
	
	IUser getOriParentUser();
	
	double getStoryPoints();
}
