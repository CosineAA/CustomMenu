package com.cosine.custommenu.command

import com.cosine.custommenu.gui.InventoryGui
import com.cosine.custommenu.main.CustomMenu
import com.cosine.custommenu.util.CustomConfig
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player

class OpCommand(plugin: CustomMenu) : CommandExecutor {

    private val menu: CustomConfig
    private val menuConfig: YamlConfiguration
    private val inventoryGui: InventoryGui

    init {
        menu = plugin.getCustomConfig()
        menuConfig = menu.getConfig()
        inventoryGui = plugin.getInventoryGui()
    }

    private val option = "§c§l[ 메뉴 ] §f§l"

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender is Player) {
            val player: Player = sender
            if (!player.isOp) return false
            if (args.isEmpty()) {
                help(player)
                return false
            }
            when (args[0]) {
                "생성" -> {
                    if (args.size == 1) {
                        player.sendMessage("$option 생성할 메뉴의 이름을 적어주세요.")
                        return false
                    }
                    if (menuConfig.contains(args[1])) {
                        player.sendMessage("$option 이미 존재하는 메뉴입니다.")
                        return false
                    }
                    createMenu(args[1])
                    player.sendMessage("$option 메뉴를 생성하였습니다.")
                }
                "제거" -> {
                    checkMenuExists(player, args)
                    removeMenu(args[1])
                    player.sendMessage("$option 메뉴를 제거하였습니다.")
                }
                "설정" -> {
                    checkMenuExists(player, args)
                    inventoryGui.settingMainMenu(player, args[1])
                }
            }
        }
        return false
    }
    private fun checkMenuExists(player: Player, args: Array<out String>) {
        if (args.size == 1) {
            player.sendMessage("$option 제거할 메뉴의 이름을 적어주세요.")
            return
        }
        if (!menuConfig.contains(args[1])) {
            player.sendMessage("$option 존재하지 않는 메뉴입니다.")
            return
        }
    }
    private fun createMenu(name: String) {
        menuConfig.set("$name.라인", 6)
        menuConfig.set("$name.이름", "메뉴 이름")
        menuConfig.set("$name.NPC", "NPC 이름")
        menuConfig.set("$name.슬롯", 0)
        menu.saveConfig()
    }
    private fun removeMenu(name: String) {
        menuConfig.set(name, null)
        menu.saveConfig()
    }
    private fun help(player: Player) {
        player.sendMessage("$option 메뉴 시스템 도움말")
        player.sendMessage("")
        player.sendMessage("$option §f/메뉴 생성 [이름]")
        player.sendMessage("$option §f/메뉴 제거 [이름]")
        player.sendMessage("$option §f/메뉴 설정 [이름]")
        player.sendMessage("$option §f/메뉴 열기 [이름]")
        player.sendMessage("$option §f/메뉴 목록")
    }
}