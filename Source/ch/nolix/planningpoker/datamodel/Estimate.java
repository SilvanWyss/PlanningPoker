package ch.nolix.planningpoker.datamodel;

import ch.nolix.core.errorcontrol.invalidargumentexception.ArgumentHasAttributeException;
import ch.nolix.core.errorcontrol.validator.GlobalValidator;
import ch.nolix.planningpokerapi.datamodelapi.IEstimate;
import ch.nolix.planningpokerapi.datamodelapi.IUser;
import ch.nolix.system.objectdatabase.database.Entity;
import ch.nolix.system.objectdatabase.database.Reference;
import ch.nolix.system.objectdatabase.database.Value;

public final class Estimate extends Entity implements IEstimate {
	
	public static Estimate forParentUserAndStoryPoints(final User parentUser, final int storyPoints) {
		
		final var estimate = new Estimate();
		estimate.setParentUser(parentUser);
		estimate.setStoryPoints(storyPoints);
		
		return estimate;
	}
	
	private final Reference<User> parentUser = Reference.forEntity(User.class);
	
	private final Value<Double> storyPoints = new Value<>();
	
	private Estimate() {
		initialize();
	}
	
	@Override
	public IUser getOriParentUser() {
		return parentUser.getReferencedEntity();
	}
	
	@Override
	public double getStoryPoints() {
		return storyPoints.getOriValue();
	}
	
	private void assertDoesNotHaveParentUser() {
		if (hasParentUser()) {
			throw ArgumentHasAttributeException.forArgumentAndAttributeName(this, "parent user");
		}
	}
	
	private void assertStoryPointsAreNotSet() {
		if (storyPointsAreSet()) {
			throw ArgumentHasAttributeException.forArgumentAndAttributeName(this, "story points");
		}
	}
	
	private boolean hasParentUser() {
		return parentUser.containsAny();
	}
	
	private void setParentUser(final User parentUser) {
		
		assertDoesNotHaveParentUser();
		
		this.parentUser.setEntity(parentUser);
	}
	
	private void setStoryPoints(final double storyPoints) {
		
		GlobalValidator.assertThat(storyPoints).thatIsNamed("story points").isNotNegative();
		assertStoryPointsAreNotSet();
		
		this.storyPoints.setValue(storyPoints);
	}
	
	private boolean storyPointsAreSet() {
		return storyPoints.containsAny();
	}
}
