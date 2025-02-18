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

package xaero.pac.client.gui.widget;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

import javax.annotation.Nullable;

public class FixedEditBox extends EditBox {

	public FixedEditBox(Font $$0, int $$1, int $$2, int $$3, int $$4, Component $$5) {
		super($$0, $$1, $$2, $$3, $$4, $$5);
	}

	public FixedEditBox(Font $$0, int $$1, int $$2, int $$3, int $$4, @Nullable EditBox $$5, Component $$6) {
		super($$0, $$1, $$2, $$3, $$4, $$5, $$6);
	}

	@Override
	public void updateWidgetNarration(NarrationElementOutput $$0) {
		$$0.add(NarratedElementType.TITLE, createNarrationMessage());
	}

}
