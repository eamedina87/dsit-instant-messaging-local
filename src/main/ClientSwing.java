package main;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import publisher.Publisher;
import subscriber.Subscriber;
import subscriber.SubscriberImpl;
import topicmanager.TopicManager;

public class ClientSwing {

    public Map<String,Subscriber> my_subscriptions;
    Publisher publisher;
    String publisherTopic;
    TopicManager topicManager;
    private SubscriberImpl subscriber;
    
    
    JFrame frame;
    JTextArea topic_list_TextArea;
    public JTextArea messages_TextArea;
    public JTextArea my_subscriptions_TextArea;
    JTextArea publisher_TextArea;
    JTextField argument_TextField;
    
    public ClientSwing(TopicManager topicManager) {
        my_subscriptions = new HashMap<String,Subscriber>();
        publisher = null;
        this.topicManager = topicManager;
    }
    public void createAndShowGUI() {

        frame = new JFrame("Publisher/Subscriber demo");
        frame.setSize(300,300);
        frame.addWindowListener(new CloseWindowHandler());
        
        topic_list_TextArea = new JTextArea(5,10);
        messages_TextArea = new JTextArea(10,20);
        my_subscriptions_TextArea = new JTextArea(5,10);
        publisher_TextArea = new JTextArea(1,10);
        argument_TextField = new JTextField(20);

        topic_list_TextArea.setEditable(false);
        my_subscriptions_TextArea.setEditable(false);
        publisher_TextArea.setEditable(false);
        
        JButton show_topics_button = new JButton("show Topics");
        JButton new_publisher_button = new JButton("new Publisher");
        JButton new_subscriber_button = new JButton("new Subscriber");
        JButton to_unsubscribe_button = new JButton("to unsubscribe");
        JButton to_post_an_event_button = new JButton("post an event");
        JButton to_close_the_app = new JButton("close app.");

        show_topics_button.addActionListener(new showTopicsHandler());
        new_publisher_button.addActionListener(new newPublisherHandler());
        new_subscriber_button.addActionListener(new newSubscriberHandler());
        to_unsubscribe_button.addActionListener(new UnsubscribeHandler());
        to_post_an_event_button.addActionListener(new postEventHandler());
        to_close_the_app.addActionListener(new CloseAppHandler());

        JPanel buttonsPannel = new JPanel(new FlowLayout());
        buttonsPannel.add(show_topics_button);
        buttonsPannel.add(new_publisher_button);
        buttonsPannel.add(new_subscriber_button);
        buttonsPannel.add(to_unsubscribe_button);
        buttonsPannel.add(to_post_an_event_button);
        buttonsPannel.add(to_close_the_app);

        JPanel argumentP = new JPanel(new FlowLayout());
        argumentP.add(new JLabel("Write content to set a new_publisher / new_subscriber / unsubscribe / post_event:"));
        argumentP.add(argument_TextField);

        JPanel topicsP = new JPanel();
        topicsP.setLayout(new BoxLayout(topicsP, BoxLayout.PAGE_AXIS));
        topicsP.add(new JLabel("Topics:"));
        topicsP.add(topic_list_TextArea);
        topicsP.add(new JScrollPane(topic_list_TextArea));
        topicsP.add(new JLabel("My Subscriptions:"));
        topicsP.add(my_subscriptions_TextArea);
        topicsP.add(new JScrollPane(my_subscriptions_TextArea));
        topicsP.add(new JLabel("I'm Publisher of topic:"));
        topicsP.add(publisher_TextArea);
        topicsP.add(new JScrollPane(publisher_TextArea));

        JPanel messagesPanel = new JPanel();
        messagesPanel.setLayout(new BoxLayout(messagesPanel, BoxLayout.PAGE_AXIS));
        messagesPanel.add(new JLabel("Messages:"));
        messagesPanel.add(messages_TextArea);
        messagesPanel.add(new JScrollPane(messages_TextArea));

        Container mainPanel = frame.getContentPane();
        mainPanel.add(buttonsPannel, BorderLayout.PAGE_START);
        mainPanel.add(messagesPanel,BorderLayout.CENTER);
        mainPanel.add(argumentP,BorderLayout.PAGE_END);
        mainPanel.add(topicsP,BorderLayout.LINE_START);

        frame.pack();
        frame.setVisible(true);
    }
    
    public void displaySubscriptions() {
        if (my_subscriptions!=null){
            StringBuilder sb = new StringBuilder();
            for (String sub:my_subscriptions.keySet()){
                sb.append(sub).append("\n");
            }
            my_subscriptions_TextArea.setText(sb.toString());
        }
    }

    class showTopicsHandler implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            Set<String> topics = topicManager.topics();
            StringBuilder sb = new StringBuilder();
            for (String topic:topics){
                sb.append(topic).append("\n");
            }
            topic_list_TextArea.setText(sb.toString());
        }
    }
    class newPublisherHandler implements ActionListener{

        public void actionPerformed(ActionEvent e) {
            if (!topicManager.isTopic(argument_TextField.getText())){
                if (publisherTopic!=null){
                    topicManager.removePublisherFromTopic(publisherTopic);
                    my_subscriptions.remove(publisherTopic);
                }
                publisherTopic = argument_TextField.getText();
                if (subscriber==null){
                    subscriber = new SubscriberImpl(ClientSwing.this);
                }
                publisher = topicManager.addPublisherToTopic(publisherTopic);
                topicManager.subscribe(publisherTopic, subscriber);
                my_subscriptions.put(publisherTopic, subscriber);   
                publisher_TextArea.setText(publisherTopic);
                argument_TextField.setText("");
                displaySubscriptions();
                
            }
            new showTopicsHandler().actionPerformed(e);
        }

        
    }
    class newSubscriberHandler implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            String data = argument_TextField.getText();
            
            if (topicManager.isTopic(data)){
                if (subscriber==null){
                    subscriber = new SubscriberImpl(ClientSwing.this);
                }
                topicManager.subscribe(data, subscriber);
                //Use mysubscriptions
                my_subscriptions.put(data, subscriber);
                displaySubscriptions();
                argument_TextField.setText("");            
            }
            
            new showTopicsHandler().actionPerformed(e);
            
        }
    }
    class UnsubscribeHandler implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            String data = argument_TextField.getText();
            
            if (topicManager.isTopic(data)){
                if (subscriber==null){
                    subscriber = new SubscriberImpl(ClientSwing.this);
                }          
                topicManager.unsubscribe(data, subscriber);
                //Use mysubscriptions
                my_subscriptions.remove(data);
                displaySubscriptions();
                argument_TextField.setText("");
            }
            
            new showTopicsHandler().actionPerformed(e);
        }
    }
    
    class postEventHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            
            if (publisher!=null){
                String event = argument_TextField.getText();
                publisher.publish(publisherTopic, event);
                argument_TextField.setText("");
            }
            
            new showTopicsHandler().actionPerformed(e);
        }
    }
    class CloseAppHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }
    class CloseWindowHandler implements WindowListener{
        public void windowDeactivated(WindowEvent e) {}
        public void windowActivated(WindowEvent e) {}
        public void windowIconified(WindowEvent e) {}
        public void windowDeiconified(WindowEvent e) {}
        public void windowClosed(WindowEvent e) {}
        public void windowOpened(WindowEvent e) {}
        public void windowClosing(WindowEvent e) {
            //...
        }
    }
}

