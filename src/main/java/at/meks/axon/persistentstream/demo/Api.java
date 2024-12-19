package at.meks.axon.persistentstream.demo;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public class Api {

    public record CreateBikeCommand(@TargetAggregateIdentifier String id) { }

    public record BikeCreatedEvent(String id) { }

    public record RentBikeCommand(@TargetAggregateIdentifier String id) { }

    public record BikeRentedEvent(String id) { }
}
