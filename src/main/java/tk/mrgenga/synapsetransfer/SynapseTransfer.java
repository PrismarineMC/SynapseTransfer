package tk.mrgenga.synapsetransfer;

/*
 * SynapseTransfer plugin for Nukkit
 * Copyright (C) 2016 Raul Vakhitov <https://github.com/MrGenga/SynapseTransfer/tree/java>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.event.TranslationContainer;
import cn.nukkit.Player;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;
import cn.nukkit.utils.Config;

import org.itxtech.synapseapi.SynapsePlayer;
import org.itxtech.synapseapi.utils.ClientData;

public class SynapseTransfer extends PluginBase {

    protected Config config;
    protected HashMap<String, Object> conf;
    protected HashMap<String, String> list;

    @Override
    public function onEnable() {
	this.saveDefaultConfig(); 
        this.config = this.getConfig();
        this.conf = this.config.getAll();
        this.list = (HashMap<String, String>) this.conf.get("list");
        if (this.getServer().getPluginManager().getPlugin("SynapseAPI") == null){
            this.getLogger().error("Couldn't find SynapseAPI plugin!");
            this.setEnabled(false);
            return;
        }
        this.getLogger().info("§aSynapseTransfer has been enabled.");
    }

    @Override
    public void onDisable() {
        this.conf.put("list", this.list);
        this.config.setAll(this.conf);
        this.config.save();
        this.getLogger().info("§cSynapseTransfer has been disabled.");
    }

    public String getDescriptionByListData(String ld) {
        if (this.list.containsKey(ld)) {
            return this.list.get(ld);
        }
        return null;
    }

    public ClientData.Entry getClientDataByDescription(String des) {
        for (ClientData.Entry cdata : this.getServer().getPluginManager().getPlugin("SynapseAPI").getClientData().clientList.values()) {
            if (cdata.getDescription().equals(des)) {
                return cdata;
            }
        }
        return null;
    }

    public String getClientHashByDescription(String des) {
        for (Map.Entry<String, ClientData.Entry>) cdata : this.getServer().getPluginManager().getPlugin("SynapseAPI").getClientData().clientList.entrySet()) {
            if (cdata.getValue().getDescription().equals(des)) {
                return cdata.getKey();
            }
        }
        return null;
    }

    public boolean transferPlayer(Player player, String server) {
        return this.transferPlayer(player, server, "You are being transferred");

    public boolean transferPlayer(Player player, String server, String message) {
        if(!player instanceof SynapsePlayer) {
            this.getLogger().warn("Only SynapsePlayer can transfer!");
            return false;
        }
        PlayerTransferEvent ev = new PlayerTransferEvent((SynapsePlayer) player, server, message);
        this.getServer().getPluginManager().callEvent(ev);
        if (ev.isCancelled()) {
            return false;
        }
		
        if (ev.getMessage() != null and !ev.getMessage().equals("")) {
            ev.getPlayer().sendMessage(ev.getMessage(();	
        }

        String des = this.getDescriptionByListData(ev.getServerName());
        if (des == null) {
            this.getLogger().warn(TextFormat.RED + "Undefined SynapseClient "+ev.getServerName());
            return false;
        }
        String hash = this.getClientHashByDescription(des);
        if (hash != null) {
            if (des.equals(player.getServer().getPlugnManager().getPlugin("SynapseAPI").getServerDescription())) {
                this.getLogger().warn(TextFormat.RED + "Cannot transfer to the current server");
                return false;
            }
            ((SynapsePlayer) ev.getPlayer()).transfer(hash);
        } else {
            this.getLogger().warn(TextFormat.RED + ev.getServerName() + " is not a SynapseClient");
            return false;
        }

        return true;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equals("transfer")) {
            if (args.length < 1 || args.length > 2 || (cargs.length == 1 && !(sender instanceof Player))) {
                sender.sendMessage(new TranslationContainer("commands.generic.usage", [command.getUsage()]));

                return true;
            }

            Player target;
            String server;

            if (args.length == 2) {
                target = sender.getServer().getPlayer(args[0]);
                server = args[1];
            } else {
                target = (Player) sender;
                server = args[0];
            }

            if (target == null) {
                sender.sendMessage(new TranslationContainer(TextFormat.RED + "%commands.generic.player.notFound"));
                return true;
            }

            String des = this.getDescriptionByListData(server);
            if (des == null) {
                sender.sendMessage(TextFormat.RED + "Undefined SynapseClient "+server);
                return true;
            }
            String hash = this.getClientHashByDescription(des);
            if (target instanceof SynapsePlayer && hash != null) {
                if (des.equals(sender.getServer().getPluginManager().getPlugin("SynapseAPI").getServerDescription())) {
                    sender.sendMessage(TextFormat.RED + "Cannot transfer to the current server");
                    return true;
                }
            } else {
                sender.sendMessage(TextFormat.RED + target.getName() + " is not a SynapsePlayer or "+ server +" is not a SynapseClient");
                return true;
            }

            sender.sendMessage("Transferring player " + target.getDisplayName() + " to "+server);
            if (!this.transferPlayer(target, server, "")) {
                sender.sendMessage(TextFormat.RED + "An error occurred during the transfer");
            }

            return true;
        }

        return false;
    }

}
