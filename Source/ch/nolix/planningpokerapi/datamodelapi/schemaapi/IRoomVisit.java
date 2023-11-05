package ch.nolix.planningpokerapi.datamodelapi.schemaapi;

import ch.nolix.coreapi.attributeapi.mandatoryattributeapi.IIdHolder;

public interface IRoomVisit extends IIdHolder {

  void deleteEstimate();

  double getEstimateInStoryPoints();

  IRoom getStoredParentRoom();

  IUser getStoredVisitor();

  boolean hasEstimateInStorypoints();

  boolean hasInfiniteEstimate();

  void setEstimateInStoryPoints(double estimateInStoryPoints);

  void setInfiniteEstimate();
}
