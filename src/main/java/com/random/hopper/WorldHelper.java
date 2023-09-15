package com.random.hopper;

import java.util.Arrays;
import net.runelite.api.EnumComposition;

public class WorldHelper
{
	private static EnumComposition worldEnum = null;
	// Sets the enum that holds the US East vs West map
	public static void setWorldEnum(EnumComposition worldEnum) {
		WorldHelper.worldEnum = worldEnum;
	}

	public static USWorld getUSWorldSide(int worldNumber) {
		boolean worldInKeys = Arrays.stream(worldEnum.getKeys()).anyMatch((key) -> key == worldNumber);
		if (worldInKeys)
		{
			int worldEnumValue = worldEnum.getIntValue(worldNumber);
			if(worldEnumValue == USWorld.WEST.getEnumValue())
			{
				return USWorld.WEST;
			}
			else if(worldEnumValue == USWorld.EAST.getEnumValue()) {
				return USWorld.EAST;
			}
			else {
				return null;
			}
		}
		else {
			return null;
		}
	}
}
