package com.random.hopper.filters;


import net.runelite.http.api.worlds.World;
import net.runelite.http.api.worlds.WorldRegion;

public class RegionWorldFilter implements WorldFilter {
    private boolean allowAustralia;
    private boolean allowGermany;
    private boolean allowUK;
    private boolean allowUSA;

    public RegionWorldFilter(boolean allowAustralia, boolean allowUSA, boolean allowGermany, boolean allowUK) {
        this.allowAustralia = allowAustralia;
        this.allowGermany = allowGermany;
        this.allowUK = allowUK;
        this.allowUSA = allowUSA;
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
                return allowUSA;
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
                ", allowUSA=" + allowUSA +
                '}';
    }
}
