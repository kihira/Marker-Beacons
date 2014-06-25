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

package kihira.markerbeacons.client.gui

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiButton
import net.minecraft.util.MathHelper
import org.lwjgl.opengl.GL11

import scala.math.BigDecimal.RoundingMode

class GuiSlider(id: Int, xPos: Int, yPos: Int, width: Int, height: Int, text: String, minValue: Float, maxValue: Float, var currentValue: Float, decimal: Int) extends GuiButton(id, xPos, yPos, width, height, text) {
  def this(id: Int, xPos: Int, yPos: Int, text: String, minValue: Float, maxValue: Float, currentValue: Float, decimal: Int) = this(id, xPos, yPos, 200, 20, text, minValue, maxValue, currentValue, decimal)
  this.displayString = text + ":" + String.valueOf(this.currentValue)

  var sliderValue: Float = MathHelper.clamp_float((this.currentValue - this.minValue) / (this.maxValue - this.minValue), 0.0F, 1.0F)
  var dragging: Boolean = false

  override def mouseDragged(minecraft: Minecraft, xPos: Int, yPos: Int) {
    if (this.visible) {
      if (this.dragging) {
        this.updateValues(xPos, yPos)
      }

      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F)
      this.drawTexturedModalRect(this.xPosition + (this.sliderValue * (this.width - 8).asInstanceOf[Float]).asInstanceOf[Int], this.yPosition, 0, 66, 4, 20)
      this.drawTexturedModalRect(this.xPosition + (this.sliderValue * (this.width - 8).asInstanceOf[Float]).asInstanceOf[Int] + 4, this.yPosition, 196, 66, 4, 20)
    }
  }

  override def mousePressed(minecraft : Minecraft, xPos : Int, yPos : Int): Boolean = {
    if (super.mousePressed(minecraft, xPos, yPos)) {
      this.updateValues(xPos, yPos)
      this.dragging = true
      true
    }
    else {
      false
    }
  }

  protected override def getHoverState(p_146114_1_ : Boolean): Int = 0

  override def mouseReleased(p_146118_1_ : Int, p_146118_2_ : Int) {
    this.dragging = false
  }

  private def updateValues(xPos: Int, yPos: Int) {
    this.sliderValue = MathHelper.clamp_float((xPos - (this.xPosition + 4)) / (this.width - 8).asInstanceOf[Float], 0F, 1F)

    this.currentValue = BigDecimal(this.sliderValue * (maxValue - minValue) + minValue).setScale(this.decimal, RoundingMode.HALF_EVEN).toFloat
    this.displayString = text + ":" + String.valueOf(this.currentValue)
  }
}
