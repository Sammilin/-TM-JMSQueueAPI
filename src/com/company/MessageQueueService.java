package com.company;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

/**
 * Created by sammi_lin on 16/2/4.
 */
public class MessageQueueService {
    private String service_url = "localhost:7222";
    private String username="";
    private String password="";
    private String service_name = "";

    private String queue_prefix = "trend.multi.";
    private Properties props = new Properties();

    public void setService_url(String service_url) {
        this.service_url = service_url;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setService_name(String servicename) {
        this.service_name = servicename;
    }


    public String getService_url() {
        return service_url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getService_name() {
        return service_name;
    }


    public MessageQueueService(String service_url, String username, String password, String service_name) {
        this.service_url = service_url;
        this.username = username;
        this.password = password;
        this.service_name = service_name;

        props.setProperty(Context.INITIAL_CONTEXT_FACTORY, "com.tibco.tibjms.naming.TibjmsInitialContextFactory");
        props.setProperty(Context.PROVIDER_URL, "tcp://"+ service_url);
        props.setProperty(Context.SECURITY_PRINCIPAL, username);
        props.setProperty(Context.SECURITY_CREDENTIALS, password);

    }

    public MessageQueueService() {
        //Properties props = new Properties();
        props.setProperty(Context.INITIAL_CONTEXT_FACTORY, "com.tibco.tibjms.naming.TibjmsInitialContextFactory");
        props.setProperty(Context.PROVIDER_URL, "tcp://"+ service_url);
        props.setProperty(Context.SECURITY_PRINCIPAL, username);
        props.setProperty(Context.SECURITY_CREDENTIALS, password);
    }

    public void sendMessageToQueue(String contentMessage){
        Context context = null;
        String queueName = queue_prefix+service_name;

        try {
            context = new InitialContext(props);
            QueueConnectionFactory qcf = (QueueConnectionFactory) context.lookup("QueueConnectionFactory");
            QueueConnection qc = qcf.createQueueConnection();
            QueueSession qs = qc.createQueueSession(false, javax.jms.Session.AUTO_ACKNOWLEDGE);

            TextMessage msg = qs.createTextMessage();
            msg.setText(contentMessage);

            queueToQueue(msg, qs,queueName);
            qc.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Object getMessageFromQueue() throws NamingException, JMSException {
        Context context = null;
        String queueName = queue_prefix+service_name;
        String content_msg ="";
        try {
            context = new InitialContext(props);
            QueueConnectionFactory qcf = (QueueConnectionFactory) context.lookup("QueueConnectionFactory");
            QueueConnection qc = qcf.createQueueConnection();
            QueueSession qs = qc.createQueueSession(false, javax.jms.Session.AUTO_ACKNOWLEDGE);
            MessageConsumer mc = qs.createConsumer(qs.createQueue(queueName));

            mc.setMessageListener(new MessageListener() {
                @Override
                public void onMessage(Message message) {
                    String msgText;

                    if (message instanceof TextMessage) {
                        TextMessage tm = (TextMessage) message;
                        try {
                            System.out.println(tm.getText());
                            msgText = tm.getText();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            try {
                                qc.close();
                            } catch (Exception innerex) {

                            }
                        }

                    }
                }
            });

            mc.close();
            qs.close();
//            qc.start();
        } catch (NamingException e) {
            e.printStackTrace();
        }
        return content_msg;
    }

//
//    private Object getMessageData(Message message) throws JMSException {
//        Object messageData = null;
//
//        if (message instanceof ObjectMessage) {
//            messageData = ((ObjectMessage) message).getObject();
//        } else if (message instanceof TextMessage) {
//            TextMessage textMessage = (TextMessage) message;
//
//            if (message.getBooleanProperty(SERIALIZED_DATA_INDICATOR)) {
//                messageData = new JSLiteral(textMessage.getText());
//            } else {
//                messageData = textMessage.getText();
//            }
//        }
//
//        return messageData;
//    }




    private void queueToQueue(Message message, javax.jms.Session jmsSession, String queueName)
            throws java.lang.Exception {
        if (message instanceof javax.jms.TextMessage)  {
            String oldContent = ((javax.jms.TextMessage) message).getText();
            javax.jms.TextMessage newMessage = jmsSession.createTextMessage(oldContent);
            jmsSession.createProducer(jmsSession.createQueue(queueName)).send(newMessage);
            jmsSession.close();

        }
    }
}
