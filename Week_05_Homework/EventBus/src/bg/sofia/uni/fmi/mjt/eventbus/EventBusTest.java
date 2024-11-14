package bg.sofia.uni.fmi.mjt.eventbus;

import bg.sofia.uni.fmi.mjt.eventbus.events.TestEvent;
import bg.sofia.uni.fmi.mjt.eventbus.events.TestPayload;
import bg.sofia.uni.fmi.mjt.eventbus.subscribers.DeferredEventSubscriber;

import java.time.Instant;

public class EventBusTest {
    public static void main(String[] args) {
        EventBusImpl bus = new EventBusImpl();
        DeferredEventSubscriber<TestEvent> subscriber = new DeferredEventSubscriber<>();

        bus.subscribe(TestEvent.class, subscriber);

        TestEvent event1 = new TestEvent(Instant.now(), 1, "Source1", new TestPayload("Payload1"));
        TestEvent event2 = new TestEvent(Instant.now().plusSeconds(1), 2, "Source2", new TestPayload("Payload2"));

        bus.publish(event1);
        bus.publish(event2);

        System.out.println("Published Events:");
        for (TestEvent event : subscriber) {
            System.out.println("Payload: " + event.getPayload().getPayload());
        }
    }
}