package me.lukiiy.hytaleMP;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.modules.singleplayer.commands.PlayOnlineCommand;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;

import java.util.List;
import java.util.stream.Collectors;

public class MPConfig {
    public static final BuilderCodec<MPConfig> CODEC =
            BuilderCodec.builder(MPConfig.class, MPConfig::new)
                    .append(new KeyedCodec<>("Welcome", Codec.STRING), (cfg, val, x) -> cfg.welcome = val, (cfg, x) -> cfg.welcome).add()
                    .append(new KeyedCodec<>("ChatCoords", Codec.BOOLEAN), (cfg, val, x) -> cfg.chatCoords = val, (cfg, x) -> cfg.chatCoords).add()
                    .build();

    // Defaults
    public String welcome = "\nWelcome to HytaleMP!\nOnline players (%o): %ps\n";
    public boolean chatCoords = false;

    public String getWelcome() {
        List<PlayerRef> online = Universe.get().getPlayers();

        return welcome.replace("%o", online.size() + "").replace("%ps", online.stream().map(PlayerRef::getUsername).collect(Collectors.joining(", ")));
    }
}
