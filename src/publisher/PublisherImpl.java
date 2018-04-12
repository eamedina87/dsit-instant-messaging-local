package publisher;

import java.util.HashSet;
import java.util.Set;
import subscriber.Subscriber;
import subscriber.SubscriberImpl;
import topicmanager.TopicManager;
import topicmanager.TopicManagerImpl;

public class PublisherImpl implements PublisherAdmin, Publisher {

    
    private Set<Subscriber> subscriberSet;
    private int numPublishers;
    private String mTopic;
    private static PublisherImpl instance = null;
    
    /*
    protected PublisherImpl(String topic){
        if (!topic.equals(mTopic)){
        subscriberSet = new HashSet<Subscriber>();
        numPublishers = 1;
        this.mTopic = topic;
        }
    }
    
    public static PublisherImpl getInstance(String topic) {       
      if(instance == null) {
         instance = new PublisherImpl(topic);
      }
      return instance;
   }
    */
    
    public PublisherImpl(String topic){
        subscriberSet = new HashSet<Subscriber>();
        numPublishers = 1;
        this.mTopic = topic;
    }
    
   
    
    public int incPublishers(){
        return ++numPublishers;
    }
    public int decPublishers(){
        return --numPublishers;
    }
    public void attachSubscriber(Subscriber subscriber) {
        if(subscriber!=null)
            subscriberSet.add(subscriber);
    }
    public void detachSubscriber(Subscriber subscriber) {
        if(subscriber!=null && subscriberSet.contains(subscriber))
            subscriberSet.remove(subscriber);
    }
    public void detachAllSubscribers() {
        if (!subscriberSet.isEmpty())
            subscriberSet = new HashSet<Subscriber>();
    }
    public void publish(String topic, String event) {
        for (Subscriber subscriber:subscriberSet){
            subscriber.onEvent(topic, event);
        }
    }
}
