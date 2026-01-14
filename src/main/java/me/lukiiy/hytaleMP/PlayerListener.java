package me.lukiiy.hytaleMP;

import com.hypixel.hytale.event.EventRegistry;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.event.events.player.PlayerChatEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import me.lukiiy.hytaleMP.utils.PlayerHelper;

public class PlayerListener {
    public PlayerListener(EventRegistry registry) {
        registry.registerGlobal(PlayerReadyEvent.class, this::ready);
        registry.registerGlobal(PlayerChatEvent.class, this::chat);
    }

    public void ready(PlayerReadyEvent e) {
        e.getPlayer().sendMessage(Message.raw(HytaleMP.getInstance().CONFIG.get().getWelcome()));
    }

    public void chat(PlayerChatEvent e) {
        PlayerRef sender = e.getSender();
        if (sender.getWorldUuid() == null) return;

        World w = Universe.get().getWorld(sender.getWorldUuid());
        if (w == null) return;

        Vector3d loc = PlayerHelper.getPlayerLocation(w, sender);
        if (loc == null) return;

        e.setContent(e.getContent().replace("[coords]", (int) loc.getX() + " " + (int) loc.getY() + " " + (int) loc.getZ()));
    }
}
