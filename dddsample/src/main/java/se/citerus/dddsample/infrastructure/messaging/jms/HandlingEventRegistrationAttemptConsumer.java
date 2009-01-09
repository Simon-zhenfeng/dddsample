package se.citerus.dddsample.infrastructure.messaging.jms;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import se.citerus.dddsample.application.HandlingEventRegistrationAttempt;
import se.citerus.dddsample.application.HandlingEventService;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

/**
 * Consumes handling event registration attempt messages and delegates to
 * proper registration.
 * 
 */
public class HandlingEventRegistrationAttemptConsumer implements MessageListener {

  private HandlingEventService handlingEventService;
  private static final Log logger = LogFactory.getLog(HandlingEventRegistrationAttemptConsumer.class);

  @Override
  public void onMessage(final Message message) {
    try {
      final ObjectMessage om = (ObjectMessage) message;
      handlingEventService.registerHandlingEvent((HandlingEventRegistrationAttempt) om.getObject());
    } catch (Exception e) {
      logger.error(e, e);
    }
  }

  public void setHandlingEventService(HandlingEventService handlingEventService) {
    this.handlingEventService = handlingEventService;
  }

}
