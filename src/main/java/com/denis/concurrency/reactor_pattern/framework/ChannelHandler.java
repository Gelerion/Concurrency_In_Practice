package com.denis.concurrency.reactor_pattern.framework;

import java.nio.channels.SelectionKey;

/**
 * Represents the <i>EventHandler</i> of Reactor pattern. It handles the incoming events dispatched
 * to it by the {@link Dispatcher}. This is where the application logic resides.
 *
 * <p>
 * A {@link ChannelHandler} can be associated with one or many {@link AbstractNioChannel}s, and
 * whenever an event occurs on any of the associated channels, the handler is notified of the event.
 */
public interface ChannelHandler
{
	void handleChannelRead(AbstractNioChannel channel, Object readObject, SelectionKey key);
}
