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

import java.util

import cpw.mods.fml.relauncher.{Side, SideOnly}

@SideOnly(Side.CLIENT) class BeaconData {
  var facePlayerY: Boolean = false
  var facePlayerX: Boolean = false

  var height: Float = 3F
  var rotationSpeed: Float = 2F
  var angleY: Float = 0F
  var angleX: Float = 0F
  var count: Int = 1
  var offset: Float = 2F
  var scale: Float = 1F
  
  var components: util.List[LogoComponent] = new util.ArrayList[LogoComponent]()
}
