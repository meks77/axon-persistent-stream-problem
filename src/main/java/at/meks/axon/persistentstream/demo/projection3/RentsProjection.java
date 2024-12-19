package at.meks.axon.persistentstream.demo.projection3;

import at.meks.axon.persistentstream.demo.Api;
import jakarta.enterprise.context.ApplicationScoped;
import org.axonframework.eventhandling.EventHandler;

@ApplicationScoped
public class RentsProjection {

    private boolean eventReceived;

    @EventHandler
    void on(Api.BikeRentedEvent event) {
        this.eventReceived = true;
    }

    public boolean isEventReceived() {
        return eventReceived;
    }
}
