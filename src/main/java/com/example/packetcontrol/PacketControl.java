package com.example.packetcontrol;

import com.example.packetcontrol.network.PacketQueueManager;
import com.example.packetcontrol.util.ModKeyBindings;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class PacketControl implements ClientModInitializer {

    public static boolean isPacketSendingEnabled = true;
    public static boolean isCloseWithoutPacketEnabled = false;

    private static KeyBinding togglePacketsKey;
    private static KeyBinding toggleGuiCloseKey;

    @Override
    public void onInitializeClient() {
        // Keybindings
        togglePacketsKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.packetcontrol.togglepackets",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_O, // O for "On/Off"
            "category.packetcontrol"
        ));

        toggleGuiCloseKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.packetcontrol.toggleguiclose",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_P, // P for "Packetless close"
            "category.packetcontrol"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (togglePacketsKey.wasPressed()) {
                isPacketSendingEnabled = !isPacketSendingEnabled;
                client.player.sendMessage(Text.literal("Packet sending is now " + (isPacketSendingEnabled ? "ENABLED" : "DISABLED")), true);
            }

            if (toggleGuiCloseKey.wasPressed()) {
                isCloseWithoutPacketEnabled = !isCloseWithoutPacketEnabled;
                client.player.sendMessage(Text.literal("Close GUI without packet is now " + (isCloseWithoutPacketEnabled ? "ENABLED" : "DISABLED")), true);
            }
        });

        ModKeyBindings.register();
        PacketQueueManager.start();
    }
}
