package com.random.hopper.filters;

import net.runelite.http.api.worlds.World;

import java.util.function.Predicate;

public interface WorldFilter {
    public boolean isWorldAccepted(World world);
}
