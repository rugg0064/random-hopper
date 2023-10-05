package com.random.hopper.filters;


import net.runelite.http.api.worlds.World;
import net.runelite.http.api.worlds.WorldType;

import java.util.function.Predicate;

public class WorldFilterHelpers {
    public static final Predicate<World> isWorldPayToPlay =
        world -> world.getTypes().contains(WorldType.MEMBERS);

    public static final Predicate<World> isWorldPVP =
            world -> world.getTypes().contains(WorldType.PVP);

    public static final Predicate<World> isWorldHighRisk =
            world -> world.getTypes().contains(WorldType.HIGH_RISK);

    public static final Predicate<World> isWorldSkillTotal =
            world -> world.getTypes().contains(WorldType.SKILL_TOTAL);

    public static final Predicate<World> isWorldBounty =
            world -> world.getTypes().contains(WorldType.BOUNTY);

    public static final Predicate<World> isWorldDeadman =
            world -> world.getTypes().contains(WorldType.DEADMAN);

    public static final Predicate<World> isWorldSeasonal =
            world -> world.getTypes().contains(WorldType.SEASONAL);

    public static final Predicate<World> isWorldFreshStart =
            world -> world.getTypes().contains(WorldType.FRESH_START_WORLD);

    public static final Predicate<World> isWorldQuest =
            world -> world.getTypes().contains(WorldType.QUEST_SPEEDRUNNING);

    public static final Predicate<World> isWorldPVPArena =
            world -> world.getTypes().contains(WorldType.PVP_ARENA);

    public static final Predicate<World> isWorldBeta =
            world -> world.getTypes().contains(WorldType.BETA_WORLD);

	public static final Predicate<World> isWorldNoSave =
		world -> world.getTypes().contains(WorldType.NOSAVE_MODE);

    public static final Predicate<World> isWorldTournament =
            world -> world.getTypes().contains(WorldType.TOURNAMENT);

    public static final Predicate<World> isWorldNormal = world ->
            !isWorldDeadman.test(world) &&
            !isWorldSeasonal.test(world) &&
            !isWorldFreshStart.test(world) &&
            !isWorldQuest.test(world) &&
            !isWorldPVPArena.test(world) &&
            !isWorldBeta.test(world) &&
			!isWorldNoSave.test(world) &&
            !isWorldTournament.test(world);

}
