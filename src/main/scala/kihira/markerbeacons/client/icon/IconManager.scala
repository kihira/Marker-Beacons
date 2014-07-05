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

import java.io.{File, IOException}

import kihira.markerbeacons.common.MarkerBeacons
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.texture.{ITextureObject, TextureUtil}
import org.lwjgl.opengl.GL11

import scala.collection.immutable.HashMap
import scala.collection.mutable.ListBuffer

object IconManager {

  private var iconTextureList: HashMap[IconData, ITextureObject] = new HashMap[IconData, ITextureObject]

  var iconList: ListBuffer[IconData] = new ListBuffer[IconData]()

  def bindTexture(iconData: IconData) {
    if (iconData.resourceLocation != null) {
      Minecraft.getMinecraft.getTextureManager.bindTexture(iconData.resourceLocation)
    }
    else {
      val textureOption: Option[ITextureObject] = this.iconTextureList.get(iconData)
      var textureObject:ITextureObject = null

      if (textureOption.isEmpty) {
        val iconFile: File = new File(iconData.textureName)
        textureObject = new IconTexture(iconFile)
        this.loadTexture(iconData, textureObject)
      }
      else {
        textureObject = textureOption.get
      }

      GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureObject.getGlTextureId)
    }
  }

  def loadTexture(iconData: IconData, textureObject: ITextureObject) {
    var textureObjectCopy: ITextureObject = textureObject
    try {
      textureObject.loadTexture(null)
    }
    catch {
      case ioexception: IOException =>
        MarkerBeacons.logger.warn("Failed to load icon texture: " + iconData, ioexception)
        textureObjectCopy = TextureUtil.missingTexture
        this.iconTextureList += (iconData -> textureObjectCopy)
    }
    this.iconTextureList += (iconData -> textureObjectCopy)
  }
}
