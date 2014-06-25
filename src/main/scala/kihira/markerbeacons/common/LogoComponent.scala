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

import cpw.mods.fml.relauncher.{Side, SideOnly}

abstract class LogoComponent {

  var title: String
  var xOffset: Float = 0
  var yOffset: Float = 0
  var zOffset: Float = 0
  var scale: Float = 1
  var clazz: String = getClass.getCanonicalName

  @SideOnly(Side.CLIENT) def drawComponent()
}
