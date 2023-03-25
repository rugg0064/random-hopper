package com.random.hopper.filters;

import net.runelite.http.api.worlds.World;

public class SkillTotalWorldFilter implements WorldFilter{
    private int maximumLevel;

    public SkillTotalWorldFilter(int maximumLevel) {
        this.maximumLevel = maximumLevel;
    }

    @Override
    public boolean isWorldAccepted(World world) {
        if(!WorldFilterHelpers.isWorldSkillTotal.test(world))
        { // Accept all non-skill total worlds
            return true;
        }
        else
        {
            String maxLevelString = world.getActivity().split(" ")[0];
            return maximumLevel >= Integer.parseInt(maxLevelString);
        }
    }
}
