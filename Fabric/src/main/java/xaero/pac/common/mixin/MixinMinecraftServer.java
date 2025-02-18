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

import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xaero.pac.OpenPartiesAndClaims;
import xaero.pac.OpenPartiesAndClaimsFabric;
import xaero.pac.common.server.IOpenPACMinecraftServer;
import xaero.pac.common.server.IServerDataAPI;

@Mixin(MinecraftServer.class)
public class MixinMinecraftServer implements IOpenPACMinecraftServer {

	private IServerDataAPI xaero_OPAC_ServerData;

	@Override
	public void setXaero_OPAC_ServerData(IServerDataAPI data) {
		xaero_OPAC_ServerData = data;
	}

	@Override
	public IServerDataAPI getXaero_OPAC_ServerData() {
		return xaero_OPAC_ServerData;
	}

	@Inject(at = @At("HEAD"), method = "loadLevel")
	public void onLoadLevel(CallbackInfo callbackInfo) throws Throwable {
		((OpenPartiesAndClaimsFabric)OpenPartiesAndClaims.INSTANCE).getCommonEvents().onServerAboutToStart((MinecraftServer)(Object)this);
	}

}
