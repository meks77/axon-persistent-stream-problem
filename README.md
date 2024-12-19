# Persistent Stream Problem

I created a quarkus extension for the axon framwork.

While using it I recognized the issue that, when more event handler listen to the same events, not all are invoked.

This seems to happen if the event handler classes are not covered by the same event processor. This happens only if persistent streams are used. 

The question is, if this is an error in the extension, while creating the configuration, or if it is a bug in the axon framework.

In the extension, only a defaul processor is configured for all even handler.

## Infrastructur

If you have running a container daemon, the database and the axon server are started automatically in a container.

If you don't have a container daemon, you have to configure the database and axon server connection manually.

For the test with the tracking processor, a database is necessary.
For persistent streams, the axon server is in every case used as event store.

## How to reproduce

I have setup a test. 

There existist the aggregate [Bike](src/main/java/at/meks/axon/persistentstream/demo/model/Bike.java), the [command CreateBikeCommand and the event BikeCreatedEvent](src/main/java/at/meks/axon/persistentstream/demo/Api.java).

There 4 projection class which listen to the event BikeCreatedEvent.

2 projection classes are in the package [at.meks.axon.persistentstream.demo.projection1](src/main/java/at/meks/axon/persistentstream/demo/projection1). 

The remaining 2 projection classes are in the package [at.meks.axon.persistentstream.demo.projection2](src/main/java/at/meks/axon/persistentstream/demo/projection2).

I also create a [test](src/test/java/at/meks/axon/persistentstream/demo/EventHandlingTest.java) which sends the command, and expectes that all 4 projection classes have been informed.

If using e.g. the tracking processor, everything works as expected.

If using persistent streams, event handler only of one package are invoked.

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

When executing the test, it finishes successfull.

## Using persistent streams -> Error
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

When executing the test, it will fail