package nl._deurklink_.watershow;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Main extends JavaPlugin implements Listener
{
	public void onEnable()
	{
		Bukkit.getPluginManager().registerEvents(this, this);
	}
	@SuppressWarnings("deprecation")
	public void shootWater(Location loc, Material mat, Location destination, int color)
	{
		FallingBlock fb = loc.getWorld().spawnFallingBlock(loc, mat, (byte)color);
		fb.setVelocity(destination.toVector());
		fb.setDropItem(false);
	}
	@EventHandler
	public void onBlockRedstone(final BlockRedstoneEvent e)
	{
		ArrayList<Block> checkList = new ArrayList<Block>();
		
		checkList.add(e.getBlock().getRelative(1, 0, 0));
		checkList.add(e.getBlock().getRelative(0, 0, 1));
		checkList.add(e.getBlock().getRelative(0, 0, -1));
		checkList.add(e.getBlock().getRelative(-1, 0, 0));
		
		for(final Block b : checkList)
		{
			if (b.getState() instanceof Sign)
			{
				final Sign sign = (Sign)b.getState();
				
				if (sign.getLine(0).split(":")[0].equalsIgnoreCase("[ws]")) 
				{
					new BukkitRunnable()
					{
						public void run()
						{
							if (sign.getLine(0).split(":")[0].equalsIgnoreCase("[ws]"))
							{
								if (e.getBlock().isBlockIndirectlyPowered() || e.getBlock().isBlockPowered())
								{
									Sign sign = (Sign)b.getState();
									World tWorld = sign.getBlock().getWorld();
									float xdest = Float.parseFloat(sign.getLine(1));
									float ydest = Float.parseFloat(sign.getLine(2));
									float zdest = Float.parseFloat(sign.getLine(3));
									Location destination = new Location(tWorld, xdest, ydest, zdest);
									int spawny = Integer.parseInt(sign.getLine(0).split(":")[1]);
									int color = Integer.parseInt(sign.getLine(0).split(":")[2]);
									Location spawn = sign.getLocation().add(0.0D, spawny, 0.0D);
									Main.this.shootWater(spawn, Material.STAINED_GLASS, destination, color);
								}
								else
								{
									cancel();
								}
							}
							else 
							{
								cancel();
							}
						}
					}.runTaskTimer(this, 0L, 1L);
				}
			}
		}
	}
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onBlockFall(EntityChangeBlockEvent e) 
	{
		if ((e.getEntityType() == EntityType.FALLING_BLOCK)) 
		{
			FallingBlock fs = (FallingBlock) e.getEntity();
			if(fs.getBlockId() == 95)
			{
				e.setCancelled(true);
			}
		}
	}
}
