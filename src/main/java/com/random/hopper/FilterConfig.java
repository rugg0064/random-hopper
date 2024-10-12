package com.random.hopper;

public class FilterConfig {
    public int subscriptionIndex;
    public int pvpIndex;
    public int highRiskIndex;
    public int skillTotalIndex;
    public int bountyWorldIndex;
    
    public String seed;
    
    public boolean usaEastSelected;
    public boolean usaWestSelected;
    public boolean ukSelected;
    public boolean germanySelected;
    public boolean australiaSelected;

    public int worldTypeIndex;

    public FilterConfig(
            int subscriptionIndex,
            int pvpIndex,
            int highRiskIndex,
            int skillTotalIndex,
            int bountyWorldIndex,
            String seed,
            boolean usaEastSelected,
            boolean usaWestSelected,
            boolean ukSelected,
            boolean germanySelected,
            boolean australiaSelected,
            int worldTypeIndex) {
        this.subscriptionIndex = subscriptionIndex;
        this.pvpIndex = pvpIndex;
        this.highRiskIndex = highRiskIndex;
        this.skillTotalIndex = skillTotalIndex;
        this.bountyWorldIndex = bountyWorldIndex;
        this.seed = seed;
        this.usaEastSelected = usaEastSelected;
        this.usaWestSelected = usaWestSelected;
        this.ukSelected = ukSelected;
        this.germanySelected = germanySelected;
        this.australiaSelected = australiaSelected;
        this.worldTypeIndex = worldTypeIndex;
    }
}
