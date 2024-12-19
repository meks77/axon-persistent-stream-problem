package at.meks.axon.persistentstream.demo.projection2;

import at.meks.axon.persistentstream.demo.Api;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import org.axonframework.eventhandling.EventHandler;

@ApplicationScoped
public class Projection2_1 {

    private boolean eventReceived;

    @EventHandler
    void on (Api.BikeCreatedEvent event) {
        Log.info("received event: " + event);
        this.eventReceived = true;
    }

    public boolean isEventReceived() {
        return eventReceived;
    }
}
