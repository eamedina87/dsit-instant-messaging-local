package topicmanager;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import publisher.Publisher;
import publisher.PublisherAdmin;
import publisher.PublisherImpl;
import subscriber.Subscriber;

public class TopicManagerImpl implements TopicManager {

    private Map<String,PublisherAdmin> topicMap;

    public TopicManagerImpl() {
        topicMap = new HashMap<String,PublisherAdmin>();
    }
    public boolean isTopic(String topic){
        return topicMap.containsKey(topic);
    }
    public Set<String> topics(){
        return topicMap.keySet();
    }
    public Publisher addPublisherToTopic(String topic){        
        PublisherImpl publisher = new PublisherImpl(topic);
        //TODO publisher.incPublishers();
        topicMap.put(topic, publisher);
        return publisher;
    }
    public int removePublisherFromTopic(String topic){
        topicMap.remove(topic);
        return -1;
    }
    public boolean subscribe(String topic, Subscriber subscriber){
        PublisherAdmin admin = (PublisherAdmin)topicMap.get(topic);
        admin.attachSubscriber(subscriber);
        return true;
    }
    public boolean unsubscribe(String topic, Subscriber subscriber){
        ((PublisherAdmin)topicMap.get(topic)).detachSubscriber(subscriber);
        return true;
    }
}