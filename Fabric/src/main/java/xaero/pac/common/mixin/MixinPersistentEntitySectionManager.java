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

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.entity.EntityAccess;
import net.minecraft.world.level.entity.PersistentEntitySectionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xaero.pac.OpenPartiesAndClaims;
import xaero.pac.OpenPartiesAndClaimsFabric;

@Mixin(value = PersistentEntitySectionManager.class, priority = 1000001)
public class MixinPersistentEntitySectionManager {

	@Inject(at = @At("HEAD"), method = "addEntity", cancellable = true)
	public void onAddEntity(EntityAccess entityAccess, boolean fromDisk, CallbackInfoReturnable<Boolean> cir){
		if(entityAccess instanceof Entity entity
				&& ((OpenPartiesAndClaimsFabric) OpenPartiesAndClaims.INSTANCE).getCommonEvents().onEntityJoinWorld(entity, entity.level(), fromDisk))
			cir.setReturnValue(false);
	}

}
