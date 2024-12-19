# Persistent Stream Problem

I created a quarkus extension for the axon framwork.

While using it, I recognized the issue that if event handler of different packages were registered, only the one which was registered last is invoked.

This happens only if persistent streams are used. 

The question is, if this is an error in the extension, while creating the configuration, or if it is a bug in the axon framework.

In the extension, only a default processor is configured for all event handler.

## Infrastructur

If you have a container daemon, the database and the axon server are started automatically in a container.

If you don't have a container daemon, you have to configure the database and axon server connection manually.

For the test with the tracking processor, a database is necessary.
For persistent streams, the axon server is used as event store in every case.

## How to reproduce

I have set up a test. 

There exists the aggregate [Bike](src/main/java/at/meks/axon/persistentstream/demo/model/Bike.java), the [command CreateBikeCommand and the event BikeCreatedEvent](src/main/java/at/meks/axon/persistentstream/demo/Api.java).

There are 4 event handler classes, which listen to the event BikeCreatedEvent. Furthermore, there is 1 event handler class, which listens just for BikeRentedEvent.

2 projection classes are in the package [at.meks.axon.persistentstream.demo.projection1](src/main/java/at/meks/axon/persistentstream/demo/projection1). 

The remaining 2 projection classes for BikeCreatedEvent are in the package [at.meks.axon.persistentstream.demo.projection2](src/main/java/at/meks/axon/persistentstream/demo/projection2).

The event handler for BikeRentedEvent is in the package [at.meks.axon.persistentstream.demo.projection3](src/main/java/at/meks/axon/persistentstream/demo/projection3)

I also created a [test](src/test/java/at/meks/axon/persistentstream/demo/EventHandlingTest.java) which sends the command and expectes that all 5 projection classes have been informed.

If using e.g., the tracking processor, everything works as expected.

If using persistent streams, the event handlers of only one package are invoked.

## Using tracking processor -> Success
In the [pom.xml](pom.xml) disable the dependency for the persistent stream and enable the dependencies for the tracking processor.

```xml
        <!-- Enable this dependencies to run with persistent stream -->
<!--        <dependency>-->
<!--            <groupId>at.meks.quarkiverse.axonframework-extension</groupId>-->
<!--            <artifactId>quarkus-axon-persistent-stream-eventprocessor</artifactId>-->
<!--            <version>${quarkus.axon.extension.version}</version>-->
<!--        </dependency>-->
        <!-- persistent stream dependencies end -->
        <!-- Enable this dependencies to run with tracking processor-->
        <dependency>
            <groupId>at.meks.quarkiverse.axonframework-extension</groupId>
            <artifactId>quarkus-axon-tracking-eventprocessor</artifactId>
            <version>${quarkus.axon.extension.version}</version>
        </dependency>
        <dependency>
            <groupId>at.meks.quarkiverse.axonframework-extension</groupId>
            <artifactId>quarkus-axon-tokenstore-jdbc</artifactId>
            <version>${quarkus.axon.extension.version}</version>
        </dependency>
        <dependency>
            <groupId>io.quarkus</groupId>
            <artifactId>quarkus-agroal</artifactId>
        </dependency>
        <dependency>
            <groupId>io.quarkus</groupId>
            <artifactId>quarkus-jdbc-mariadb</artifactId>
        </dependency>
        <!-- tracking event processor end -->
```

When executing the test, it finishes successfully.

## Using persistent streams -> Error

Only the event handler, which is registered as last(projection3), is invoked, independently if the event handler listens for the same event or not.

In the [pom.xml](pom.xml) enable the dependency for the persistent stream and disable the dependencies for the tracking processor.

```xml
        <!-- Enable this dependencies to run with persistent stream-->
        <dependency>
            <groupId>at.meks.quarkiverse.axonframework-extension</groupId>
            <artifactId>quarkus-axon-persistent-stream-eventprocessor</artifactId>
            <version>${quarkus.axon.extension.version}</version>
        </dependency>
        <!-- persistent stream dependencies end -->
        <!-- Enable this dependencies to run with tracking processor-->
<!--        <dependency>-->
<!--            <groupId>at.meks.quarkiverse.axonframework-extension</groupId>-->
<!--            <artifactId>quarkus-axon-tracking-eventprocessor</artifactId>-->
<!--            <version>${quarkus.axon.extension.version}</version>-->
<!--        </dependency>-->
<!--        <dependency>-->
<!--            <groupId>at.meks.quarkiverse.axonframework-extension</groupId>-->
<!--            <artifactId>quarkus-axon-tokenstore-jdbc</artifactId>-->
<!--            <version>${quarkus.axon.extension.version}</version>-->
<!--        </dependency>-->
<!--        <dependency>-->
<!--            <groupId>io.quarkus</groupId>-->
<!--            <artifactId>quarkus-agroal</artifactId>-->
<!--        </dependency>-->
<!--        <dependency>-->
<!--            <groupId>io.quarkus</groupId>-->
<!--            <artifactId>quarkus-jdbc-mariadb</artifactId>-->
<!--        </dependency>-->
        <!-- tracking event processor end -->
```

When executing the test, it will fail.

In the log output you will see four assertion errors.