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

package kihira.markerbeacons.proxy

import cpw.mods.fml.client.registry.ClientRegistry
import cpw.mods.fml.relauncher.{Side, SideOnly}
import kihira.markerbeacons.client.render.TileEntityMarkerBeaconRenderer
import kihira.markerbeacons.common.TileEntityMarkerBeacon

@SideOnly(Side.CLIENT)
class ClientProxy extends CommonProxy {

  override def registerRenderers() {
    ClientRegistry.bindTileEntitySpecialRenderer(classOf[TileEntityMarkerBeacon], TileEntityMarkerBeaconRenderer)
  }
}
