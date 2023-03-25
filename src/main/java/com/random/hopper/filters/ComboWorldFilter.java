package com.random.hopper.filters;

import net.runelite.http.api.worlds.World;

import java.util.ArrayList;
import java.util.List;

// A class that can merge filters together such that all filters
// must pass for the world to pass.
public class ComboWorldFilter implements WorldFilter{
    List<WorldFilter> filters;

    public ComboWorldFilter(List<WorldFilter> filters) {
        this.filters = filters;
    }

    @Override
    public boolean isWorldAccepted(World world) {
        for(WorldFilter filter : filters) {
            if(!filter.isWorldAccepted(world)) {
                return false;
            }
        }
        return true;
    }
}
