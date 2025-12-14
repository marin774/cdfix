package me.marin.cdfix.mixin;

import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static org.lwjgl.glfw.GLFW.GLFW_CURSOR;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_NORMAL;

@Mixin(InputUtil.class)
public class InputUtilMixin {

    /*
        Cursor displacement bug:
        Setting the cursor position in 'GLFW_CURSOR_HIDDEN' mode gets *ignored*, which makes the cursor
        stay at the outdated/wrong position (center of the window before the cursor got disabled).
    */

    @Inject(method="setCursorParameters", at = @At("HEAD"), cancellable = true)
    private static void setCursorParameters(long handler, int i, double d, double e, CallbackInfo ci) {
        // If cursor is being enabled, first set mode to GLFW_CURSOR_NORMAL, then set the position.
        if (i == GLFW_CURSOR_NORMAL) {
            GLFW.glfwSetInputMode(handler, GLFW_CURSOR, i);
            GLFW.glfwSetCursorPos(handler, d, e);
            ci.cancel();
        }
    }

}
