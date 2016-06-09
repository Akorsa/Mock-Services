
package com.test.mock;

import javax.jms.*;

import org.apache.log4j.Logger;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class TestMessageListener implements MessageListener {

    private TestMessageSender messageSender;

    private static final Logger log = Logger.getLogger(TestMessageListener.class);

    public static byte[] getResponse() throws Exception {
        Path path = Paths.get(
                TestMessageListener.class.getClassLoader().getResource("ResponseCIF.xml").toURI());

        byte[] encoded = Files.readAllBytes(path);
        return encoded;
    }


    /**
     * Method implements JMS onMessage and acts as the entry
     * point for messages consumed by Springs DefaultMessageListenerContainer.
     * When DefaultMessageListenerContainer picks a message from the queue it
     * invokes this method with the message payload.
     */
    public void onMessage(Message message) {
        log.debug("Received message from queue [" + message + "]");

		/* The message must be of type TextMessage */
        if (message instanceof TextMessage) {
            try {
                String msgText = ((TextMessage) message).getText();
                String corID = ((TextMessage) message).getJMSCorrelationID();

                log.debug("About to process message: " + msgText);
                byte[] resp = getResponse();

				/* call message sender to put message onto second queue */
                messageSender.sendMessage(resp, corID);

            } catch (Exception jmsEx) {
                String errMsg = "An error occurred extracting message";
                log.error(errMsg, jmsEx);
            }
        } else {
            String errMsg = "Message is not of expected type TextMessage";
            log.error(errMsg);
            throw new RuntimeException(errMsg);
        }
    }
    /**
     * Sets the message sender.
     *
     * @param messageSender the new message sender
     */
    public void setTestMessageSender(TestMessageSender messageSender)
    {
        this.messageSender = messageSender;
    }

}
