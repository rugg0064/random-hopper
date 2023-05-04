package com.random.hopper;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Keybind;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

@ConfigGroup(RandomHopperConfig.GROUP)
public interface RandomHopperConfig extends Config {
    String GROUP = "randomhopper";

    @ConfigItem(
            keyName = "previousKey",
            name = "Hop previous",
            description = "Press this key to hop to the previous world in the cycle",
            position = 0
    )
    default Keybind previousKey()
    {
        return new Keybind(KeyEvent.VK_1, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK);
    }

    @ConfigItem(
            keyName = "randomKey",
            name = "Hop random",
            description = "Press this key to hop to a random world",
            position = 1
    )
    default Keybind randomKey()
    {
        return new Keybind(KeyEvent.VK_2, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK);
    }

    @ConfigItem(
            keyName = "nextKey",
            name = "Hop next",
            description = "Press this key to hop to the next world in the cycle",
            position = 2
    )
    default Keybind nextKey()
    {
        return new Keybind(KeyEvent.VK_3, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK);
    }
}
