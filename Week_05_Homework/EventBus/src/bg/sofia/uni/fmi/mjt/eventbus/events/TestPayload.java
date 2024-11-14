package bg.sofia.uni.fmi.mjt.eventbus.events;

public class TestPayload implements Payload<String> {
    private final String data;

    public TestPayload(String data) {
        this.data = data;
    }

    @Override
    public int getSize() {
        return data.length();
    }

    @Override
    public String getPayload() {
        return data;
    }
}