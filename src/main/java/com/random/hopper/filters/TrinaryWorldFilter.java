package com.random.hopper.filters;

import net.runelite.http.api.worlds.World;

import java.util.function.Predicate;

// Assumes that the world being compared can always BE filtered
public class TrinaryWorldFilter implements WorldFilter{
    Predicate<World> matcher;
    TrinaryWorldFilterParameters parameters;

    public TrinaryWorldFilter(Predicate<World> matcher, TrinaryWorldFilterParameters parameters) {
        this.matcher = matcher;
        this.parameters = parameters;
    }

    @Override
    public boolean isWorldAccepted(World world) {
        return matcher.test(world) ? parameters.getAllowTrue() : parameters.getAllowFalse();
    }

    @Override
    public String toString() {
        return String.format("matcher: %s | %s", matcher, parameters.toStringDebug());
    }
}
