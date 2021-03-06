package com.denis.concurrency.reactor_pattern.framework;

import java.io.IOException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * This represents the <i>Handle</i> of Reactor pattern. These are resources managed by OS which can
 * be submitted to {@link NioReactor}.
 * <p>
 * <p>
 * This class serves has the responsibility of reading the data when a read event occurs and writing
 * the data back when the channel is writable. It leaves the reading and writing of data on the
 * concrete implementation. It provides a block writing mechanism wherein when any
 * {@link ChannelHandler} wants to write data back, it queues the data in pending write queue and
 * clears it in block manner. This provides better throughput.
 */
public abstract class AbstractNioChannel
{
	private final SelectableChannel channel;
	private final ChannelHandler handler;
	private final Map<SelectableChannel, Queue<Object>> channelToPendingWriters = new ConcurrentHashMap<>();
	private NioReactor reactor;

	/**
	 * Creates a new channel.
	 *
	 * @param handler which will handle events occurring on this channel.
	 * @param channel a NIO channel to be wrapped.
	 */
	public AbstractNioChannel(SelectableChannel channel, ChannelHandler handler)
	{
		this.channel = channel;
		this.handler = handler;
	}

	/**
	 * Injects the reactor in this channel.
	 */
	void setReactor(NioReactor reactor) {
		this.reactor = reactor;
	}

	/**
	 * @return the wrapped NIO channel.
	 */
	public SelectableChannel getJavaChannel() {
		return channel;
	}

	/**
	 * The operation in which the channel is interested, this operation is provided to
	 * {@link Selector}.
	 *
	 * @return interested operation.
	 * @see SelectionKey
	 */
	public abstract int getInterestedOps();

	/**
	 * Binds the channel on provided port.
	 *
	 * @throws IOException if any I/O error occurs.
	 */
	public abstract void bind() throws IOException;

	/**
	 * Reads the data using the key and returns the read data. The underlying channel should be
	 * fetched using {@link SelectionKey#channel()}.
	 *
	 * @param key the key on which read event occurred.
	 * @return data read.
	 * @throws IOException if any I/O error occurs.
	 */
	public abstract Object read(SelectionKey key) throws IOException;

	/**
	 * @return the handler associated with this channel.
	 */
	public ChannelHandler getHandler()
	{
		return handler;
	}

	void flush(SelectionKey key) throws IOException
	{
		Queue<Object> pendingWriters = channelToPendingWriters.get(channel);

		while (true)
		{
			Object pendingWrite = pendingWriters.poll();
			if (pendingWrite == null)
			{
				// We don't have anything more to write so channel is interested in reading more data
				reactor.changeOps(key, SelectionKey.OP_READ);
				break;
			}

			// ask the concrete channel to make sense of data and write it to java channel
			doWrite(pendingWrite, key);
		}
	}

	/**
	 * Writes the data to the channel.
	 *
	 * @param pendingWrite the data to be written on channel.
	 * @param key the key which is writable.
	 * @throws IOException if any I/O error occurs.
	 */
	protected abstract void doWrite(Object pendingWrite, SelectionKey key) throws IOException;

	/**
	 * Queues the data for writing. The data is not guaranteed to be written on underlying channel
	 * when this method returns. It will be written when the channel is flushed.
	 *
	 * <p>
	 * This method is used by the {@link ChannelHandler} to send reply back to the client. <br/>
	 * @param data the data to be written on underlying channel.
	 * @param key the key which is writable.
	 */
	public void write(Object data, SelectionKey key)
	{
		Queue<Object> pendingWriters = this.channelToPendingWriters.get(key.channel());
		if (pendingWriters == null)
		{
			synchronized (this.channelToPendingWriters)
			{
				pendingWriters = this.channelToPendingWriters.get(key.channel());
				if (pendingWriters == null)
				{
					pendingWriters = new ConcurrentLinkedQueue<>();
					this.channelToPendingWriters.put(key.channel(), pendingWriters);
				}
			}
		}

		pendingWriters.add(data);
		reactor.changeOps(key, SelectionKey.OP_WRITE);
	}
}
