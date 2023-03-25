package com.random.hopper;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class randomHopperTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(RandomHopperPlugin.class);
		RuneLite.main(args);
	}
}