/*
 * Open Parties and Claims - adds chunk claims and player parties to Minecraft
 * Copyright (C) 2023, Xaero <xaero1996@gmail.com> and contributors
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

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BasePressurePlateBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xaero.pac.common.server.core.ServerCore;

@Mixin(value = BasePressurePlateBlock.class, priority = 1000001)
public class MixinBasePressurePlateBlock {

	@Inject(method = "checkPressed", at = @At(value = "HEAD"))
	public void onCheckPressedPre(Entity entity, Level level, BlockPos blockPos, BlockState blockState, int i, CallbackInfo cir){
		ServerCore.beforePressurePlateCheckPressed(level, (Block)(Object)this, blockPos);
	}

	@Inject(method = "checkPressed", at = @At(value = "RETURN"))
	public void onCheckPressedPost(Entity entity, Level level, BlockPos blockPos, BlockState blockState, int i, CallbackInfo cir){
		ServerCore.afterPressurePlateCheckPressed(level);
	}

}
