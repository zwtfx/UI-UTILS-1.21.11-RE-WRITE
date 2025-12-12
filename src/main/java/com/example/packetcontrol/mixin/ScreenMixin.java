package com.example.packetcontrol.mixin;

import com.example.packetcontrol.util.GuiStateSaver;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public class ScreenMixin {

    @Inject(method = "init", at = @At("TAIL"))
    private void onInit(CallbackInfo ci) {
        // This is a simple trigger. A more robust solution might check for specific screen types.
        GuiStateSaver.setLastHandledScreen();
    }
}
