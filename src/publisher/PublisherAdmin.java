package publisher;

import subscriber.Subscriber;

public interface PublisherAdmin extends Publisher {
    public int  incPublishers();
    public int  decPublishers();
    public void attachSubscriber(Subscriber subscriber);
    public void detachSubscriber(Subscriber subscriber);
    public void detachAllSubscribers();
}
