package com.example.packetcontrol.util;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class ModKeyBindings {

    private static KeyBinding saveGuiKey;
    private static KeyBinding loadGuiKey;

    public static void register() {
        saveGuiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.packetcontrol.savegui",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_K, // K for sa"K"e
            "category.packetcontrol"
        ));

        loadGuiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.packetcontrol.loadgui",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_L, // L for "L"oad
            "category.packetcontrol"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (saveGuiKey.wasPressed()) {
                GuiStateSaver.saveCurrentGuiState("default");
            }
            if (loadGuiKey.wasPressed()) {
                GuiStateSaver.loadGuiState("default");
            }
        });
    }
}
