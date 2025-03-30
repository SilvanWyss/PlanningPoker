package ch.nolix.planningpoker.backend.datamodel;

import ch.nolix.system.objectdata.model.EntityTypeSet;
import ch.nolix.systemapi.objectdataapi.modelapi.IEntityTypeSet;

public final class EntityTypeSetCatalog {

  public static final IEntityTypeSet ENTITY_TYPE_SET = //
  EntityTypeSet.withEntityType(User.class, Room.class, RoomVisit.class);

  private EntityTypeSetCatalog() {
  }
}
