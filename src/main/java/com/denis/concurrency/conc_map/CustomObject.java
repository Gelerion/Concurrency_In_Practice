package com.denis.concurrency.conc_map;

public class CustomObject
{
	Integer value;

	public Integer getValue()
	{
		return value;
	}

	public CustomObject setValue(Integer value)
	{
		this.value = value;
		return this;
	}

	@Override
	public String toString()
	{
		final StringBuilder sb = new StringBuilder("CustomObject{");
		sb.append("value=").append(value);
		sb.append('}');
		return sb.toString();
	}
}
