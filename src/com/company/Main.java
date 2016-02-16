package com.company;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

public class Main {

    public static void main(String[] args) throws Exception {
	// write your code here

        String url = "tw-ei-bw-dev1.tw.trendnet.org:7222";
        String username = "admin";
        String pwd = "";
        String service_name = "Sample";


        //MessageQueueService mqs = new MessageQueueService();
        MessageQueueService mqs = new MessageQueueService(url,username,pwd,service_name);
//        mqs.getMessageFromQueue();
        mqs.sendMessageToQueue("Sammi Test3 in Java");


//        Properties props = new Properties();
//        props.setProperty(Context.INITIAL_CONTEXT_FACTORY, "com.tibco.tibjms.naming.TibjmsInitialContextFactory");
//        props.setProperty(Context.PROVIDER_URL, "tcp://tw-ei-bw-dev1.tw.trendnet.org:7222");
//        props.setProperty(Context.SECURITY_PRINCIPAL, "admin");
//        props.setProperty(Context.SECURITY_CREDENTIALS, "");
//        Context context = null;
//        try {
//            context = new InitialContext(props);
//            QueueConnectionFactory qcf = (QueueConnectionFactory) context.lookup("QueueConnectionFactory");
//            QueueConnection qc = qcf.createQueueConnection();
//            QueueSession qs = qc.createQueueSession(false, javax.jms.Session.AUTO_ACKNOWLEDGE);
//            MessageConsumer mc = qs.createConsumer(qs.createQueue("queue.sample"));
//
//            TextMessage msg = qs.createTextMessage();;
//            msg.setText("Sammi test from java");
//            //qs.createSender(qs.createQueue("queue.sample")).send(ms.get());
////            queueToQueue(msg, qs);
//            mc.setMessageListener(new MessageListener() {
//                @Override
//                public void onMessage(Message message) {
//                    if (message instanceof TextMessage) {
//                        TextMessage tm = (TextMessage) message;
//                        try {
//                            System.out.println(tm.getText());
//                        } catch (Exception ex) {
//                            ex.printStackTrace();
//                            try {
//                                qc.close();
//                            } catch (Exception innerex) {
//
//                            }
//                        }
//                    }
//
//                }
//            });
//
//            qc.start();
//        } catch (NamingException e) {
//            e.printStackTrace();
//        }


    }



//    private static void queueToQueue(Message message, javax.jms.Session jmsSession)
//            throws java.lang.Exception {
//        if (message instanceof javax.jms.TextMessage)  {
//            String oldContent = ((javax.jms.TextMessage) message).getText();
//            javax.jms.TextMessage newMessage = jmsSession.createTextMessage("Hello "
//                    + oldContent);
//            jmsSession.createProducer(jmsSession.createQueue("queue.sample")).send(newMessage);
//        }
//    }
}
