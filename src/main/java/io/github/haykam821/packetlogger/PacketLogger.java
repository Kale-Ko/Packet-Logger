package io.github.haykam821.packetlogger;

import java.util.Locale;
import net.minecraft.network.NetworkSide;
import net.minecraft.network.packet.Packet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PacketLogger {
    private static final Logger LOGGER = LogManager.getLogger("Packet Logger");

    private PacketLogger() {
    }

    private static String getSideName(NetworkSide side) {
        if (side == NetworkSide.CLIENTBOUND)
            return "client";
        if (side == NetworkSide.SERVERBOUND)
            return "server";

        return side.name().toLowerCase(Locale.ROOT);
    }

    public static void logSentPacket(Packet<?> packet, NetworkSide side) {
        String sideName = PacketLogger.getSideName(side);

        LOGGER.info("Sending packet with name '{}' ({})", packet.getClass().getName(), sideName);
    }

    public static void logReceivedPacket(Packet<?> packet, NetworkSide side) {
        String sideName = PacketLogger.getSideName(side);

        LOGGER.info("Received packet with name '{}' ({})", packet.getClass().getName(), sideName);
    }
}