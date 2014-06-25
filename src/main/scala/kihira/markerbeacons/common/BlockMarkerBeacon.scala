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
import net.minecraft.block.material.Material
import net.minecraft.block.{Block, ITileEntityProvider}
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.IIcon
import net.minecraft.world.World

object BlockMarkerBeacon extends Block(Material.rock) with ITileEntityProvider {
  setCreativeTab(CreativeTabs.tabDecorations)
  setHardness(5F)
  setResistance(10.0F)
  setLightLevel(1.0F)
  setStepSound(Block.soundTypeMetal)
  setBlockName("blockMarkerBeacon")
  setBlockTextureName(Beacons.MOD_ID + ":markerbeacon")

  @SideOnly(Side.CLIENT) var topIcon: IIcon = null

  override def createNewTileEntity(var1: World, var2: Int): TileEntity = new TileEntityMarkerBeacon

  @SideOnly(Side.CLIENT) override def getIcon(side : Int, meta : Int): IIcon = if (side == 1) this.topIcon else this.blockIcon

  @SideOnly(Side.CLIENT) override def registerBlockIcons(iconRegister : IIconRegister) {
    this.blockIcon = iconRegister.registerIcon(this.getTextureName + "_side")
    this.topIcon = iconRegister.registerIcon(this.getTextureName + "_top")
  }

  override def onBlockActivated(world : World, xPos : Int, yPos : Int, zPos : Int, entityPlayer : EntityPlayer, metadata : Int, p_149727_7_ : Float, p_149727_8_ : Float, p_149727_9_ : Float): Boolean = {
    if (world.isRemote) {
      entityPlayer.openGui(Beacons, 0, world, xPos, yPos, zPos)
    }
    true
  }
}
