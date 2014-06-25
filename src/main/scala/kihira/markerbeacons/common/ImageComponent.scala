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

package kihira.markerbeacons.common

import kihira.markerbeacons.client.icon.{IconData, IconManager}
import net.minecraft.client.renderer.Tessellator
import org.lwjgl.opengl.GL11

class ImageComponent extends LogoComponent {

  override var title: String = "Image"

  var iconData: IconData = IconManager.iconList(0)

  override def drawComponent() {
    if (iconData != null) {
      val tessellator: Tessellator = Tessellator.instance
      //Scales the UV positions depending on the size of the base texture imagine
      val scaleX: Float = 1F / iconData.textureXSize
      val scaleY: Float = 1F / iconData.textureYSize
      //This allows for off shaped textures (need to test with larger width then height
      val yOffset: Float = if (iconData.width > iconData.height) iconData.height.asInstanceOf[Float] / iconData.width.asInstanceOf[Float] else 1
      val xOffset: Float = if (iconData.width < iconData.height) iconData.width.asInstanceOf[Float]/ iconData.height.asInstanceOf[Float] else 1

      //Adjust the position depending on if the icon is odd sized
      if (yOffset != 1) GL11.glTranslatef(0F, yOffset / 2, 0F)
      if (xOffset != 1) GL11.glTranslatef(0F, xOffset / 2, 0F)

      IconManager.bindTexture(iconData)
      tessellator.startDrawingQuads()
      tessellator.addVertexWithUV(-xOffset, yOffset, 0, iconData.minU * scaleX, (iconData.minV + iconData.height) * scaleY)
      tessellator.addVertexWithUV(xOffset, yOffset, 0, (iconData.minU + iconData.width) * scaleX, (iconData.minV + iconData.height) * scaleY)
      tessellator.addVertexWithUV(xOffset, -yOffset, 0, (iconData.minU + iconData.width) * scaleX, iconData.minV * scaleY)
      tessellator.addVertexWithUV(-xOffset, -yOffset, 0, iconData.minU * scaleX, iconData.minV * scaleY)
      tessellator.draw
    }
  }
}
