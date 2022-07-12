package com.cosine.custommenu.listener

import com.cosine.custommenu.gui.InventoryGui
import com.cosine.custommenu.main.CustomMenu
import com.cosine.custommenu.util.CustomConfig
import com.cosine.custommenu.util.CustomMenuValue
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.inventory.ItemStack

class ChatListener(plugin: CustomMenu) : Listener {

    private val menu: CustomConfig
    private val menuConfig: YamlConfiguration
    private val inventoryGui: InventoryGui
    private val value: CustomMenuValue

    init {
        menu = plugin.getCustomConfig()
        menuConfig = menu.getConfig()
        inventoryGui = plugin.getInventoryGui()
        value = plugin.getCustomMenuValue()
    }

    private val option = "§c§l[ 메뉴 ] §f§l"

    @EventHandler
    fun chat(event: AsyncPlayerChatEvent) {
        val player = event.player
        val message = event.message

        if (value.npc.containsKey(player.uniqueId)) {
            event.isCancelled = true

            val name = value.npc[player.uniqueId]

            menuConfig.set("$name.NPC", message)
            menu.saveConfig()

            value.npc.remove(player.uniqueId)

            player.sendMessage("$option NPC 설정이 완료되었습니다.")
        }
        if (value.addLore.containsKey(player.uniqueId)) {
            event.isCancelled = true

            val name = value.npc[player.uniqueId]
            val slot = value.clickSlot[player.uniqueId]

            val item = ItemStack(menuConfig.getItemStack("$name.슬롯.$slot.아이템"))
            val meta = item.itemMeta
            val lore = item.itemMeta.lore

            lore?.add(convertColor(event.message))
            item.itemMeta = meta

            menuConfig.set("$name.슬롯.$slot.아이템", item)
            menu.saveConfig()

            value.addLore.remove(player.uniqueId)

            player.sendMessage("$option 로어 추가가 완료되었습니다.")
        }
    }
    private fun convertColor(message: String) : String {
        return message.replace("&", "§")
    }
    private fun isInt(str: String): Boolean {
        return try {
            str.toInt()
            true
        } catch (e: NumberFormatException) {
            false
        }
    }
}