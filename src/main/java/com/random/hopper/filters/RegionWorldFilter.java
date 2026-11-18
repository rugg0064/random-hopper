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
    private boolean allowBrazil;
    private boolean allowJapan;
    private boolean allowSingapore;
    private boolean allowSouthAfrica;

    public RegionWorldFilter(boolean allowAustralia, boolean allowUSAEast, boolean allowUSAWest, boolean allowGermany, boolean allowUK, boolean allowBrazil, boolean allowJapan, boolean allowSingapore, boolean allowSouthAfrica) {
        this.allowAustralia = allowAustralia;
		this.allowUSAEast = allowUSAEast;
		this.allowUSAWest = allowUSAWest;
        this.allowGermany = allowGermany;
        this.allowUK = allowUK;
        this.allowBrazil = allowBrazil;
        this.allowJapan = allowJapan;
        this.allowSingapore = allowSingapore;
        this.allowSouthAfrica = allowSouthAfrica;
    }

    @Override
    public boolean isWorldAccepted(World world) {
        WorldRegion region = world.getRegion();
        // getRegion() is null for an unknown location id, and switching on null throws
        if(region == null) {
            return false;
        }
        switch(region){
            case AUSTRALIA:
                return allowAustralia;
            case GERMANY:
                return allowGermany;
            case UNITED_KINGDOM:
                return allowUK;
            case BRAZIL:
                return allowBrazil;
            case JAPAN:
                return allowJapan;
            case SINGAPORE:
                return allowSingapore;
            case SOUTH_AFRICA:
                return allowSouthAfrica;
            case UNITED_STATES_OF_AMERICA:
				USWorld usWorld = WorldHelper.getUSWorldSide(world.getId());
				if(usWorld == null) {
					// East/West is unknown (world enum not loaded yet, or world missing from it),
					// so keep the world if either US side is allowed
					return allowUSAEast || allowUSAWest;
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
                ", allowBrazil=" + allowBrazil +
                ", allowJapan=" + allowJapan +
                ", allowSingapore=" + allowSingapore +
                ", allowSouthAfrica=" + allowSouthAfrica +
                '}';
    }
}
