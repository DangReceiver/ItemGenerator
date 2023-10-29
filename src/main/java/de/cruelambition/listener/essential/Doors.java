package de.cruelambition.listener.essential;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Openable;
import org.bukkit.block.data.type.Door;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.logging.Level;

public class Doors implements Listener {
	@EventHandler
	public void onDoorInteract(final PlayerInteractEvent e) {
		if (e.isCancelled()) return;

		final Player p = e.getPlayer();
		if (p.getGameMode() == GameMode.SPECTATOR) return;

		if (!e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && !e.getAction().equals(Action.LEFT_CLICK_BLOCK)) return;
		final Block b = e.getClickedBlock();

		if (b.equals(null) || !(b.getBlockData() instanceof Door)) return;
		if (e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {

			if (b.getType() == Material.IRON_DOOR) p.playSound(b.getLocation(), Sound.BLOCK_CHAIN_FALL, 2.25f, 0.0f);
			else p.playSound(b.getLocation(), Sound.BLOCK_LADDER_PLACE, 1.85f, 0.0f);

			return;
		}

		if (b.getType() == Material.IRON_DOOR) return;
		if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) matchDoubleDoor(b);
	}

	private void matchDoubleDoor(final Block block) {
		Block blockBottom = block.getRelative(BlockFace.DOWN),
				blockTop = block.getRelative(BlockFace.UP),
				blockNearBottom = blockBottom.getRelative(BlockFace.NORTH),
				blockNearTop = blockTop.getRelative(BlockFace.NORTH);

		if (blockBottom.getBlockData() instanceof Door) blockTop = block;
		else blockBottom = block;

		BlockFace direction;
		BlockFace direction2;

		final BlockFace face = ((Directional) blockBottom.getBlockData()).getFacing();

		switch (face) {
			case NORTH: {
				direction = BlockFace.EAST;
				direction2 = BlockFace.WEST;
				break;
			}

			case EAST: {
				direction = BlockFace.SOUTH;
				direction2 = BlockFace.NORTH;
				break;
			}

			case SOUTH: {
				direction = BlockFace.WEST;
				direction2 = BlockFace.EAST;
				break;
			}

			case WEST: {
				direction = BlockFace.NORTH;
				direction2 = BlockFace.SOUTH;
				break;
			}

			default: {
				Bukkit.getLogger().log(Level.WARNING, "[Helpy] Construction was reset.");
				return;
			}
		}

		if (((Door) blockTop.getBlockData()).getHinge().equals(Door.Hinge.RIGHT)) {
			blockNearBottom = blockBottom.getRelative(direction2);
			blockNearTop = blockTop.getRelative(direction2);

		} else {
			blockNearBottom = blockBottom.getRelative(direction);
			blockNearTop = blockTop.getRelative(direction);
		}

		if (!(blockNearBottom.getType().equals(block.getType()) &&
				blockNearTop.getType().equals(block.getType()))) return;

		if (((Door) blockTop.getBlockData()).getHinge() == ((Door) blockNearTop.getBlockData()).getHinge()) return;

		if (((Directional) blockBottom.getBlockData()).getFacing() !=
				((Directional) blockNearBottom.getBlockData()).getFacing()) return;

		this.changeDoor(blockNearBottom, blockBottom);
	}

	private void changeDoor(final Block blockChange, final Block reference) {
		final BlockState changeState = blockChange.getState();

		final Openable doorOpen = (Openable) changeState.getBlockData(),
				referenceD = (Openable) reference.getState().getBlockData();

		doorOpen.setOpen(!referenceD.isOpen());
		changeState.setBlockData(doorOpen);

		changeState.update();
	}
}
