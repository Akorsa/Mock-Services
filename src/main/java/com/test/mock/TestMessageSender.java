package com.test.mock;

import javax.jms.*;

import org.apache.log4j.Logger;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

/**
 * The TestMessageSender class uses the injected JMSTemplate to send a message
 * to a specified Queue. In our case we're sending messages to 'TestQueueTwo'
 */
@Service
public class TestMessageSender {
    private JmsTemplate jmsTemplate;
    private Queue testQueue;
    private static final Logger log = Logger.getLogger(TestMessageSender.class);

    /**
     * Sends message using JMS Template.
     *
     * @param message_p the message_p
     * @throws JMSException the jMS exception
     */
    public void sendMessage(final byte[] message_p, final String corId) throws JMSException {
        log.debug("About to put message on queue. Queue[" + testQueue.toString() + "] Message[" + message_p + "]");
        //jmsTemplate.convertAndSend(testQueue, message_p);
        jmsTemplate.send(testQueue, new MessageCreator() {

            public Message createMessage(Session session) throws JMSException {
                BytesMessage message = session.createBytesMessage();
                message.writeBytes(message_p);
                message.setJMSReplyTo(testQueue);
                message.setJMSCorrelationID(corId);
                return message;
            }
        });
    }

    /**
     * Sets the jms template.
     *
     * @param tmpl the jms template
     */
    public void setJmsTemplate(JmsTemplate tmpl) {
        this.jmsTemplate = tmpl;
    }

    /**
     * Sets the test queue.
     *
     * @param queue the new test queue
     */
    public void setTestQueue(Queue queue)
    {
        this.testQueue = queue;
    }
}