package io.github.haykam821.packetlogger.mixin;

import io.github.haykam821.packetlogger.PacketLogger;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.NetworkSide;
import net.minecraft.network.PacketCallbacks;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.Packet;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientConnection.class)
public abstract class ClientConnectionMixin {
    @Shadow
    public abstract NetworkSide getSide();

    @Inject(method="sendInternal", at=@At("HEAD"))
    private void logSentPacket(Packet<?> packet, @Nullable PacketCallbacks callbacks, boolean flush, CallbackInfo ci) {
        PacketLogger.logSentPacket(packet, this.getSide());
    }

    @Inject(method="handlePacket", at=@At("HEAD"))
    private static void logReceivedPacket(Packet<?> packet, PacketListener listener, CallbackInfo ci) {
        PacketLogger.logReceivedPacket(packet, listener.getSide());
    }
}