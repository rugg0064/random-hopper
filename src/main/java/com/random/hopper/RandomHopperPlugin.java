package com.random.hopper;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import javax.inject.Inject;

import com.google.inject.Provides;
import com.random.hopper.filters.*;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.WorldChanged;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.WorldService;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.util.HotkeyListener;
import net.runelite.client.util.ImageUtil;
import net.runelite.client.util.WorldUtil;
import net.runelite.http.api.worlds.World;
import net.runelite.http.api.worlds.WorldRegion;
import net.runelite.http.api.worlds.WorldResult;

import java.awt.image.BufferedImage;
import java.util.*;
import net.runelite.http.api.worlds.WorldType;

@Slf4j
@PluginDescriptor(
	name = "Random Hopper",
	description = "",
	tags = {"World Hop"}
)
public class RandomHopperPlugin extends Plugin
{
	@Inject private Client client;
	@Inject private ClientToolbar clientToolbar;
	@Inject private WorldService worldService;
	@Inject private ClientThread clientThread;
	@Inject private ConfigManager configManager;
	@Inject private RandomHopperConfig config;
	@Inject private KeyManager keyManager;

	private NavigationButton navButton;
	private RandomHopperPanel panel;

	private World targetWorld;

	// A map from current world to next world
	private BiMap<Integer, Integer> cycleMapping;
	private LinkedList<Integer> worldQueue;

	private boolean shouldRecalculateWorldsOnTick = false;

	@Override
	protected void startUp()
	{
		System.out.println("AAAAA");
		clientThread.invoke(() -> {
			WorldHelper.setWorldEnum(client.getEnum(4992));
			shouldRecalculateWorldsOnTick = true;
		});
		keyManager.registerKeyListener(randomKeyListener);
		keyManager.registerKeyListener(previousKeyListener);
		keyManager.registerKeyListener(nextKeyListener);
		targetWorld = null;
		BufferedImage icon = ImageUtil.loadImageResource(getClass(), "/randomhopper_icon.png");
		panel = new RandomHopperPanel(this);
		navButton = NavigationButton.builder()
				.tooltip("Random Hopper")
				.icon(icon)
				.priority(5)
				.panel(panel)
				.build();
		panel.updateWorldCountLabel();
		worldQueue = new LinkedList<>();
		clientToolbar.addNavigation(navButton);
	}

	@Override
	protected void shutDown() throws Exception
	{
		clientToolbar.removeNavigation(navButton);
		keyManager.unregisterKeyListener(randomKeyListener);
		keyManager.unregisterKeyListener(previousKeyListener);
		keyManager.unregisterKeyListener(nextKeyListener);
	}

	@Provides
	RandomHopperConfig getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(RandomHopperConfig.class);
	}

	@Subscribe
	public void onGameTick(GameTick tick)
	{
		if (targetWorld == null)
		{
			return;
		}
		if(shouldRecalculateWorldsOnTick) {
			newCycle();
			panel.updateAdjacentWorlds();
			shouldRecalculateWorldsOnTick = false;
		}
		if(isWorldHopperOpen()) {
			//System.out.println("Trying to client hopworld");
			client.hopToWorld(convertToApiWorld(targetWorld));
			targetWorld = null;
		} else {
			client.openWorldHopper();
		}
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		if(gameStateChanged.getGameState() == GameState.LOGGED_IN) {
			//System.out.printf("Hopped to %s\n", client.getWorld());
			worldQueue.addFirst(client.getWorld());
			panel.updateAdjacentWorlds();
		}
	}

	@Subscribe
	public void onWorldChanged(WorldChanged worldChanged) {
		panel.updateAdjacentWorlds();
	}

	boolean isWorldHopperOpen()
	{
		return client.getWidget(WidgetInfo.WORLD_SWITCHER_LIST) != null;
	}

	private void hop(World world)
	{
		assert client.isClientThread();

		final net.runelite.api.World rsWorld = convertToApiWorld(world);

		if (client.getGameState() == GameState.LOGIN_SCREEN)
		{ // on the login screen we can just change the world by ourselves
			client.changeWorld(rsWorld);
		}
		else if(client.getGameState() != GameState.HOPPING)
		{ // Clicking the hop button mid-hop will queue another hop immediately without this.
			targetWorld = world;
		}
		panel.updateAdjacentWorlds();
	}

	private net.runelite.api.World convertToApiWorld(net.runelite.http.api.worlds.World world) {
		final net.runelite.api.World rsWorld = client.createWorld();
		rsWorld.setActivity(world.getActivity());
		rsWorld.setAddress(world.getAddress());
		rsWorld.setId(world.getId());
		rsWorld.setPlayerCount(world.getPlayers());
		rsWorld.setLocation(world.getLocation());
		rsWorld.setTypes(WorldUtil.toWorldTypes(world.getTypes()));
		return rsWorld;
	}

	ArrayList<World> getValidWorlds(WorldFilter filter) {
		List<World> worlds = worldService.getWorlds().getWorlds();
		ArrayList<World> validWorlds = new ArrayList<>();
		for(World world : worlds) {
			EnumSet types = world.getTypes();
			System.out.println(world);
			if(filter.isWorldAccepted(world)) {
				validWorlds.add(world);
			}
		}
		return validWorlds;
	}

	List<World> filterWorlds(List<World> worlds, WorldFilter filter) {
		List<World> newWorlds = new ArrayList<>();
		for(World world : worlds) {
			if(filter.isWorldAccepted(world)) {
				newWorlds.add(world);
			}
		}
		return newWorlds;
	}

	// Cycle must exist
	private World getRandomWorldFromCycle() {
		WorldResult worldResult = worldService.getWorlds();
		Random r = new Random();
		int nextWorldIndex = r.nextInt(cycleMapping.keySet().size());
		int i = 0;
		int nextWorldID = -1; // This will never be -1
		for(int worldID : cycleMapping.keySet()) {
			if(i == nextWorldIndex) {
				nextWorldID = worldID;
				break;
			}
			i++;
		}
		return worldResult.findWorld(nextWorldID);
	}

	void hopPrevious() {
		if(cycleMapping == null) {
			return;
		}
		int currentWorld = client.getWorld();
		World nextWorld;
		WorldResult worldResult = worldService.getWorlds();
		if(!cycleMapping.containsKey(currentWorld)) {
			nextWorld = getRandomWorldFromCycle();
		}
		else
		{
			int nextWorldID = cycleMapping.inverse().get(currentWorld);
			nextWorld = worldResult.findWorld(nextWorldID);
		}

		clientThread.invoke(() -> hop(nextWorld));
	}

	void hopNext() {
		if(cycleMapping == null) {
			return;
		}
		int currentWorld = client.getWorld();
		World nextWorld;
		WorldResult worldResult = worldService.getWorlds();
		if(!cycleMapping.containsKey(currentWorld)) {
			nextWorld = getRandomWorldFromCycle();
		}
		else
		{
			int nextWorldID = cycleMapping.get(currentWorld);
			nextWorld = worldResult.findWorld(nextWorldID);
		}

		clientThread.invoke(() -> hop(nextWorld));
	}

	void newCycle() {
		List<WorldFilter> panelFilters = panel.getFilters();
		panelFilters.add(getSkillTotalFilter());
		WorldFilter comboFilter = new ComboWorldFilter(panelFilters);

		/**
		 * getWorlds() does not return a sorted list, must manually sort to ensure
		 * that the shuffle is consistent given a seed.
 		 */
		List<World> allWorlds = worldService.getWorlds().getWorlds();
		Collections.sort(allWorlds, Comparator.comparingInt(World::getId));
		Collections.shuffle(allWorlds, new Random(panel.getSeed()));
		List<World> validWorlds = filterWorlds(allWorlds, comboFilter);
		for(World world : validWorlds) {
			System.out.println(world);
		}

		ArrayList<Integer> validWorldIDs = new ArrayList<>();
		for(World world : validWorlds) {
			validWorldIDs.add(world.getId());
		}

		if(validWorlds.size() == 0) {
			cycleMapping = null;
			return;
		}

		cycleMapping = HashBiMap.create();
		for(int i = 0; i < validWorlds.size() - 1; i++) {
			cycleMapping.put(validWorldIDs.get(i), validWorldIDs.get(i + 1));
		}
		cycleMapping.put(validWorldIDs.get(validWorldIDs.size() - 1), validWorldIDs.get(0));
	}

	public int getWorldCount() {
		System.out.println(cycleMapping);
		if(cycleMapping == null) {
			return 0;
		} else {
			return cycleMapping.size();
		}
	}

	void doRandomHop()
	{
		List<WorldFilter> filters = panel.getFilters();
		filters.add(new BlockIDWorldFilter(client.getWorld()));
		filters.add(getSkillTotalFilter());
		WorldFilter comboFilter = new ComboWorldFilter(filters);
		ArrayList<World> validWorlds = getValidWorlds(comboFilter);
		Random r = new Random();
		if(validWorlds.size() == 0) {
			//System.out.printf("No valid worlds, not hopping");
			return;
		}
		int chosenIndex = r.nextInt(validWorlds.size());
		World chosenWorld = validWorlds.get(chosenIndex);
		//System.out.printf("Selecting world %d%n", chosenWorld.getId());
		clientThread.invoke(() -> hop(chosenWorld));
	}

	WorldFilter getSkillTotalFilter() {
		return new SkillTotalWorldFilter(client.getTotalLevel());
	}

	public Integer[] getAdjacentWorlds() {
		int currentWorldID = client.getWorld();
		if(cycleMapping == null || !cycleMapping.containsKey(currentWorldID))
		{
			// If there is only one available world though, then it's always that one.
			if(cycleMapping != null && cycleMapping.size() == 1) {
				int onlyWorld = cycleMapping.values().iterator().next();
				return new Integer[] {onlyWorld, currentWorldID, onlyWorld};
			}
			// Not in the cycle currently - next would be random
			return new Integer[] {null, currentWorldID, null};
		}
		else
		{
			int nextWorldID = cycleMapping.get(currentWorldID);
			int prevWorldID = cycleMapping.inverse().get(currentWorldID);
			return new Integer[] {prevWorldID, currentWorldID, nextWorldID};
		}
	}

	private final HotkeyListener randomKeyListener = new HotkeyListener(() -> config.randomKey())
	{
		@Override
		public void hotkeyPressed()
		{
			clientThread.invoke(() -> doRandomHop());
		}
	};
	private final HotkeyListener previousKeyListener = new HotkeyListener(() -> config.previousKey())
	{
		@Override
		public void hotkeyPressed()
		{
			clientThread.invoke(() -> hopPrevious());
		}
	};
	private final HotkeyListener nextKeyListener = new HotkeyListener(() -> config.nextKey())
	{
		@Override
		public void hotkeyPressed()
		{
			clientThread.invoke(() -> hopNext());
		}
	};
}
