package com.example.packetcontrol.mixin;

import com.example.packetcontrol.PacketControl;
import com.example.packetcontrol.network.PacketQueueManager;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {

    @Inject(method = "sendPacket(Lnet/minecraft/network/packet/Packet;)V", at = @At("HEAD"), cancellable = true)
    private void onSendPacket(Packet<?> packet, CallbackInfo ci) {
        // Check if packet sending is globally disabled
        if (!PacketControl.isPacketSendingEnabled) {
            ci.cancel();
            return;
        }

        // Check if we should close GUI without sending the packet
        if (PacketControl.isCloseWithoutPacketEnabled && packet instanceof CloseHandledScreenC2SPacket) {
            ci.cancel();
            return;
        }

        // If packet delay is active, queue the packet instead of sending it
        if (PacketQueueManager.isDelayActive()) {
            PacketQueueManager.queuePacket((Packet<?>) packet);
            ci.cancel();
        }
    }
}
