package at.meks.axon.persistentstream.demo;

import at.meks.axon.persistentstream.demo.projection1.Projection1_1;
import at.meks.axon.persistentstream.demo.projection1.Projection1_2;
import at.meks.axon.persistentstream.demo.projection2.Projection2_1;
import at.meks.axon.persistentstream.demo.projection2.Projection2_2;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.junit.jupiter.api.*;

import java.time.Duration;
import java.util.UUID;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class EventHandlingTest {

    @Inject CommandGateway commandGateway;
    @Inject Projection1_1 projection1_1;
    @Inject Projection1_2 projection1_2;
    @Inject Projection2_1 projection2_1;
    @Inject Projection2_2 projection2_2;


    @Test
    void testEventhandlerReceivedEvent() {
        commandGateway.sendAndWait(new Api.CreateBikeCommand(UUID.randomUUID().toString()));

        assertThat(() -> assertTrue(projection1_1.isEventReceived()));
        assertThat(() -> assertTrue(projection1_2.isEventReceived()));
        assertThat(() -> assertTrue(projection2_1.isEventReceived()));
        assertThat(() -> assertTrue(projection2_2.isEventReceived()));
    }

    private void assertThat(Runnable assertion) {
        await().pollDelay(Duration.ofMillis(100))
                .atMost(Duration.ofSeconds(5))
                .pollInterval(Duration.ofMillis(100))
               .untilAsserted(assertion::run);
    }
}