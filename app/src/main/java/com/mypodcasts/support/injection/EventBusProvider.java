package com.mypodcasts.support.injection;

import javax.inject.Provider;

import org.greenrobot.eventbus.EventBus;

public class EventBusProvider implements Provider<EventBus> {
  @Override
  public EventBus get() {
    return EventBus.getDefault();
  }
}
