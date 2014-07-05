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

package kihira.markerbeacons.client.icon

import java.awt.image.BufferedImage
import java.io.{File, IOException}
import javax.imageio.ImageIO

import kihira.markerbeacons.common.MarkerBeacons
import net.minecraft.client.renderer.texture.{AbstractTexture, TextureUtil}
import net.minecraft.client.resources.IResourceManager

class IconTexture(val imageFile : File) extends AbstractTexture {

  override def loadTexture(var1: IResourceManager): Unit = {
    this.deleteGlTexture()

    try {
      val bufferedimage: BufferedImage = ImageIO.read(imageFile)
      TextureUtil.uploadTextureImageAllocate(this.getGlTextureId, bufferedimage, false, false)  
    } catch {
      case ioe: IOException => MarkerBeacons.logger.warn("Unable to load icon texture from file", ioe)
    }
  }
}
