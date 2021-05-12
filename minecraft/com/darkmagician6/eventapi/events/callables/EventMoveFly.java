package com.darkmagician6.eventapi.events.callables;

import com.darkmagician6.eventapi.events.Event;




public class EventMoveFly implements Event {
    public static float yaw;

    public EventMoveFly(float yaw)  {
        EventMoveFly.yaw = yaw;
    }
}
