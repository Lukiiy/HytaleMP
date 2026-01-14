package me.lukiiy.hytaleMP;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;

import java.util.List;
import java.util.stream.Collectors;

public class MPConfig {
    public static final BuilderCodec<MPConfig> CODEC =
            BuilderCodec.builder(MPConfig.class, MPConfig::new)
                    .append(new KeyedCodec<>("Welcome", Codec.STRING), (cfg, val, x) -> cfg.welcome = val, (cfg, x) -> cfg.welcome).add()
                    .append(new KeyedCodec<>("ChatCoords", Codec.BOOLEAN), (cfg, val, x) -> cfg.chatCoords = val, (cfg, x) -> cfg.chatCoords).add()
                    .append(new KeyedCodec<>("DeathMsg", Codec.STRING), (cfg, val, x) -> cfg.deathMsg = val, (cfg, x) -> cfg.deathMsg).add()
                    .append(new KeyedCodec<>("DeathItemsMsg", Codec.STRING), (cfg, val, x) -> cfg.deathItemsMsg = val, (cfg, x) -> cfg.deathItemsMsg).add()
                    .append(new KeyedCodec<>("CheatyBack", Codec.BOOLEAN), (cfg, val, x) -> cfg.cheatyBack = val, (cfg, x) -> cfg.cheatyBack).add()
                    .build();

    // Defaults
    private String welcome = "\nWelcome to HytaleMP!\nOnline players (%o): %ps\n";
    private boolean chatCoords = false;
    private String deathMsg = "You died at %x %y %z.";
    private String deathItemsMsg = "You lost %is.";
    private boolean cheatyBack;

    public String getWelcome() {
        List<PlayerRef> online = Universe.get().getPlayers();

        return welcome.replace("%o", online.size() + "").replace("%ps", online.stream().map(PlayerRef::getUsername).collect(Collectors.joining(", ")));
    }

    public String getDeathMsg(Vector3i vector3i) {
        return deathMsg.replace("%x", vector3i.getX() + "").replace("%y", vector3i.getY() + "").replace("%z", vector3i.getZ() + "");
    }

    public String getDeathItemsMsg() {
        return deathItemsMsg;
    }

    public boolean isBackCheaty() {
        return cheatyBack;
    }
}
