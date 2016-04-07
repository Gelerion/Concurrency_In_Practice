package com.denis.concurrency.reactor_pattern.framework;

import java.io.IOException;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * A Reactor runs in a separate thread and its job is to react to IO events by dispatching
 * the work to the appropriate handler. Its like a telephone operator in a company who answers
 * the calls from clients and transfers the communication line to the appropriate receiver.
 * Don't go too far with the analogy though :)
 * <p>
 * This class acts as Synchronous Event De-multiplexer and Initiation Dispatcher of Reactor pattern. Multiple handles
 * i.e. {@link AbstractNioChannel}s can be registered to the reactor and it blocks for events from all these handles.
 * Whenever an event occurs on any of the registered handles, it synchronously de-multiplexes the event which can be any
 * of read, write or accept, and dispatches the event to the appropriate {@link ChannelHandler} using the
 * {@link Dispatcher}.
 * <p>
 * Implementation: A NIO reactor runs in its own thread when it is started using {@link #start()} method.
 * {@link NioReactor} uses {@link Selector} for realizing Synchronous Event De-multiplexing.
 * <p>
 * NOTE: This is one of the ways to implement NIO reactor and it does not take care of all possible edge cases which are
 * required in a real application. This implementation is meant to demonstrate the fundamental concepts that lie behind
 * Reactor pattern.
 */
public class NioReactor
{
	private final Selector selector;
	private final Dispatcher dispatcher;
	/**
	 * All the work of altering the SelectionKey operations and Selector operations are performed in the context of main
	 * event loop of reactor. So when any channel needs to change its readability or writability, a new commands is added
	 * in the commands queue and then the event loop picks up the commands and executes it in next iteration.
	 */
	private final Queue<Runnable> pendingCommands = new ConcurrentLinkedQueue<>();
	private final ExecutorService reactorMain = Executors.newSingleThreadExecutor();

	/**
	 * Creates a reactor which will use provided {@code dispatcher} to dispatch events. The application can provide
	 * various implementations of dispatcher which suits its needs.
	 *
	 * @param dispatcher a non-null dispatcher used to dispatch events on registered channels.
	 * @throws IOException if any I/O error occurs.
	 */
	public NioReactor(Dispatcher dispatcher) throws IOException
	{
		this.dispatcher = dispatcher;
		this.selector = Selector.open();
	}

	public NioReactor registerChannel(AbstractNioChannel channel) throws ClosedChannelException
	{
		SelectionKey key = channel.getJavaChannel().register(selector, channel.getInterestedOps());
		key.attach(channel);
		channel.setReactor(this);
		return this;
	}

	/**
	 * Starts the reactor event loop in a new thread.
	 *
	 * @throws IOException if any I/O error occurs.
	 */
	public void start() throws IOException
	{
		reactorMain.execute(() -> {
			System.out.println("Reactor started, waiting for events...");
			try
			{
				eventLoop();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		});
	}

	/**
	 * Stops the reactor and related resources such as dispatcher.
	 *
	 * @throws InterruptedException if interrupted while stopping the reactor.
	 * @throws IOException          if any I/O error occurs.
	 */
	public void stop() throws InterruptedException, IOException
	{
		reactorMain.shutdownNow();
		selector.wakeup();
		reactorMain.awaitTermination(4, TimeUnit.SECONDS);
		selector.close();
	}

	private void eventLoop() throws IOException
	{
		while (true)
		{
			if (Thread.interrupted())
			{
				break;
			}

			processPendingCommands();

		  /*
		   * Synchronous event de-multiplexing happens here, this is blocking call which returns when it is possible to
		   * initiate non-blocking operation on any of the registered channels.
		   */
			selector.select();

			Set<SelectionKey> keys = selector.selectedKeys();
			Iterator<SelectionKey> iterator = keys.iterator();

			while (iterator.hasNext())
			{
				SelectionKey key = iterator.next();

				if(!key.isValid()) {
					iterator.remove();
					continue;
				}

				processKey(key);
			}

			keys.clear();
		}
	}

	private void processKey(SelectionKey key) throws IOException
	{
		if (key.isAcceptable())
		{
			onChannelAcceptable(key);
		}
		else if (key.isReadable())
		{
			onChannelReadable(key);
		}
		else if (key.isWritable())
		{
			onChannelWritable(key);
		}
	}

	private void processPendingCommands() {
		Iterator<Runnable> iterator = pendingCommands.iterator();
		while (iterator.hasNext())
		{
			Runnable command = iterator.next();
			command.run();
			iterator.remove();
		}
	}

	private void onChannelAcceptable(SelectionKey key) throws IOException {
		ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
		SocketChannel socketChannel = serverSocketChannel.accept();
		socketChannel.configureBlocking(false);
		SelectionKey readKey = socketChannel.register(selector, SelectionKey.OP_READ);
		readKey.attach(key.attachment());
	}

	private void onChannelWritable(SelectionKey key) throws IOException {
		AbstractNioChannel channel = (AbstractNioChannel) key.attachment();
		channel.flush(key);
	}

	private void onChannelReadable(SelectionKey key) {
		try {
			// reads the incoming data in context of reactor main loop. Can this be improved?
			Object readObject = ((AbstractNioChannel) key.attachment()).read(key);

			dispatchReadEvent(key, readObject);
		} catch (IOException e) {
			try {
				key.channel().close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	private void dispatchReadEvent(SelectionKey key, Object readObject)
	{
		dispatcher.onChannelReadEvent((AbstractNioChannel) key.attachment(), readObject, key);
	}

	/**
	 * Queues the change of operations request of a channel, which will change the interested operations of the channel
	 * sometime in future.
	 * <p>
	 * This is a non-blocking method and does not guarantee that the operations have changed when this method returns.
	 *
	 * @param key
	 *          the key for which operations have to be changed.
	 * @param interestedOps
	 *          the new interest operations.
	 */
	public void changeOps(SelectionKey key, int interestedOps)
	{
		pendingCommands.add(new ChangedKeyOpsCommand(key, interestedOps));
		selector.wakeup();
	}

	class ChangedKeyOpsCommand implements Runnable
	{
		private SelectionKey key;
		private int interestedOps;

		public ChangedKeyOpsCommand(SelectionKey key, int interestedOps)
		{
			this.key = key;
			this.interestedOps = interestedOps;
		}

		@Override
		public void run()
		{
			key.interestOps(interestedOps);
		}

		@Override
		public String toString()
		{
			return "Change of ops to: " + interestedOps;
		}
	}
}
