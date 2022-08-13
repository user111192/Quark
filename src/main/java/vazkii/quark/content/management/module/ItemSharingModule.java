/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Quark Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Quark
 *
 * Quark is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 *
 * File Created @ [06/06/2016, 01:40:29 (GMT)]
 */
package vazkii.quark.content.management.module;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import vazkii.quark.base.module.LoadModule;
import vazkii.quark.base.module.ModuleCategory;
import vazkii.quark.base.module.ModuleLoader;
import vazkii.quark.base.module.QuarkModule;
import vazkii.quark.base.module.config.Config;

import java.util.List;

@LoadModule(category = ModuleCategory.MANAGEMENT, hasSubscriptions = true, subscribeOn = Dist.CLIENT)
public class ItemSharingModule extends QuarkModule {

	@Config
	public static boolean renderItemsInChat = true;

	@OnlyIn(Dist.CLIENT)
	public static void renderItemForMessage(PoseStack poseStack, FormattedCharSequence sequence, float x, float y, int color) {
		if (!ModuleLoader.INSTANCE.isModuleEnabled(ItemSharingModule.class) || !renderItemsInChat)
			return;

		Minecraft mc = Minecraft.getInstance();

		StringBuilder before = new StringBuilder();

		sequence.accept((counter_, style, character) -> {
			String sofar = before.toString();
			if (sofar.endsWith("    ")) {
				render(mc, poseStack, sofar.substring(0, sofar.length() - 3), x, y, style, color);
				return false;
			}
			before.append((char) character);
			return true;
		});
	}

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public void keyboardEvent(ScreenEvent.KeyPressed.Pre event) {
		Minecraft mc = Minecraft.getInstance();
		Options settings = mc.options;
		Screen screen = event.getScreen();
		if(InputConstants.isKeyDown(mc.getWindow().getWindow(), settings.keyChat.getKey().getValue()) &&
				screen instanceof AbstractContainerScreen<?> gui && Screen.hasShiftDown()) {

			List<? extends GuiEventListener> children = gui.children();
			for(GuiEventListener c : children)
				if(c instanceof EditBox tf) {
					if(tf.isFocused())
						return;
				}

			Slot slot = gui.getSlotUnderMouse();
			if(slot != null) {
				ItemStack stack = slot.getItem();

				if(!stack.isEmpty()) {
					LocalPlayer player = mc.player;
					if (player != null) {
						Component fullComp = Component.translatable("chat.type.text", player.getDisplayName(), stack.getDisplayName());
						player.chatSigned(fullComp.getString(), fullComp);
						event.setCanceled(true);
					}
				}
			}
		}
	}

	public static MutableComponent createStackComponent(ItemStack stack, MutableComponent component) {
		if (!ModuleLoader.INSTANCE.isModuleEnabled(ItemSharingModule.class) || !renderItemsInChat)
			return component;

		Style style = component.getStyle();
		if (stack.getCount() > 64) {
			ItemStack copyStack = stack.copy();
			copyStack.setCount(64);
			style = style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, new HoverEvent.ItemStackInfo(copyStack)));
			component.withStyle(style);
		}

		MutableComponent out = Component.literal("   ");
		out.setStyle(style);
		return out.append(component);
	}

	@OnlyIn(Dist.CLIENT)
	private static void render(Minecraft mc, PoseStack pose, String before, float x, float y, Style style, int color) {
		float a = (color >> 24 & 255) / 255.0F;

		HoverEvent hoverEvent = style.getHoverEvent();
		if (hoverEvent != null && hoverEvent.getAction() == HoverEvent.Action.SHOW_ITEM) {
			HoverEvent.ItemStackInfo contents = hoverEvent.getValue(HoverEvent.Action.SHOW_ITEM);

			ItemStack stack = contents != null ? contents.getItemStack() : ItemStack.EMPTY;

			if (stack.isEmpty())
				stack = new ItemStack(Blocks.BARRIER); // for invalid icon

			int shift = mc.font.width(before);

			if (a > 0) {
				alphaValue = a;

				PoseStack poseStack = RenderSystem.getModelViewStack();

				poseStack.pushPose();

				poseStack.mulPoseMatrix(pose.last().pose());

				poseStack.translate(shift + x, y, 0);
				poseStack.scale(0.5f, 0.5f, 0.5f);
				mc.getItemRenderer().renderGuiItem(stack, 0, 0);
				poseStack.popPose();

				RenderSystem.applyModelViewMatrix();

				alphaValue = 1F;
			}
		}
	}


	// used in a mixin because rendering overrides are cursed by necessity hahayes
	public static float alphaValue = 1F;
}
