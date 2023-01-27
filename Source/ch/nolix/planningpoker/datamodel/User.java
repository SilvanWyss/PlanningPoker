package ch.nolix.planningpoker.datamodel;

import ch.nolix.core.errorcontrol.validator.GlobalValidator;
import ch.nolix.core.programatom.name.LowerCaseCatalogue;
import ch.nolix.planningpokerapi.datamodelapi.IRoomVisit;
import ch.nolix.planningpokerapi.datamodelapi.IUser;
import ch.nolix.system.objectdatabase.database.Entity;
import ch.nolix.system.objectdatabase.database.OptionalBackReference;
import ch.nolix.system.objectdatabase.database.Value;

public final class User extends Entity implements IUser {
	
	public static User withName(final String name) {
		
		final var user = new User();
		user.setName(name);
		
		return user;
	}
	
	private final Value<String> name = new Value<>();
	
	private final OptionalBackReference<RoomVisit> currentRoomVisit =
	OptionalBackReference.forEntityAndBackReferencedPropertyName(RoomVisit.class, "visitor");
	
	private User() {
		initialize();
	}
	
	@Override
	public String getName() {
		return name.getRefValue();
	}
	
	@Override
	public IRoomVisit getRefCurrentRoomVisit() {
		return currentRoomVisit.getBackReferencedEntity();
	}
	
	@Override
	public boolean isInARoom() {
		return currentRoomVisit.containsAny();
	}
	
	@Override
	public void setName(final String name) {
		
		GlobalValidator.assertThat(name).thatIsNamed(LowerCaseCatalogue.NAME).isNotBlank();
		
		this.name.setValue(name);
	}
}
