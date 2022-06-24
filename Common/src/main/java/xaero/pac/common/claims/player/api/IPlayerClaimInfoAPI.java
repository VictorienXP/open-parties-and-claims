/*
 *     Open Parties and Claims - adds chunk claims and player parties to Minecraft
 *     Copyright (C) 2022, Xaero <xaero1996@gmail.com> and contributors
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of version 3 of the GNU Lesser General Public License
 *     (LGPL-3.0-only) as published by the Free Software Foundation.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received copies of the GNU Lesser General Public License
 *     and the GNU General Public License along with this program.
 *     If not, see <https://www.gnu.org/licenses/>.
 */

package xaero.pac.common.claims.player.api;

import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * API for claim info of a player
 */
public interface IPlayerClaimInfoAPI<DC extends IPlayerDimensionClaimsAPI<?>> {

	/**
	 * Gets the number of claims owned by the player.
	 *
	 * @return the claim count
	 */
	public int getClaimCount();

	/**
	 * Gets the number of forceloadable claims owned by the player.
	 *
	 * @return the forceloadable claim count
	 */
	public int getForceloadCount();

	/**
	 * Gets the UUID of the player.
	 *
	 * @return the UUID of the player, not null
	 */
	@Nonnull
	public UUID getPlayerId();

	/**
	 * Gets the username of the player.
	 * <p>
	 * Can just be the UUID in string form until the player logs in and it's updated to the actual current username,
	 * or before it is synced to the client.
	 *
	 * @return the username of the player, not null
	 */
	@Nonnull
	public String getPlayerUsername();

	/**
	 * Gets the currently configured custom name of the player's claims.
	 * <p>
	 * Can be empty if a custom name is not configured or null if the name hasn't been synced to the client yet.
	 *
	 * @return the custom name of claimed chunks
	 */
	@Nullable
	public String getClaimsName();

	/**
	 * Gets the currently configured color of the player's claims.
	 * <p>
	 * Is 0 on the client side before it is synced from the server.
	 *
	 * @return the claim color int
	 */
	public int getClaimsColor();

	/**
	 * Gets a stream of all dimension claim info entries for the player.
	 *
	 * @return the stream of all dimension claim info entries, not null
	 */
	@Nonnull
	public Stream<Entry<ResourceLocation, DC>> getStream();
	
	
}
