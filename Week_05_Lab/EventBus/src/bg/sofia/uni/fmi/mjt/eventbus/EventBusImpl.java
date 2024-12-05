package bg.sofia.uni.fmi.mjt.eventbus;

import bg.sofia.uni.fmi.mjt.eventbus.events.Event;
import bg.sofia.uni.fmi.mjt.eventbus.exception.MissingSubscriptionException;
import bg.sofia.uni.fmi.mjt.eventbus.subscribers.Subscriber;

import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;

public class EventBusImpl implements EventBus {

    private final Map<Class<? extends Event<?>>, Set<Subscriber<?>>> subscribers = new HashMap<>();
    private final Map<Class<? extends Event<?>>, List<Event<?>>> eventLogs = new HashMap<>();

    /**
     * Subscribes the given subscriber to the given event type.
     *
     * @param eventType  the type of event to subscribe to
     * @param subscriber the subscriber to subscribe
     * @throws IllegalArgumentException if the event type is null
     * @throws IllegalArgumentException if the subscriber is null
     */
    @Override
    public <T extends Event<?>> void subscribe(Class<T> eventType, Subscriber<? super T> subscriber) {
        if (eventType == null || subscriber == null) {
            throw new IllegalArgumentException("Event type and subscriber cannot be null");
        }

        Set<Subscriber<?>> eventSubscribers = subscribers.get(eventType);
        if (eventSubscribers == null) {
            eventSubscribers = new HashSet<>();
            subscribers.put(eventType, eventSubscribers);
        }
        eventSubscribers.add(subscriber);
    }

    /**
     * Unsubscribes the given subscriber from the given event type.
     *
     * @param eventType  the type of event to unsubscribe from
     * @param subscriber the subscriber to unsubscribe
     * @throws IllegalArgumentException     if the event type is null
     * @throws IllegalArgumentException     if the subscriber is null
     * @throws MissingSubscriptionException if the subscriber is not subscribed to the event type
     */
    @Override
    public <T extends Event<?>> void unsubscribe(Class<T> eventType, Subscriber<? super T> subscriber)
            throws MissingSubscriptionException {
        if (eventType == null || subscriber == null) {
            throw new IllegalArgumentException("Event type and subscriber cannot be null");
        }

        Set<Subscriber<?>> eventSubscribers = subscribers.get(eventType);
        if (eventSubscribers == null || !eventSubscribers.remove(subscriber)) {
            throw new MissingSubscriptionException("Subscriber is not subscribed to the event type");
        }

        if (eventSubscribers.isEmpty()) {
            subscribers.remove(eventType);
        }
    }

    /**
     * Publishes the given event to all subscribers of the event type.
     *
     * @param event the event to publish
     * @throws IllegalArgumentException if the event is null
     */
    @Override
    public <T extends Event<?>> void publish(T event) {
        if (event == null) {
            throw new IllegalArgumentException("Event cannot be null");
        }

        Class<? extends Event<?>> eventType = (Class<? extends Event<?>>) event.getClass();

        Set<Subscriber<?>> eventSubscribers = subscribers.get(eventType);
        if (eventSubscribers != null) {
            for (Subscriber<?> subscriber : eventSubscribers) {
                @SuppressWarnings("unchecked")
                Subscriber<T> typedSubscriber = (Subscriber<T>) subscriber;
                typedSubscriber.onEvent(event);
            }
        }

        List<Event<?>> events = eventLogs.get(eventType);
        if (events == null) {
            events = new ArrayList<>();
            eventLogs.put(eventType, events);
        }
        events.add(event);
    }

    /**
     * Clears all subscribers and event logs.
     */
    @Override
    public void clear() {
        subscribers.clear();
        eventLogs.clear();
    }

    /**
     * Returns all events of the given event type that occurred between the given timestamps. If
     * {@code from} and {@code to} are equal the returned collection is empty.
     * <p> {@code from} - inclusive, {@code to} - exclusive. </p>
     *
     * @param eventType the type of event to get
     * @param from      the start timestamp (inclusive)
     * @param to        the end timestamp (exclusive)
     * @return an unmodifiable collection of events of the given event type that occurred between
     * the given timestamps
     * @throws IllegalArgumentException if the event type is null
     * @throws IllegalArgumentException if the start timestamp is null
     * @throws IllegalArgumentException if the end timestamp is null
     */
    @Override
    public Collection<? extends Event<?>> getEventLogs(Class<? extends Event<?>> eventType, Instant from, Instant to) {
        if (eventType == null || from == null || to == null) {
            throw new IllegalArgumentException("Event type and timestamps cannot be null");
        }

        List<Event<?>> result = new ArrayList<>();
        List<Event<?>> events = eventLogs.get(eventType);

        if (events != null) {
            for (Event<?> event : events) {
                Instant timestamp = event.getTimestamp();
                if (!timestamp.isBefore(from) && timestamp.isBefore(to)) {
                    result.add(event);
                }
            }

            for (int i = 1; i < result.size(); i++) {
                Event<?> currentEvent = result.get(i);
                int j = i - 1;

                while (j >= 0 && result.get(j).getTimestamp().isAfter(currentEvent.getTimestamp())) {
                    result.set(j + 1, result.get(j));
                    j--;
                }
                result.set(j + 1, currentEvent);
            }
        }

        return Collections.unmodifiableList(result);
    }

    /**
     * Returns all subscribers for the given event type in an unmodifiable collection. If there are
     * no subscribers for the event type, the method returns an empty unmodifiable collection.
     *
     * @param eventType the type of event to get subscribers for
     * @return an unmodifiable collection of subscribers for the given event type
     * @throws IllegalArgumentException if the event type is null
     */
    @Override
    public <T extends Event<?>> Collection<Subscriber<?>> getSubscribersForEvent(Class<T> eventType) {
        if (eventType == null) {
            throw new IllegalArgumentException("Event type cannot be null");
        }

        Set<Subscriber<?>> eventSubscribers = subscribers.get(eventType);
        if (eventSubscribers == null) {
            return Collections.emptySet();
        }
        return Collections.unmodifiableSet(eventSubscribers);
    }
}