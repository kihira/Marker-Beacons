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

package kihira.markerbeacons.client.render

import kihira.markerbeacons.common._
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.OpenGlHelper
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.tileentity.TileEntity
import org.lwjgl.opengl.GL11

import scala.collection.JavaConversions._

object TileEntityMarkerBeaconRenderer extends TileEntitySpecialRenderer {

  override def renderTileEntityAt(tileEntity: TileEntity, x: Double, y: Double, z: Double, partialTicks: Float): Unit = {
    val beaconData: BeaconData = tileEntity.asInstanceOf[TileEntityMarkerBeacon].beaconData
    if (beaconData != null && !tileEntity.getWorldObj.isBlockIndirectlyGettingPowered(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord)) {
      val player: EntityPlayer = Minecraft.getMinecraft.thePlayer
      val xDiff: Double = player.posX - 0.5F - tileEntity.xCoord
      val zDiff: Double = player.posZ - 0.5F - tileEntity.zCoord
      val yDiff: Double = (player.posY + player.getEyeHeight) - (tileEntity.yCoord + beaconData.height)
      val d3: Double = Math.sqrt(xDiff * xDiff + zDiff * zDiff)
      val yAngle: Float = (-(Math.atan2(yDiff, d3) * 180.0D / Math.PI)).asInstanceOf[Float]
      val xAngle: Float = (Math.atan2(zDiff, xDiff) * 180.0D / Math.PI).asInstanceOf[Float]

      //Sets the lighting/brightness
      val i: Int = 15728880
      OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (i % 65536).asInstanceOf[Float] / 1F, (i / 65536).asInstanceOf[Float] / 1F)

      GL11.glPushMatrix()
      GL11.glDisable(GL11.GL_LIGHTING)
      GL11.glDisable(GL11.GL_CULL_FACE)
      GL11.glEnable(GL11.GL_BLEND)
      GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)

      for (i <- 1 to beaconData.count) {
        for (component: LogoComponent <- beaconData.components) {
          if (component != null) {
            GL11.glPushMatrix()
            GL11.glColor4f(1F, 1F, 1F, 1F)

            GL11.glTranslated(x + 0.5d, y + beaconData.height, z + 0.5d)
            if (beaconData.facePlayerX) GL11.glRotatef(-xAngle - 90F, 0.0F, 1.0F, 0.0F)
            else if (beaconData.rotationSpeed != 0) GL11.glRotatef(((System.nanoTime() / 10000000F) / beaconData.rotationSpeed) + (i * (360 / beaconData.count)), 0F, 1F, 0F)
            else GL11.glRotatef(beaconData.angleX + (i * (360 / beaconData.count)), 0F, 1F, 0F)
            GL11.glRotatef(180F, 0F, 0F, 1F)
            GL11.glTranslatef(0F, 0F, -beaconData.offset)
            if (beaconData.facePlayerY) GL11.glRotatef(yAngle, 1.0F, 0.0F, 0.0F)
            else GL11.glRotatef(beaconData.angleY, 1.0F, 0.0F, 0.0F)
            GL11.glScalef(0.5F, 0.5F, 0.5F)
            GL11.glScalef(beaconData.scale, beaconData.scale, 0)

            GL11.glTranslatef(component.xOffset, component.yOffset, component.zOffset)
            GL11.glScalef(component.scale, component.scale, component.scale)
            component.drawComponent()
            GL11.glColor4f(1F, 1F, 1F, 1F)
            GL11.glPopMatrix()
          }
        }
      }
      GL11.glColor4f(1F, 1F, 1F, 1F)
      GL11.glEnable(GL11.GL_LIGHTING)
      GL11.glEnable(GL11.GL_CULL_FACE)
      GL11.glDisable(GL11.GL_BLEND)
      GL11.glPopMatrix()
    }
  }
}
