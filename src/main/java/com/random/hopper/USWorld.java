package com.random.hopper;

public enum USWorld
{
	EAST(-42),
	WEST(-73);

	private final int enumValue;

	USWorld(int enumValue) {
		this.enumValue = enumValue;
	}

	public int getEnumValue() {
		return enumValue;
	}
}
