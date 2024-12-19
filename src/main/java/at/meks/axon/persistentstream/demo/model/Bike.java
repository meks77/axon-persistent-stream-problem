package at.meks.axon.persistentstream.demo.model;

import at.meks.axon.persistentstream.demo.Api;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@SuppressWarnings("unused")
public class Bike {

    @AggregateIdentifier
    String id;

    Bike() {
        // necessary for Axon framework
    }

    @CommandHandler
    Bike(Api.CreateBikeCommand command) {
        apply(new Api.BikeCreatedEvent(command.id()));
    }

    @EventSourcingHandler
    void on(Api.BikeCreatedEvent event) {
        this.id = event.id();
    }

    @CommandHandler
    void rentBike(Api.RentBikeCommand command) {
        apply(new Api.BikeRentedEvent(command.id()));
    }

}
