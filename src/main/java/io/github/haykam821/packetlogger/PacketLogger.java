package io.github.haykam821.packetlogger;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
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

    public static String makePacketString(Packet<?> packet) {
        StringBuilder packetString = new StringBuilder(packet.getClass().getName());
        packetString.append("[");

        Field[] fields = packet.getClass().getDeclaredFields();
        boolean addComma = false;
        for (Field field : fields) {
            if ((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC || "CODEC".equals(field.getName())) {
                continue;
            }

            if (addComma) {
                packetString.append(",");
            }
            addComma = true;

            packetString.append(field.getName());
            packetString.append("=");
            if (field.trySetAccessible()) {
                try {
                    Object value = field.get(packet);
                    packetString.append(value);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            } else {
                packetString.append("<error>");
            }
        }

        packetString.append("]");
        return packetString.toString();
    }

    public static void logSentPacket(Packet<?> packet, NetworkSide side) {
        LOGGER.info("Sending packet {} ({})", PacketLogger.makePacketString(packet), PacketLogger.getSideName(side));
    }

    public static void logReceivedPacket(Packet<?> packet, NetworkSide side) {
        LOGGER.info("Receiving packet {} ({})", PacketLogger.makePacketString(packet), PacketLogger.getSideName(side));
    }
}