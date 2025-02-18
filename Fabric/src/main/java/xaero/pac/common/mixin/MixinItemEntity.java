/*
 * Open Parties and Claims - adds chunk claims and player parties to Minecraft
 * Copyright (C) 2022-2023, Xaero <xaero1996@gmail.com> and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of version 3 of the GNU Lesser General Public License
 * (LGPL-3.0-only) as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received copies of the GNU Lesser General Public License
 * and the GNU General Public License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
 */

package xaero.pac.common.mixin;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xaero.pac.common.entity.IItemEntity;
import xaero.pac.common.server.core.ServerCore;

import java.util.UUID;

@Mixin(value = ItemEntity.class, priority = 1000001)
public class MixinItemEntity implements IItemEntity {

	private UUID xaero_OPAC_throwerAccessor;
	@Shadow
	private UUID thrower;
	@Shadow
	private UUID target;

	@Inject(method = "playerTouch", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/item/ItemEntity;getItem()Lnet/minecraft/world/item/ItemStack;"), cancellable = true)
	public void onPlayerTouch(Player player, CallbackInfo ci){
		if(ServerCore.onEntityItemPickup(player, (ItemEntity)(Object)this))
			ci.cancel();
	}

	@Inject(method = "merge(Lnet/minecraft/world/entity/item/ItemEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/item/ItemEntity;Lnet/minecraft/world/item/ItemStack;)V", at = @At("HEAD"), cancellable = true)
	private static void onMerge(ItemEntity first, ItemStack firstStack, ItemEntity second, ItemStack secondStack, CallbackInfo ci){
		if(ServerCore.onItemMerge(first, second))
			ci.cancel();
	}

	@Override
	public UUID getXaero_OPAC_throwerAccessor() {
		return xaero_OPAC_throwerAccessor;
	}

	@Override
	public void setXaero_OPAC_throwerAccessor(UUID xaero_OPAC_throwerAccessor) {
		this.xaero_OPAC_throwerAccessor = xaero_OPAC_throwerAccessor;
	}

	@Override
	public UUID getXaero_OPAC_thrower() {
		return thrower;
	}

	@Override
	public UUID getXaero_OPAC_target() {
		return target;
	}

}
