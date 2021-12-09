package dev.tommyothen.plugins.automilk;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.material.DirectionalContainer;
import org.bukkit.material.Dispenser;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Automilk extends JavaPlugin implements Listener {
    private static final Logger LOGGER = Logger.getLogger(Automilk.class.getName());

    @Override
    public void onEnable() {
        // Plugin startup logic
        LOGGER.log(Level.INFO, "Automilk is enabled!");

        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        LOGGER.log(Level.INFO, "Automilk is disabled!");
    }

    @EventHandler
    public void onBlockDispenseEvent(BlockDispenseEvent event) {
        // Get event as a block
        Block block = event.getBlock();

        // Get the dispenser that was just fired
        InventoryHolder dispenserIHolder = (InventoryHolder) block.getState();

        // Get the inventory of the dispenser
        Inventory dispenserInventory = dispenserIHolder.getInventory();

        // Get the item that was just dispensed
        ItemStack dispensedItem = event.getItem();

        // Get the name of the dispensed item
        String dispensedItemName = dispensedItem.getType().toString();

        // Return if the item is not an empty bucket
        if (!dispensedItemName.equals("BUCKET"))
            return;

        // Get the dispenser face direction
        DirectionalContainer dispenser = (DirectionalContainer) block.getState().getData();
        BlockFace dispenserFace = dispenser.getFacing();

        // Get the block that the dispenser is facing
        Block dispenserFacingBlock = block.getRelative(dispenserFace);
        Location dispenserFacingBlockLocation = dispenserFacingBlock.getLocation();

        // Get nearby entities within a block
        Collection<Entity> nearbyEntities = block.getWorld().getNearbyEntities(dispenserFacingBlockLocation.toCenterLocation(), 0.5, 0.5, 0.5);

        // Return if there are no nearby entities
        if (nearbyEntities.isEmpty())
            return;

        // Loop through all nearby entities
        for (Entity entity : nearbyEntities) {
            // Get the type of the entity
            String entityType = entity.getType().toString();

            // If the entity is a cow
            if (entityType.equals("COW")) {
                LOGGER.log(Level.INFO, "Cow found!");

                // Remove the empty bucket from the dispenser inventory
                dispenserInventory.removeItemAnySlot(dispensedItem);

                // Set the event item to a milk bucket
                event.setItem(new ItemStack(Material.MILK_BUCKET));
                return;
            }
        }
    }
}
