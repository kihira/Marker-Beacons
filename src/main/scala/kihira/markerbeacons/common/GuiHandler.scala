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

import cpw.mods.fml.common.network.IGuiHandler
import kihira.markerbeacons.client.gui.GuiMarkerBeacon
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.world.World

object GuiHandler extends IGuiHandler {

  override def getServerGuiElement(ID: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): AnyRef = null

  override def getClientGuiElement(ID: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): AnyRef = {
    ID match {
      case 0 => new GuiMarkerBeacon(world.getTileEntity(x, y, z).asInstanceOf[TileEntityMarkerBeacon])
    }
  }
}
