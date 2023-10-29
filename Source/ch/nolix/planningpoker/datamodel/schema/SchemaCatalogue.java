package ch.nolix.planningpoker.datamodel.schema;

import ch.nolix.system.objectdatabase.schema.Schema;
import ch.nolix.systemapi.objectdatabaseapi.schemaapi.ISchema;

public final class SchemaCatalogue {

  public static final ISchema SCHEMA = Schema.withEntityType(User.class, Room.class, RoomVisit.class);

  private SchemaCatalogue() {
  }
}
