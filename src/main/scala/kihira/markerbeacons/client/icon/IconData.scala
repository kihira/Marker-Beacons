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

import net.minecraft.util.ResourceLocation

class IconData {

  var iconName: String = ""
  var textureName: String = ""
  var minU: Int = 0
  var minV: Int = 0
  var maxU: Int = 0
  var maxV: Int = 0
  var width: Int = 0
  var height: Int = 0
  var textureXSize: Int = 256
  var textureYSize: Int = 256

  def resourceLocation: ResourceLocation = {
    var resource: ResourceLocation = null
    if (this.textureName.contains(":")) {
      val split: Array[String] = this.textureName.split(":")
      if (split.length == 2) resource = new ResourceLocation(split(0), split(1))
    }
    resource
  }
}
