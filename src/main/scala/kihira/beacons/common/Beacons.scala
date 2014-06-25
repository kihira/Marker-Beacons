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

import java.io.{FileWriter, File, FileReader, IOException}

import com.google.gson.stream.{JsonWriter, JsonReader}
import com.google.gson.{Gson, GsonBuilder}
import cpw.mods.fml.common.event.FMLPreInitializationEvent
import cpw.mods.fml.common.network.NetworkRegistry
import cpw.mods.fml.common.registry.GameRegistry
import cpw.mods.fml.common.{Mod, SidedProxy}
import kihira.beacons.client.icon.{IconData, IconManager}
import kihira.beacons.proxy.CommonProxy
import net.minecraft.creativetab.CreativeTabs
import org.apache.logging.log4j.{Logger, LogManager}

@Mod(modid = Beacons.MOD_ID, name = "Marker Beacons", version = "$version", modLanguage = "scala")
object Beacons {

  final val MOD_ID = "markerbeacons"
  final val logger: Logger = LogManager.getLogger(MOD_ID)

  @SidedProxy(clientSide = "kihira.beacons.proxy.ClientProxy", serverSide = "kihira.beacons.proxy.CommonProxy")
  var proxy: CommonProxy = null

  @Mod.EventHandler
  def onPreInit(e: FMLPreInitializationEvent) {
    GameRegistry.registerBlock(MarkerBeaconBlock.setCreativeTab(CreativeTabs.tabBlock), "beacon")
    GameRegistry.registerTileEntity(classOf[TileEntityMarkerBeacon], "beacons:beacon")

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
        iconsFile.setWritable(true)
        iconsFile.setReadable(true)
        iconsDir.mkdirs() //Make the missing directories
      }
      if (!iconsFile.exists) {
        iconsFile.setWritable(true)
        iconsFile.setReadable(true)
        iconsFile.createNewFile() //Make the missing file

        val jsonWriter: JsonWriter = new JsonWriter(new FileWriter(iconsFile))
        jsonWriter.beginArray()
        gson.toJson(gson.toJsonTree(new IconData), jsonWriter)
        jsonWriter.endArray()
        jsonWriter.close()
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
