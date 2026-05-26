package dev.massuus.vaultpartyui.client;

import com.mojang.logging.LogUtils;
import iskallia.vault.client.data.ClientPartyData;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.loading.FMLPaths;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class ClientRestorePrevParty {
    private static final String modConfigDir = "vhPartyUi/";
    public static String loadedWorld = "";
    private static List<String> players = new ArrayList<>();
    private static Logger LOGGER = LogUtils.getLogger();

    public static void loadConfigForWorld(String world) {
        LOGGER.info("Loading config for world: {}", world);
        var configDir = ensureConfigFolderExists();
        var configFile = configDir.resolve(String.format("%s.txt", world));

        if (!Files.exists(configFile)) {
            players = new ArrayList<>(0);
            return;
        }
        LOGGER.info("Reading config for world: {}", world);
        String fileContents;
        try {
            fileContents = Files.readString(configFile).trim();
        } catch (IOException e) {
            players = new ArrayList<>(0);
            loadedWorld = world;
            return;
        }

        var prevPartList = fileContents.split(",");
        players = List.of(prevPartList);
        loadedWorld = world;
    }

    public static void save() {
        var configDir = ensureConfigFolderExists();
        var configFile = configDir.resolve(String.format("%s.txt", loadedWorld));

        if(loadedWorld.isEmpty()) {
            LOGGER.warn("Trying to save to a empty world: {}", loadedWorld);
            return;
        }

        var fileContent = String.join(",", players);
        try {
            Files.writeString(configFile, fileContent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void addPlayer(Player player) {
        Minecraft minecraft = Minecraft.getInstance();
        Player currentPlayer = minecraft.player;
        var serverConnection = minecraft.getConnection();
        if(!ClientPartyData.getParty(currentPlayer.getUUID()).hasMember(player.getUUID())) {
            LOGGER.warn("Tried to addPlayer that isnt in the party to restoreParty options. This is likely a bug");
            return;
        }
        if(!serverConnection.getOnlinePlayerIds().contains(player.getUUID())) {
            LOGGER.warn("Tried to addPlayer to restoreParty options. This is likely a bug");
            return;
        }

        players.add(player.getUUID().toString());

        save();
    }

    public void removePlayer(Player player) {
        if(!players.contains(player.getUUID().toString())) {
            LOGGER.warn("Tried to removePlayer that isn't saved already to restoreParty options. This is likely a bug");
        }

        players.remove(player.getUUID().toString());

        save();
    }


    private static Path ensureConfigFolderExists() {
        var configPath = FMLPaths.CONFIGDIR.get().resolve(modConfigDir);
        if (!Files.exists(configPath)) {
            try {
                Files.createDirectories(configPath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return configPath;
    }
}
