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

    enum changetype {
	blockbreak, blockplace, interact
    }

    Clone c;
    public static CloneMe plugin;
    Block b;
    Player p;
    changetype change;
    Event event;

    public ScheduledBlockChange(final Clone clone, final CloneMe instance,
	    final Player player, final Block block, changetype changes,
	    Event eevent) {
	c = clone;
	plugin = instance;
	p = player;
	b = block;
	change = changes;
	event = eevent;
    }

    @Override
    public void run() {

	// plugin.logger.info("CloneMe PlayerData = " +
	// e.getClickedBlock().getData());

	Block newblock = c.getNewBlockLocation(b);

	switch (change) {
	case interact:

	    if (newblock != null && newblock.getType() != Material.AIR) {
		net.minecraft.server.World myworld = ((CraftWorld) b.getWorld())
			.getHandle();
		net.minecraft.server.EntityHuman human = ((CraftPlayer) p)
			.getHandle();

		if (myworld != null && human != null) {
		    net.minecraft.server.Block.byId[newblock.getTypeId()]
			    .interact(myworld, newblock.getX(),
				    newblock.getY(), newblock.getZ(), human);
		}
	    }
	    break;
	case blockbreak:

	    CloneBlockBreakEvent breakevent = new CloneBlockBreakEvent(
		    newblock, p);

	    plugin.getServer().getPluginManager().callEvent(breakevent);

	    if (breakevent.isCancelled() || !c.Break(b, p)) {
		p.sendMessage(ChatColor.RED + "Clone could not break block");
	    }

	    break;
	case blockplace:

	    BlockPlaceEvent pevent = (BlockPlaceEvent) event;
	    Block placedagainstblock = null;
	    if (pevent.getBlockAgainst() != null)
		placedagainstblock = pevent.getBlockAgainst();
	    Block newplacedagainstblock = null;

	    if (placedagainstblock != null)
		newplacedagainstblock = c.getNewBlockLocation(pevent
			.getBlockAgainst());

	    CloneBlockPlaceEvent placeevent = new CloneBlockPlaceEvent(
		    newblock, newblock.getState(), newplacedagainstblock,
		    pevent.getItemInHand(), p, true);

	    plugin.getServer().getPluginManager().callEvent(placeevent);

	    if (!placeevent.canBuild() || placeevent.isCancelled()
		    || !c.Place(b, p)) {
		p.sendMessage(ChatColor.RED + "Clone could not place block");
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
