package pers.zhangyang.easyguishop.base;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class EventBase extends Event {
    private static final HandlerList HANDLERS = new HandlerList();

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
}
