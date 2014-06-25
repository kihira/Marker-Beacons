/*
 * Copyright (C) 2014  Kihira
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */

package kihira.beacons.common

import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.FontRenderer
import org.lwjgl.opengl.GL11

class TextComponent extends LogoComponent {

  override var title: String = "Text"

  var text: String = "Banana"
  var textColour: Int = 0xFFFFFF

  @SideOnly(Side.CLIENT) override def drawComponent(): Unit = {
    val fontRenderer: FontRenderer = Minecraft.getMinecraft.fontRenderer
    GL11.glScalef(0.05F, 0.05F, 0)
    GL11.glTranslatef(-fontRenderer.getStringWidth(text), 25F, 0F)
    fontRenderer.drawString(text, fontRenderer.getStringWidth(text) / 2, 0, textColour)
  }
}
