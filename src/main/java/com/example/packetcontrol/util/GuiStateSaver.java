package com.example.packetcontrol.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GuiStateSaver {

    private static ScreenHandler lastHandledScreen;
    private static final Map<String, List<ItemStack>> savedStates = new HashMap<>();

    public static void setLastHandledScreen() {
        if (MinecraftClient.getInstance().player != null) {
            lastHandledScreen = MinecraftClient.getInstance().player.currentScreenHandler;
        }
    }

    public static void saveCurrentGuiState(String name) {
        if (lastHandledScreen == null) {
            MinecraftClient.getInstance().player.sendMessage(Text.literal("No GUI to save!"), true);
            return;
        }

        List<ItemStack> items = new ArrayList<>();
        for (Slot slot : lastHandledScreen.slots) {
            items.add(slot.getStack().copy());
        }
        savedStates.put(name, items);
        MinecraftClient.getInstance().player.sendMessage(Text.literal("Saved GUI state as '" + name + "'"), true);
    }

    public static void loadGuiState(String name) {
        if (!savedStates.containsKey(name)) {
            MinecraftClient.getInstance().player.sendMessage(Text.literal("No saved GUI state named '" + name + "'"), true);
            return;
        }

        // NOTE: You cannot "re-open" a GUI without server packets.
        // This function will print the contents of the saved state to chat as a proof-of-concept.
        List<ItemStack> items = savedStates.get(name);
        MinecraftClient.getInstance().player.sendMessage(Text.literal("--- Contents of saved GUI '" + name + "' ---"), false);
        for (int i = 0; i < items.size(); i++) {
            ItemStack stack = items.get(i);
            if (!stack.isEmpty()) {
                MinecraftClient.getInstance().player.sendMessage(Text.literal("Slot " + i + ": " + stack.toHoverableText().getString()), false);
            }
        }
        MinecraftClient.getInstance().player.sendMessage(Text.literal("------------------------------------"), false);
    }
}
