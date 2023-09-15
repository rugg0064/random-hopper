package com.random.hopper.filters;

import net.runelite.http.api.worlds.World;

public interface WorldFilter {
    public boolean isWorldAccepted(World world);
}
