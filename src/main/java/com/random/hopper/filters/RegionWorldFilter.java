package com.random.hopper.filters;


import com.random.hopper.USWorld;
import com.random.hopper.WorldHelper;
import net.runelite.http.api.worlds.World;
import net.runelite.http.api.worlds.WorldRegion;

public class RegionWorldFilter implements WorldFilter {
    private boolean allowAustralia;
    private boolean allowGermany;
    private boolean allowUK;
    private boolean allowUSAEast;
	private boolean allowUSAWest;

    public RegionWorldFilter(boolean allowAustralia, boolean allowUSAEast, boolean allowUSAWest, boolean allowGermany, boolean allowUK) {
        this.allowAustralia = allowAustralia;
		this.allowUSAEast = allowUSAEast;
		this.allowUSAWest = allowUSAWest;
        this.allowGermany = allowGermany;
        this.allowUK = allowUK;
    }

    @Override
    public boolean isWorldAccepted(World world) {
        WorldRegion region = world.getRegion();
        switch(region){
            case AUSTRALIA:
                return allowAustralia;
            case GERMANY:
                return allowGermany;
            case UNITED_KINGDOM:
                return allowUK;
            case UNITED_STATES_OF_AMERICA:
				USWorld usWorld = WorldHelper.getUSWorldSide(world.getId());
				if(usWorld == null) {
					return false;
				}
				switch(usWorld) {
					case EAST:
						return allowUSAEast;
					case WEST:
						return allowUSAWest;
					default:
						return false;
				}
            default:
                return false;
        }
    }

    @Override
    public String toString() {
        return "RegionWorldFilter{" +
                "allowAustralia=" + allowAustralia +
                ", allowGermany=" + allowGermany +
                ", allowUK=" + allowUK +
                ", allowUSAEast=" + allowUSAEast +
				", allowUSAWest=" + allowUSAWest +
                '}';
    }
}
