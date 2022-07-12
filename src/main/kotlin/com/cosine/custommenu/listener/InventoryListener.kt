package com.cosine.custommenu.listener

import com.cosine.custommenu.gui.InventoryGui
import com.cosine.custommenu.main.CustomMenu
import com.cosine.custommenu.util.CustomConfig
import com.cosine.custommenu.util.CustomMenuValue
import org.bukkit.ChatColor
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

class InventoryListener(plugin: CustomMenu) : Listener {

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
    fun onInventoryClick(event: InventoryClickEvent) {
        val player: Player = event.whoClicked as Player
        val inventory = player.openInventory.topInventory

        if (event.inventory == null) return

        if (inventory.name.contains("§a§a§a")) {
            if (event.currentItem == null) return
            event.isCancelled = true

            val name = getMenuName(inventory.name)

            when (event.rawSlot) {
                10 -> {
                    inventoryGui.settingItemMenu(player, name)
                }
                12 -> {
                    value.npc[player.uniqueId] = name
                    player.sendMessage("$option 상점이 열릴 NPC의 이름을 적어주세요.")
                }
                14 -> {
                    changeRow(player, name, 6, 1, "늘릴", event.isLeftClick)
                    changeRow(player, name, 1, -1, "줄일", event.isRightClick)
                }
                16 -> {
                    inventoryGui.settingSlotMenu(player, name)
                }
            }
        }
        if (inventory.name.contains("§c§c§c")) {
            if (event.currentItem == null) return
            event.isCancelled = true

            val name = getMenuName(inventory.name)

            if (event.isLeftClick) {
                value.clickSlot[player.uniqueId] = event.rawSlot
                if (event.isShiftClick) { // 로어 추가
                    value.addLore[player.uniqueId] = name
                    player.sendMessage("$option 로어에 추가할 문장을 적어주세요.")
                    return
                }
                // 디스플레이 설정
                value.changeDisplay[player.uniqueId] = name
                player.sendMessage("$option 디스플레이로 설정할 이름을 적어주세요.")
            }
            if (event.isRightClick) {
                value.clickSlot[player.uniqueId] = event.rawSlot
                if (event.isShiftClick) { // 로어 제거
                    value.removeLore[player.uniqueId] = name
                    player.sendMessage("$option 제거할 로어의 라인을 적어주세요.")
                    return
                }
                // 로어 수정
                value.changeLore[player.uniqueId] = name
                player.sendMessage("$option 수정할 로어의 라인을 적어주세요.")
            }
        }
    }
    @EventHandler
    fun onInventoryClose(event: InventoryCloseEvent) {
        val player: Player = event.player as Player
        val inventory: Inventory = event.inventory

        if (inventory.name.contains("§b§b§b")) {
            val name = getMenuName(inventory.name)
            val row = menuConfig.getInt("$name.라인")

            settingItem(name, row, inventory)

            player.sendMessage("$option 아이템 설정이 완료되었습니다.")
        }
    }
    private fun settingItem(name: String, row: Int, inventory: Inventory) {
        for (loop: Int in 0..row * 9) {
            val item: ItemStack? = inventory.getItem(loop)

            if (item == null) {
                if (menuConfig.contains("$name.슬롯.$loop")) {
                    menuConfig.set("$name.슬롯.$loop", null)
                }
                continue
            }
            menuConfig.set("$name.슬롯.$loop.명령어.활성화", false)
            menuConfig.set("$name.슬롯.$loop.명령어.커맨드", "/명령어를 입력해주세요")
            menuConfig.set("$name.슬롯.$loop.아이템", item)
        }
        menu.saveConfig()
    }
    private fun changeRow(player: Player, name: String, prevent: Int, choice: Int, select: String, boolean: Boolean) {
        var row = menuConfig.getInt("$name.라인")
        if (boolean) {
            if (row == prevent) {
                player.sendMessage("$option 더 이상 $select 수 없습니다.")
                return
            }
            row = (row + choice)
            menuConfig.set("$name.라인", row)
            menu.saveConfig()
            return
        }
    }
    private fun getMenuName(name: String): String {
        val color = ChatColor.stripColor(name)
        val space = color.replace(" ", "")
        return space.replace("설정", "")
    }
}