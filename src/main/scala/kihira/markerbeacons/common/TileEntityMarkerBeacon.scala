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

import net.minecraft.nbt.NBTTagCompound
import net.minecraft.network.play.server.S35PacketUpdateTileEntity
import net.minecraft.network.{NetworkManager, Packet}
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.AxisAlignedBB

class TileEntityMarkerBeacon extends TileEntity {

  var beaconData: BeaconData = new BeaconData

  override def readFromNBT(nbtTagCompound: NBTTagCompound) {
    super.readFromNBT(nbtTagCompound)

    if (nbtTagCompound.hasKey("BeaconData")) {
      this.beaconData = MarkerBeacons.gson.fromJson(nbtTagCompound.getString("BeaconData"), classOf[BeaconData])
    }
  }

  override def writeToNBT(nbtTagCompound: NBTTagCompound) {
    super.writeToNBT(nbtTagCompound)

    nbtTagCompound.setString("BeaconData", MarkerBeacons.gson.toJson(this.beaconData))
  }

  override def onDataPacket(net: NetworkManager, pkt: S35PacketUpdateTileEntity) {
    this.readFromNBT(pkt.func_148857_g)
    this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord)
  }

  override def getDescriptionPacket: Packet = {
    val tag: NBTTagCompound = new NBTTagCompound
    this.writeToNBT(tag)
    new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 0, tag)
  }

  override def shouldRenderInPass(pass: Int): Boolean = {
    pass == 0
  }

  override def getRenderBoundingBox: AxisAlignedBB = {
    TileEntity.INFINITE_EXTENT_AABB
  }

  //We don't need to tick as this is more just storing data
  override def canUpdate: Boolean = false
}
