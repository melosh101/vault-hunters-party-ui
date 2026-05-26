package dev.massuus.vaultpartyui.client;

import dev.massuus.vaultpartyui.VaultPartyUiMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import iskallia.vault.client.data.ClientPartyData;
import net.minecraftforge.network.NetworkEvent;

@Mod.EventBusSubscriber(modid = VaultPartyUiMod.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientPartyRecieveEvent {
    private  ClientPartyRecieveEvent() {}
    private static final ResourceLocation TARGET_CHANNEL = new ResourceLocation()
    public static void onNetworkSetiop(NetworkEvent.ClientCustomPayloadEvent)
}
