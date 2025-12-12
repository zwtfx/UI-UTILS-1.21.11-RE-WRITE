package com.example.packetcontrol.network;

import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.Packet;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PacketQueueManager {

    private static final Queue<Packet<?>> packetQueue = new ConcurrentLinkedQueue<>();
    private static ScheduledExecutorService scheduler;
    private static boolean delayActive = false;
    private static long delayMillis = 1000; // Default 1 second delay

    public static void start() {
        scheduler = Executors.newSingleThreadScheduledExecutor();
    }

    public static void stop() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
        }
    }

    public static void queuePacket(Packet<?> packet) {
        packetQueue.add(packet);
    }

    public static void setDelay(long millis) {
        delayMillis = millis;
    }

    public static long getDelay() {
        return delayMillis;
    }

    public static void toggleDelay() {
        if (delayActive) {
            // If we are disabling, flush the queue immediately
            flushQueue();
            delayActive = false;
        } else {
            delayActive = true;
        }
    }

    public static boolean isDelayActive() {
        return delayActive;
    }

    public static void flushQueue() {
        if (MinecraftClient.getInstance().getNetworkHandler() == null) return;

        while (!packetQueue.isEmpty()) {
            Packet<?> packet = packetQueue.poll();
            if (packet != null) {
                MinecraftClient.getInstance().getNetworkHandler().sendPacket(packet);
            }
        }
    }
    
    // This is a simplified version. A real implementation would need a more robust
    // way to send packets after a delay, perhaps one packet at a time.
    // For this example, we'll just toggle and flush.
    // To make it a true delay, you would call `flushQueue()` on a scheduled task.
}
