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

package xaero.pac.client.parties.party.api;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

/**
 * API for party ally info storage
 */
public interface IClientPartyAllyInfoStorageAPI {

	/**
	 * Gets info about an ally party with the specified ID.
	 *
	 * @param id  the UUID of the ally party, not null
	 * @return the info about the ally party, null if it doesn't exist
	 */
	@Nullable
	public IClientPartyAllyInfoAPI get(@Nonnull UUID id);
	
}
