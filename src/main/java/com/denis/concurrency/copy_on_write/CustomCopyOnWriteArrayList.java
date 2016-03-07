package com.denis.concurrency.copy_on_write;

import java.util.Arrays;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class CustomCopyOnWriteArrayList<T>
{
	private ReentrantLock lock = new ReentrantLock();
	private Object[] array = new Object[0];


	public static void main(String[] args)
	{
		CustomCopyOnWriteArrayList<String> a = new CustomCopyOnWriteArrayList<>();
//		System.out.println(a.array.length);

		a.add("0");
		a.add("1");
		a.add("2");

		a.add(0, "3");
		System.out.println(a);
		a.add(2, "4");
		System.out.println(a);
		a.add(2, "5");
		System.out.println(a);
		a.add(5, "6");
		System.out.println(a);

//		CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();
//		list.add("0");
//		list.add("1");
//		list.add("2");
//
//		list.add(0, "3");
//		System.out.println(list);
//		list.add(2, "4");
//		System.out.println(list);
//		list.add(2, "5");
//		System.out.println(list);
//		list.add(5, "6");
//		System.out.println(list);
	}

	void add(T item)
	{
		lock.lock();
		try
		{
			int index = array.length;
			Object[] newArray = Arrays.copyOf(array, index + 1);
//			System.out.println("newArray = " + Arrays.toString(newArray));
			newArray[index] = item;
			array = newArray;
		} finally
		{
			lock.unlock();
		}
	}

	void add(int index, T item)
	{
		lock.lock();
		try {

			int maxIndex = array.length;
			if(index > maxIndex || index < 0)
				throw new IllegalArgumentException("Index is greater than max size");

			if (index == 0)
			{
				Object[] newArray = new Object[maxIndex + 1];
				System.arraycopy(array, 0, newArray, 1, maxIndex);
//				System.out.println("newArray = " + Arrays.toString(newArray));
				newArray[index] = item;
				array = newArray;
			}
			else {
				Object[] newArray = new Object[maxIndex + 1];
				System.arraycopy(array, 0, newArray, 0, index);
//				System.out.println("newArray = " + Arrays.toString(newArray));
				System.arraycopy(array, index, newArray, index + 1, array.length - index);
//				System.out.println("newArray = " + Arrays.toString(newArray));
				newArray[index] = item;
				array = newArray;
			}
		} finally
		{
			lock.unlock();
		}
	}

	@Override
	public String toString()
	{
		final StringBuilder sb = new StringBuilder("CustomCopyOnWriteArrayList{");
		sb.append("array=").append(Arrays.toString(array));
		sb.append('}');
		return sb.toString();
	}
}
