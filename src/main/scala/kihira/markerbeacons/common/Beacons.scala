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

import java.io._

import com.google.gson.stream.{JsonReader, JsonWriter}
import com.google.gson.{Gson, GsonBuilder}
import cpw.mods.fml.common.event.FMLPreInitializationEvent
import cpw.mods.fml.common.network.NetworkRegistry
import cpw.mods.fml.common.registry.GameRegistry
import cpw.mods.fml.common.{Mod, SidedProxy}
import kihira.markerbeacons.client.icon.{IconData, IconManager}
import kihira.markerbeacons.proxy.CommonProxy
import org.apache.commons.io.IOUtils
import org.apache.logging.log4j.{LogManager, Logger}

@Mod(modid = Beacons.MOD_ID, name = "Marker Beacons", version = "$version", modLanguage = "scala")
object Beacons {

  final val MOD_ID = "markerbeacons"
  final val logger: Logger = LogManager.getLogger(MOD_ID)

  @SidedProxy(clientSide = "kihira.markerbeacons.proxy.ClientProxy", serverSide = "kihira.markerbeacons.proxy.CommonProxy")
  var proxy: CommonProxy = null

  @Mod.EventHandler
  def onPreInit(e: FMLPreInitializationEvent) {
    GameRegistry.registerBlock(BlockMarkerBeacon, "blockMarkerBeacon")
    GameRegistry.registerTileEntity(classOf[TileEntityMarkerBeacon], Beacons.MOD_ID + ":markerBeacon")

    proxy.registerRenderers()
    NetworkRegistry.INSTANCE.registerGuiHandler(Beacons, GuiHandler)

    if (e.getSide.isClient) loadIcons()
  }

  def loadIcons() {
    val iconsDir: File = new File(Beacons.MOD_ID)
    val iconsFile: File = new File(iconsDir + File.separator + "icons.json")
    val gson: Gson = new GsonBuilder().setPrettyPrinting().create()

    try {
      if (!iconsDir.exists) {
        iconsDir.mkdirs() //Make the missing directories
      }
      if (!iconsFile.exists) {
        iconsFile.createNewFile() //Make the missing file

        val fileWriter: FileWriter = new FileWriter(iconsFile)
        val inputStream: InputStream = Beacons.getClass.getResourceAsStream("/assets/markerbeacons/icons.json")

        try {
          IOUtils.write(IOUtils.toString(inputStream), fileWriter)
        } catch {
          case e: IOException => e.printStackTrace()
          case e: NullPointerException => e.printStackTrace()
        } finally {
          if (inputStream != null) inputStream.close()
          if (fileWriter != null) fileWriter.close()
        }
      }

      val reader: JsonReader = new JsonReader(new FileReader(iconsFile))
      reader.beginArray()
      while (reader.hasNext) {
        val iconData: IconData = gson.fromJson(reader, classOf[IconData])
        IconManager.iconList += iconData
        println(iconData)
      }
      reader.endArray()
      reader.close()
    }
    catch {
      case e1: IOException =>
        e1.printStackTrace()
    }
  }
}
