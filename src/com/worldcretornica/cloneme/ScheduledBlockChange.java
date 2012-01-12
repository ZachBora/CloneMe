package com.worldcretornica.cloneme;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockPlaceEvent;

import com.worldcretornica.cloneme.events.CloneBlockBreakEvent;
import com.worldcretornica.cloneme.events.CloneBlockPlaceEvent;

public class ScheduledBlockChange implements Runnable {

    public enum ChangeType {
	BLOCK_BREAK, BLOCK_PLACE, INTERACT
    }

    private Clone clone;
    private CloneMe plugin;
    private Block block;
    private Player player;
    private ChangeType change;
    private Event event;

    public ScheduledBlockChange(Clone clone, CloneMe plugin, Player player,
	    Block block, ChangeType changes, Event event) {
	this.clone = clone;
	this.plugin = plugin;
	this.player = player;
	this.block = block;
	this.change = changes;
	this.event = event;
    }

    @Override
    public void run() {

	// plugin.logger.info("CloneMe PlayerData = " +
	// e.getClickedBlock().getData());

	Block newblock = clone.getNewBlockLocation(block);

	switch (change) {
	case INTERACT:

	    if (newblock != null && newblock.getType() != Material.AIR) {
		net.minecraft.server.World myworld = ((CraftWorld) block
			.getWorld()).getHandle();
		net.minecraft.server.EntityHuman human = ((CraftPlayer) player)
			.getHandle();

		if (myworld != null && human != null) {
		    net.minecraft.server.Block.byId[newblock.getTypeId()]
			    .interact(myworld, newblock.getX(),
				    newblock.getY(), newblock.getZ(), human);
		}
	    }
	    break;
	case BLOCK_BREAK:

	    CloneBlockBreakEvent breakevent = new CloneBlockBreakEvent(
		    newblock, player);

	    plugin.getServer().getPluginManager().callEvent(breakevent);

	    if (breakevent.isCancelled() || !clone.breakBlock(block, player)) {
		player.sendMessage(ChatColor.RED
			+ "Clone could not break block");
	    }

	    break;
	case BLOCK_PLACE:

	    BlockPlaceEvent pevent = (BlockPlaceEvent) event;
	    Block placedagainstblock = null;
	    if (pevent.getBlockAgainst() != null)
		placedagainstblock = pevent.getBlockAgainst();
	    Block newplacedagainstblock = null;

	    if (placedagainstblock != null)
		newplacedagainstblock = clone.getNewBlockLocation(pevent
			.getBlockAgainst());

	    CloneBlockPlaceEvent placeevent = new CloneBlockPlaceEvent(
		    newblock, newblock.getState(), newplacedagainstblock,
		    pevent.getItemInHand(), player, true);

	    plugin.getServer().getPluginManager().callEvent(placeevent);

	    if (!placeevent.canBuild() || placeevent.isCancelled()
		    || !clone.placeBlock(block, player)) {
		player.sendMessage(ChatColor.RED
			+ "Clone could not place block");
	    }

	    break;
	}

	// ClonePlayerInteractEvent newevent = new
	// ClonePlayerInteractEvent(e.getPlayer(), e.getAction(),
	// e.getPlayer().getItemInHand(), newblock,
	// c.getnewface(e.getBlockFace()));

	// plugin.getServer().getPluginManager().callEvent(newevent);

	// if (newevent.useInteractedBlock() != Result.DENY) {
	// }

	/*
	 * if(!c.Toggle(b, p)) { p.sendMessage("Clone could not place block"); }
	 */
    }

}
