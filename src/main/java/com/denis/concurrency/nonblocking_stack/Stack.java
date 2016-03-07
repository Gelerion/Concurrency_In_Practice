package com.denis.concurrency.nonblocking_stack;

import java.util.concurrent.atomic.AtomicReference;

public class Stack<T>
{
	AtomicReference<Node<T>> headRef = new AtomicReference<>();

	public void push(T value)
	{
		Node<T> newNode = new Node<>();
		newNode.value = value;
		Node<T> oldNode;
		do
		{
			oldNode = headRef.get();
			newNode.next = oldNode;
		}
		while (!headRef.compareAndSet(oldNode, newNode));
	}

	public T pop()
	{
		Node<T> previousNode;
		Node<T> currentNode;

		do
		{
			previousNode = headRef.get();
			if (previousNode == null)
			{
				return null;
			}

			currentNode = previousNode.next;
		}
		while (!headRef.compareAndSet(previousNode, currentNode));

		return previousNode.value;
	}

	public static class Node<T> {
		private T value;
		private Node<T> next;
	}
}
