package com.mypodcasts.support.injection;

import javax.inject.Provider;

import de.greenrobot.event.EventBus;

public class EventBusProvider implements Provider<EventBus> {
  @Override
  public EventBus get() {
    return EventBus.getDefault();
  }
}
