package com.random.hopper.filters;

import net.runelite.http.api.worlds.World;

public class BlockIDWorldFilter implements WorldFilter{
    private int blockID;

    public BlockIDWorldFilter(int id) {
        blockID = id;
    }

    @Override
    public boolean isWorldAccepted(World world) {
        return world.getId() != blockID;
    }
}
