package com.denis.concurrency.reactor_pattern.framework;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;

/**
 * A wrapper over {@link DatagramChannel} which can read and write data on a DatagramChannel.
 */
public class NioDatagramChannel extends AbstractNioChannel
{
	private final int port;

	/**
	 * Creates a {@link DatagramChannel} which will bind at provided port and use <code>handler</code>
	 * to handle incoming events on this channel.
	 * <p>
	 * Note the constructor does not bind the socket, {@link #bind()} method should be called for
	 * binding the socket.
	 *
	 * @param port the port to be bound to listen for incoming datagram requests.
	 * @param handler the handler to be used for handling incoming requests on this channel.
	 * @throws IOException if any I/O error occurs.
	 */
	public NioDatagramChannel(int port, ChannelHandler handler) throws IOException
	{
		super(DatagramChannel.open(), handler);
		this.port = port;
	}

	@Override
	public int getInterestedOps() {
		/*
		 * there is no need to accept connections in UDP, so the channel shows interest in reading data.
		 */
		return SelectionKey.OP_READ;
	}

	/**
	 * Binds UDP socket on the provided <code>port</code>.
	 *
	 * @throws IOException if any I/O error occurs.
	 */
	@Override
	public void bind() throws IOException
	{
		getJavaChannel().socket().bind(new InetSocketAddress(InetAddress.getLocalHost(), port));
		getJavaChannel().configureBlocking(false);
		System.out.println("Bound UDP socket at port: " + port);

	}

	@Override
	public DatagramPacket read(SelectionKey key) throws IOException
	{
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		SocketAddress sender = ((DatagramChannel) key.channel()).receive(buffer);

		 /*
		 * It is required to create a DatagramPacket because we need to preserve which socket address
		 * acts as destination for sending reply packets.
		 */
		buffer.flip();
		DatagramPacket packet = new DatagramPacket(buffer);
		packet.setSender(sender);

		return packet;
	}

	/**
	 * @return the underlying datagram channel.
	 */
	@Override
	public DatagramChannel getJavaChannel() {
		return (DatagramChannel) super.getJavaChannel();
	}

	/**
	 * Writes the pending {@link DatagramPacket} to the underlying channel sending data to the
	 * intended receiver of the packet.
	 */
	@Override
	protected void doWrite(Object pendingWrite, SelectionKey key) throws IOException
	{
		DatagramPacket pendingPacket = (DatagramPacket) pendingWrite;
		getJavaChannel().send(pendingPacket.getData(), pendingPacket.getReceiver());
	}

	/**
	 * Writes the outgoing {@link DatagramPacket} to the channel. The intended receiver of the
	 * datagram packet must be set in the <code>data</code> using
	 * {@link DatagramPacket#setReceiver(SocketAddress)}.
	 */
	@Override
	public void write(Object data, SelectionKey key) {
		super.write(data, key);
	}

	public static class DatagramPacket
	{
		private SocketAddress sender;
		private ByteBuffer data;
		private SocketAddress receiver;

		/**
		 * Creates a container with underlying data.
		 *
		 * @param data the underlying message to be written on channel.
		 */
		public DatagramPacket(ByteBuffer data)
		{
			this.data = data;
		}

		public SocketAddress getSender()
		{
			return sender;
		}

		public void setSender(SocketAddress sender)
		{
			this.sender = sender;
		}

		public SocketAddress getReceiver()
		{
			return receiver;
		}

		public DatagramPacket setReceiver(SocketAddress receiver)
		{
			this.receiver = receiver;
			return this;
		}

		public ByteBuffer getData()
		{
			return data;
		}
	}
}
