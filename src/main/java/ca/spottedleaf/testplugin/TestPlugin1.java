package ca.spottedleaf.testplugin;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;
import java.util.Locale;
import java.util.UUID;

public class TestPlugin1 extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onAsyncChat(final AsyncPlayerChatEvent event) {
        final UUID playerUniqueId  = event.getPlayer().getUniqueId();
        final String message = event.getMessage();

        Bukkit.getScheduler().runTask(this, () -> {
            final Player player = Bukkit.getPlayer(playerUniqueId);

            if (player == null) {
                return;
            }

            final World world = player.getWorld();

            final String[] args = message.split(" ");

            if (args.length < 3) {
                player.sendMessage("<add1/remove1/query1> <x> <z>");
                return;
            }

            final int x = Integer.parseInt(args[1]);
            final int z = Integer.parseInt(args[2]);

            switch (args[0].toLowerCase(Locale.ENGLISH)) {
                case "add1":
                    world.addPluginTicket(x, z, this);
                    player.sendMessage("Added to " + x + ", " + z);
                    break;
                case "remove1":
                    world.removePluginTicket(x, z, this);
                    player.sendMessage("Removed from " + x + ", " + z);
                    break;
                case "query1":
                    Collection<Plugin> tickets = world.queryPluginTickets(x, z);

                    StringBuilder builder = new StringBuilder();
                    builder.append("Contains plugins: [");
                    boolean first = true;
                    for (Plugin plugin : tickets) {
                        if (!first) {
                            builder.append(", ");
                        } else {
                            first = false;
                        }
                        builder.append(plugin.getName());
                    }
                    builder.append("], is loaded: ").append(world.isChunkLoaded(x, z));

                    player.sendMessage(builder.toString());
                    break;
            }

        });
    }
}