package com.random.hopper;

import com.random.hopper.filters.*;
import net.runelite.client.ui.DynamicGridLayout;
import net.runelite.client.ui.PluginPanel;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class RandomHopperPanel extends PluginPanel  {
    private RandomHopperPlugin plugin;

    private JComboBox subscriptionDropdown;
    private JComboBox pvpDropdown;
    private JComboBox highRiskDropdown;
    private JComboBox skillTotalDropdown;
    private JComboBox targetWorldDropdown;

    private JCheckBox usaEastBox;
	private JCheckBox usaWestBox;
    private JCheckBox ukBox;
    private JCheckBox gerBox;
    private JCheckBox ausBox;

    private JLabel worldCountLabel;

    private JRadioButton normalButton;
    private JRadioButton deadmanButton;
    private JRadioButton seasonalButton;
    private JRadioButton questButton;
    private JRadioButton freshButton;
    private JRadioButton pvpArenaButton;
    private JRadioButton betaButton;
    private JRadioButton tournamentButton;

    private JTextField seedTextField;

    private JLabel prevLabel;
    private JLabel currLabel;
    private JLabel nextLabel;

    RandomHopperPanel(RandomHopperPlugin plugin)
    {
        this.plugin = plugin;

        setBorder(null);
        setLayout(new DynamicGridLayout(0, 1));

        JPanel panel = createPanel();

        add(panel);
    }

    Dimension preferredSize = new Dimension(100, 16);
    //private static final String[] comboBoxText = new String[]{"Require", "Reject", "Don't filter"};
    private static final TrinaryWorldFilterParameters[] comboBoxOptions =
        new TrinaryWorldFilterParameters[] {
            new TrinaryWorldFilterParameters( "Always", true, false),
            new TrinaryWorldFilterParameters( "Never", false, true),
            new TrinaryWorldFilterParameters( "Sometimes", true, true),
    };

    private JPanel createLabel(String text) {
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.X_AXIS));
        textPanel.setPreferredSize(preferredSize);
        textPanel.add(new JLabel(text));
        return textPanel;
    }

    public JPanel createPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JPanel subscriptionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        subscriptionPanel.add(createLabel("Subscription: "));
        subscriptionDropdown = new JComboBox<>(new TrinaryWorldFilterParameters[] {
            new TrinaryWorldFilterParameters( "P2P", true, false),
            new TrinaryWorldFilterParameters( "F2P", false, true),
            new TrinaryWorldFilterParameters( "Either", true, true),
        });
        subscriptionDropdown.setSelectedIndex(0);
        subscriptionPanel.add(subscriptionDropdown);
        panel.add(subscriptionPanel);

        JPanel pvpPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pvpPanel.add(createLabel("PVP: "));
        pvpDropdown = new JComboBox<>(comboBoxOptions);
        pvpDropdown.setSelectedIndex(1);
        pvpPanel.add(pvpDropdown);
        panel.add(pvpPanel);

        JPanel highRiskPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        highRiskPanel.add(createLabel("High Risk: "));
        highRiskDropdown = new JComboBox<>(comboBoxOptions);
        highRiskDropdown.setSelectedIndex(1);
        highRiskPanel.add(highRiskDropdown);
        panel.add(highRiskPanel);

        // Add the "Target World" dropdown
        JPanel skillTotalPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        skillTotalPanel.add(createLabel("Skill total: "));
        skillTotalDropdown = new JComboBox<>(comboBoxOptions);
        skillTotalDropdown.setSelectedIndex(2);
        skillTotalPanel.add(skillTotalDropdown);
        panel.add(skillTotalPanel);

        // Add the "Target World" dropdown
        JPanel targetWorldPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        targetWorldPanel.add(createLabel("Target World: "));
        targetWorldDropdown = new JComboBox<>(comboBoxOptions);
        targetWorldDropdown.setSelectedIndex(1);
        targetWorldPanel.add(targetWorldDropdown);
        panel.add(targetWorldPanel);

        JPanel groupCodePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        seedTextField = new JTextField(8);
        seedTextField.setToolTipText(seedToolTipText);
        groupCodePanel.add(createLabel("Seed: "));
        groupCodePanel.add(seedTextField);
        panel.add(groupCodePanel);

        // Add a checkbox selection titled "Region" with values "Australia", "Germany", "U.K.", "U.S.A"
        JPanel regionPanel = new JPanel();
        regionPanel.setBorder(BorderFactory.createTitledBorder("Region"));
        regionPanel.setLayout(new GridLayout(0, 1));
		usaEastBox = new JCheckBox("U.S.A East");
		usaWestBox = new JCheckBox("U.S.A West");
        ukBox = new JCheckBox("U.K.");
        gerBox = new JCheckBox("Germany");
        ausBox = new JCheckBox("Australia");
        for(JCheckBox box : new JCheckBox[]{usaEastBox, usaWestBox, ukBox, gerBox, ausBox}){
            regionPanel.add(box);
            box.setSelected(true);
        }
        panel.add(regionPanel);

        // World type
        JPanel typePanel = new JPanel();
        typePanel.setBorder(BorderFactory.createTitledBorder("World Type"));
        typePanel.setLayout(new GridLayout(0, 1));

        ButtonGroup typeGroup = new ButtonGroup();

        normalButton = new JRadioButton("Normal");
        deadmanButton = new JRadioButton("Deadman");
        seasonalButton = new JRadioButton("Seasonal");
        questButton = new JRadioButton("Quest Speedrun");
        freshButton = new JRadioButton("Fresh Start");
        pvpArenaButton = new JRadioButton("PVP Arena");
        betaButton = new JRadioButton("Beta");
        tournamentButton = new JRadioButton("Tournament");

        normalButton.setSelected(true);

        typeGroup.add(normalButton);
        typeGroup.add(deadmanButton);
        typeGroup.add(seasonalButton);
        typeGroup.add(questButton);
        typeGroup.add(freshButton);
        typeGroup.add(pvpArenaButton);
        typeGroup.add(betaButton);
        typeGroup.add(tournamentButton);

        typePanel.add(normalButton);
        typePanel.add(deadmanButton);
        typePanel.add(seasonalButton);
        typePanel.add(questButton);
        typePanel.add(freshButton);
        typePanel.add(pvpArenaButton);
        typePanel.add(betaButton);
        typePanel.add(tournamentButton);

        panel.add(typePanel);

        JPanel wordCountPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        worldCountLabel = new JLabel("");
        worldCountLabel.setToolTipText(worldCountToolTipText);
        wordCountPanel.add(worldCountLabel);
        panel.add(wordCountPanel);

        // Add a "Random" button to the panel
        JPanel randomButtonWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton randomButton = new JButton("Random Hop");
        randomButton.setToolTipText(randomHopToolTipText);
        randomButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                plugin.doRandomHop();
            }
        });
        randomButtonWrapper.add(randomButton);
        panel.add(randomButtonWrapper);

        // Add a row with "Previous" and "Next" buttons to the panel
        JPanel prevNextPanel = new JPanel();
        Dimension buttonPreferredSize = new Dimension(110, 32);
        JButton prevButton = new JButton("Hop Previous");
        prevButton.setPreferredSize(buttonPreferredSize);
        prevButton.setToolTipText(hopPreviousToolTipText);

        JButton nextButton = new JButton("Hop Next");
        nextButton.setPreferredSize(buttonPreferredSize);
        nextButton.setToolTipText(hopNextToolTipText);
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                plugin.hopNext();
            }
        });
        prevButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                plugin.hopPrevious();
            }
        });
        prevNextPanel.add(prevButton);
        prevNextPanel.add(nextButton);
        panel.add(prevNextPanel);

        JPanel worldPanel = new JPanel();
        worldPanel.setLayout(new GridLayout(1, 3, 10, 0));

        prevLabel = new JLabel();
        prevLabel.setToolTipText(previousWorldToolTipText);
        prevLabel.setHorizontalAlignment(SwingConstants.CENTER);
        worldPanel.add(prevLabel);

        currLabel = new JLabel();
        currLabel.setToolTipText(currentWorldToolTipText);
        currLabel.setHorizontalAlignment(SwingConstants.CENTER);
        worldPanel.add(currLabel);

        nextLabel = new JLabel();
        nextLabel.setToolTipText(nextWorldToolTipText);
        nextLabel.setHorizontalAlignment(SwingConstants.CENTER);

        worldPanel.add(nextLabel);
        updateAdjacentWorlds();
        panel.add(worldPanel);

        ActionListener updateWorldsListener = e -> {
            updateWorldCountLabel();
            updateAdjacentWorlds();
        };

        for(JComboBox component : new JComboBox[] {subscriptionDropdown, pvpDropdown, highRiskDropdown, skillTotalDropdown, targetWorldDropdown}) {
            component.addActionListener(updateWorldsListener);
        }
        for(JRadioButton component : new JRadioButton[]{normalButton, deadmanButton, seasonalButton, questButton, freshButton, pvpArenaButton, betaButton, tournamentButton}) {
            component.addActionListener(updateWorldsListener);
        }
        for(JCheckBox component : new JCheckBox[] {gerBox, usaEastBox, usaWestBox, ausBox, ukBox}) {
            component.addActionListener(updateWorldsListener);
        }

        seedTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                plugin.newCycle();
                updateAdjacentWorlds();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                plugin.newCycle();
                updateAdjacentWorlds();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                plugin.newCycle();
                updateAdjacentWorlds();
            }
        });

        return panel;
    }

    public void updateAdjacentWorlds() {
        Integer[] worlds = plugin.getAdjacentWorlds();
        prevLabel.setText(worlds[0] == null ? "?" : String.format("W%s", worlds[0]));
        currLabel.setText(worlds[1] == null ? "?" : String.format("W%s", worlds[1]));
        nextLabel.setText(worlds[2] == null ? "?" : String.format("W%s", worlds[2]));
    }


    public void updateWorldCountLabel() {
        plugin.newCycle();
        setWorldCountLabel(plugin.getWorldCount());
    }

    private void setWorldCountLabel(int count) {
        worldCountLabel.setText(String.format("%d Worlds", count));
    }

    List<WorldFilter> getFilters() {
        List<WorldFilter> filters = new ArrayList<WorldFilter>();

        // subscription filter;
        filters.add(new TrinaryWorldFilter(
                WorldFilterHelpers.isWorldPayToPlay,
                (TrinaryWorldFilterParameters) subscriptionDropdown.getSelectedItem()));

        // pvp filter;
        filters.add(new TrinaryWorldFilter(
                WorldFilterHelpers.isWorldPVP,
                (TrinaryWorldFilterParameters) pvpDropdown.getSelectedItem()));

        // high risk filter;
        filters.add(new TrinaryWorldFilter(
                WorldFilterHelpers.isWorldHighRisk,
                (TrinaryWorldFilterParameters) highRiskDropdown.getSelectedItem()));

        // skill total filter;
        filters.add(new TrinaryWorldFilter(
                WorldFilterHelpers.isWorldSkillTotal,
                (TrinaryWorldFilterParameters) skillTotalDropdown.getSelectedItem()));

        // target world filter;
        filters.add(new TrinaryWorldFilter(
                WorldFilterHelpers.isWorldTarget,
                (TrinaryWorldFilterParameters) targetWorldDropdown.getSelectedItem()));

        filters.add(new RegionWorldFilter(
                ausBox.isSelected(),
				usaEastBox.isSelected(),
				usaWestBox.isSelected(),
                gerBox.isSelected(),
                ukBox.isSelected()));

        if(normalButton.isSelected()) {
            filters.add(new TrinaryWorldFilter(WorldFilterHelpers.isWorldNormal, new TrinaryWorldFilterParameters("normal", true, false)));
        }
        if(deadmanButton.isSelected()) {
            filters.add(new TrinaryWorldFilter(WorldFilterHelpers.isWorldDeadman, new TrinaryWorldFilterParameters("deadman", true, false)));
        }
        if(seasonalButton.isSelected()) {
            filters.add(new TrinaryWorldFilter(WorldFilterHelpers.isWorldSeasonal, new TrinaryWorldFilterParameters("seasonal", true, false)));
        }
        if(questButton.isSelected()) {
            filters.add(new TrinaryWorldFilter(WorldFilterHelpers.isWorldQuest, new TrinaryWorldFilterParameters("quest", true, false)));
        }
        if(freshButton.isSelected()) {
            filters.add(new TrinaryWorldFilter(WorldFilterHelpers.isWorldFreshStart, new TrinaryWorldFilterParameters("fresh", true, false)));
        }
        if(pvpArenaButton.isSelected()) {
            filters.add(new TrinaryWorldFilter(WorldFilterHelpers.isWorldPVPArena, new TrinaryWorldFilterParameters("pvpArena", true, false)));
        }
        if(betaButton.isSelected()) {
            filters.add(new TrinaryWorldFilter(WorldFilterHelpers.isWorldBeta, new TrinaryWorldFilterParameters("beta", true, false)));
        }
        if(tournamentButton.isSelected()) {
            filters.add(new TrinaryWorldFilter(WorldFilterHelpers.isWorldTournament, new TrinaryWorldFilterParameters("tournament", true, false)));
        }
        return filters;
    }

    public int getSeed() {
        if(seedTextField.getText().isEmpty()) {
            return new Random().nextInt();
        } else {
            return seedTextField.getText().hashCode();
        }
    }

    private static final String worldCountToolTipText = "How many worlds passed through the filters.";
    private static final String randomHopToolTipText = "Hops to a random world.";
    private static final String hopPreviousToolTipText = "Hops to the previous world in the cycle, or a random world if you aren't in the cycle yet.";
    private static final String hopNextToolTipText = "Hops to the next world in the cycle, or a random world if you aren't in the cycle yet.";
    private static final String seedToolTipText = "If multiple people have the same seed and all the same configuration, \"Hop Next\" will always follow the same worlds.";

    private static final String previousWorldToolTipText = "Previous world";
    private static final String currentWorldToolTipText = "Current world";
    private static final String nextWorldToolTipText = "Next world";

}
