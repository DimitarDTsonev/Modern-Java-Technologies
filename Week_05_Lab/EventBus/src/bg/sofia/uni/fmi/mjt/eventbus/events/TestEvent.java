package bg.sofia.uni.fmi.mjt.eventbus.events;

import java.time.Instant;

public class TestEvent implements Event<TestPayload> {
    private final Instant timestamp;
    private final int priority;
    private final String source;
    private final TestPayload payload;

    public TestEvent(Instant timestamp, int priority, String source, TestPayload payload) {
        this.timestamp = timestamp;
        this.priority = priority;
        this.source = source;
        this.payload = payload;
    }

    /**
     * @return the time when the event was created.
     */
    @Override
    public Instant getTimestamp() {
        return timestamp;
    }

    /**
     * @return the priority of the event. Lower number denotes higher priority.
     */
    @Override
    public int getPriority() {
        return priority;
    }

    /**
     * @return the source of the event.
     */
    @Override
    public String getSource() {
        return source;
    }

    /**
     * @return the payload of the event.
     */
    @Override
    public TestPayload getPayload() {
        return payload;
    }
}