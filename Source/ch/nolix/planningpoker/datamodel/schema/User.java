package ch.nolix.planningpoker.datamodel.schema;

import ch.nolix.core.errorcontrol.validator.GlobalValidator;
import ch.nolix.coreapi.programatomapi.variablenameapi.LowerCaseCatalogue;
import ch.nolix.planningpokerapi.datamodelapi.schemaapi.IRoomVisit;
import ch.nolix.planningpokerapi.datamodelapi.schemaapi.IUser;
import ch.nolix.system.objectdatabase.database.Entity;
import ch.nolix.system.objectdatabase.database.OptionalBackReference;
import ch.nolix.system.objectdatabase.database.Value;

public final class User extends Entity implements IUser {

  public static final int MAX_NAME_LENGTH = 20;

  private final Value<String> name = new Value<>();

  private final OptionalBackReference<RoomVisit> currentRoomVisit = OptionalBackReference
    .forEntityAndBackReferencedPropertyName(RoomVisit.class, "visitor");

  private User() {
    initialize();
  }

  public static User withName(final String name) {
  
    final var user = new User();
    user.setName(name);
  
    return user;
  }

  @Override
  public String getName() {
    return name.getStoredValue();
  }

  @Override
  public IRoomVisit getStoredCurrentRoomVisit() {
    return currentRoomVisit.getBackReferencedEntity();
  }

  @Override
  public boolean isInARoom() {
    return currentRoomVisit.containsAny();
  }

  @Override
  public void setName(final String name) {

    GlobalValidator.assertThat(name).thatIsNamed(LowerCaseCatalogue.NAME).isNotBlank();
    GlobalValidator.assertThat(name).thatIsNamed(LowerCaseCatalogue.NAME).isNotLongerThan(MAX_NAME_LENGTH);
    GlobalValidator.assertThat(name).thatIsNamed(LowerCaseCatalogue.NAME).matches("[a-zA-Z0-9[.]\s]*");

    this.name.setValue(name);
  }
}
