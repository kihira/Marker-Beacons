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

import net.minecraft.block.{ITileEntityProvider, Block}
import net.minecraft.block.material.Material
import net.minecraft.world.World
import net.minecraft.tileentity.{TileEntityBeacon, TileEntity}
import net.minecraft.entity.player.EntityPlayer
import cpw.mods.fml.common.network.NetworkRegistry

object MarkerBeaconBlock extends Block(Material.rock) with ITileEntityProvider {

  override def createNewTileEntity(var1: World, var2: Int): TileEntity = new TileEntityMarkerBeacon

  override def onBlockActivated(world : World, xPos : Int, yPos : Int, zPos : Int, entityPlayer : EntityPlayer, metadata : Int, p_149727_7_ : Float, p_149727_8_ : Float, p_149727_9_ : Float): Boolean = {
    if (world.isRemote) {
      entityPlayer.openGui(Beacons, 0, world, xPos, yPos, zPos)
      return true
    }
    false
  }
}
