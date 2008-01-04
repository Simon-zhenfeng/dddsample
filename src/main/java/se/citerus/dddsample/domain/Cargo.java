package se.citerus.dddsample.domain;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;


/**
 * A Cargo an entity identifed by TrackingId and is capable of getting its DeliveryHistory plus a number
 * of convenience operation for finding current destination etc.
 */
public class Cargo {
  private final TrackingId trackingId;
  private final Location origin;
  private final Location finalDestination;
  private DeliveryHistory history;

  public Cargo(TrackingId trackingId, Location origin, Location finalDestination) {
    this.trackingId = trackingId;
    this.origin = origin;
    this.finalDestination = finalDestination;

    this.history = new DeliveryHistory();
  }

  public DeliveryHistory getDeliveryHistory() {
    return history;
  }

  public void handle(HandlingEvent event) {
    history.addEvent(event);
  }

  public boolean atFinalDestiation() {
    return getCurrentLocation().equals(finalDestination);
  }

  public Location getCurrentLocation() {
    HandlingEvent lastEvent = history.last();
    
    // If we have no last event, we have not even received the package. Return unknown location
    if (lastEvent == null) {
      return Location.NULL;
    }
   
    Location location = lastEvent.getLocation();
    
    // If the last handling event has no idea of where the cargo is due to lack of CarrierMovement (like for CLAIM or RECEIVE events)
    // location must be calculated based on event type and origin or final destination
    // TODO: Maybe we need to refactor HandlingEvent.
    if (location == Location.NULL){
      location = (lastEvent.getType() == HandlingEvent.Type.CLAIM) ? finalDestination : origin;
    }
      
    return location;
  }

  public TrackingId trackingId() {
    return trackingId;
  }

  @Override
  public String toString() {
    return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
  }
}
