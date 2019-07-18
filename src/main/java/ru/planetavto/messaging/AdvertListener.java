package ru.planetavto.messaging;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class AdvertListener {

  @JmsListener(destination = "NewOrModifiedAdvert")
  public void readMessage(String message) {
    System.out.println("mess: "+message);
  }
  
}