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

import com.google.gson.Gson
import cpw.mods.fml.common.FMLCommonHandler
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.common.network.FMLNetworkEvent.ServerCustomPacketEvent
import cpw.mods.fml.common.network.internal.FMLProxyPacket
import cpw.mods.fml.common.network.{ByteBufUtils, FMLEventChannel, NetworkRegistry}
import io.netty.buffer.{ByteBuf, Unpooled}
import net.minecraft.world.World
import org.apache.logging.log4j.Level

object PacketHandler {

  val eventChannel: FMLEventChannel = NetworkRegistry.INSTANCE.newEventDrivenChannel("beacons")
  eventChannel.register(this)

  @SubscribeEvent
  def onServerPacket(event: ServerCustomPacketEvent) {
    event.packet.payload().readByte() match {
      case 0 => handleBeaconDataPacket(event.packet.payload())
    }
  }

  private def handleBeaconDataPacket(payload: ByteBuf) {
    val dimID: Int = payload.readInt()
    val x: Int = payload.readInt()
    val y: Int = payload.readInt()
    val z: Int = payload.readInt()
    val beaconDataJson: String = ByteBufUtils.readUTF8String(payload)
    val world: World = FMLCommonHandler.instance().getMinecraftServerInstance.worldServerForDimension(dimID)

    if (world != null && world.getTileEntity(x, y, z).isInstanceOf[TileEntityMarkerBeacon]) {
      val tileEntity: TileEntityMarkerBeacon = world.getTileEntity(x, y, z).asInstanceOf[TileEntityMarkerBeacon]
      tileEntity.beaconDataJson = beaconDataJson
      tileEntity.markDirty()
    }
    else {
      Beacons.logger.log(Level.WARN, "Failed to update Beacon Data at (%s) %s, %s, %s with %s", Array(dimID, x, y, z, beaconDataJson))
    }
  }

  def createBeaconDataPacket(dimID: Int, x: Int, y: Int, z: Int, beaconData: BeaconData): FMLProxyPacket = {
    val payload: ByteBuf = Unpooled.buffer()

    payload.writeByte(0)
    payload.writeInt(dimID)
    payload.writeInt(x)
    payload.writeInt(y)
    payload.writeInt(z)
    ByteBufUtils.writeUTF8String(payload, new Gson().toJson(beaconData))

    new FMLProxyPacket(payload, "beacons")
  }
}
