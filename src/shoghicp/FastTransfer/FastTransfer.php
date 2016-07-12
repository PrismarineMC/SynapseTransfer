<?php

/*
 * SynapseTransfer plugin for Genisys
 * Copyright (C) 2016 Raul Vakhitov <https://github.com/MrGenga/SynapseTransfer>
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

namespace MrGenga\SynapseTransfer;

use pocketmine\command\Command;
use pocketmine\command\CommandSender;
use pocketmine\event\TranslationContainer;
use pocketmine\Player;
use pocketmine\plugin\PluginBase;
use pocketmine\utils\TextFormat;

use synapse\Player as SynapsePlayer;

class SynapseTransfer extends PluginBase{

    protected $config;

    public function onEnable(){
		@mkdir($this->getDataFolder());
		$this->config = new Config($this->getDataFolder() . "config.yml", Config::YAML, [
			"list" => [
				"hub" => "hub"
			]
		]);
		$this->conf = $this->config->getAll();
		$this->list = $this->conf["list"];
		if(!$this->getServer()->isSynapseEnabled()){
			$this->getLogger()->error("Synapse Client service has been disabled, this plugin won't work!");
			$this->setEnabled(false);
			return;
		}
		$this->getLogger()->info("§aSynapseTransfer has been enabled.");
	}

	public function onDisable(){
		$this->conf["list"] = $this->list;
		$this->config->setAll($this->conf);
		$this->config->save();
		$this->getLogger()->info("§cSynapseTransfer has been disabled.");
	}

	/**
	 * @param string $ld
	 * @return null|string
	 */
	public function getDescriptionByListData(string $ld){
		if(isset($this->list[$ld])){
			return $this->list[$ld];
		}
		return null;
	}

	/**
	 * @param $des
	 * @return array|null
	 */
	public function getClientDataByDescription(string $des){
		foreach($this->getServer()->getSynapse()->getClientData() as $cdata){
			if($cdata["description"] == $des){
				return $cdata;
			}
		}
		return null;
	}

	/**
	 * @param string $des
	 * @return null|string
	 */
	public function getClientHashByDescription(string $des){
		foreach($this->getServer()->getSynapse()->getClientData() as $hash => $cdata){
			if($cdata["description"] == $des){
				return $hash;
			}
		}
		return null;
	}

	/**
	 * Will transfer a connected player to another server.
	 * This will trigger PlayerTransferEvent
	 *
	 * Player transfer works only if he has connected from Synapse
	 *
	 * @param Player $player
	 * @param string $address
	 * @param int    $port
	 * @param string $message If null, ignore message
	 *
	 * @return bool
	 */
	public function transferPlayer(Player $player, $server, $message = "You are being transferred"){
                if(!$player instanceof SynapsePlayer){
                    $this->getLogger()->warn("Only SynapsePlayer can transfer!");
                    return false;
                 }
		$ev = new PlayerTransferEvent($player, $server, $message);
		$this->getServer()->getPluginManager()->callEvent($ev);
		if($ev->isCancelled()){
			return false;
		}
		
		if($message !== null and $message !== ""){
			$player->sendMessage($message);	
		}

		$des = $this->getDescriptionByListData($server);
		if($des == null){
			$this->getLogger()->warn(TextFormat::RED . "Undefined SynapseClient $server");
			return false;
		}
		if(($hash = $this->getClientHashByDescription($des)) != null){
			if($des == $sender->getServer()->getSynapse()->getDescription()){
				$this->getLogger()->warn(TextFormat::RED . "Cannot transfer to the current server");
				return false;
			}
			$player->transfer($hash);
		}else{
			$this->getLogger()->warn(TextFormat::RED . "$server is not a SynapseClient");
		}

		return true;
	}


	public function onCommand(CommandSender $sender, Command $command, $label, array $args){
		if($label === "transfer"){
			if(count($args) < 1 or count($args) > 2 or (count($args) === 1 and !($sender instanceof Player))){
				$sender->sendMessage(new TranslationContainer("commands.generic.usage", [$command->getUsage()]));

				return true;
			}

			/** @var Player $target */
			$target = $sender;

			if(count($args) === 2){
				$target = $sender->getServer()->getPlayer($args[0]);
				$server = $args[1];
			}else{
				$server = $args[0];
			}

			if($target === null){
				$sender->sendMessage(new TranslationContainer(TextFormat::RED . "%commands.generic.player.notFound"));
				return true;
			}

			$des = $this->getDescriptionByListData($server);
			if($des == null){
				$sender->sendMessage(TextFormat::RED . "Undefined SynapseClient $server");
				return true;
			}
			if($target instanseof SynapsePlayer && ($hash = $this->getClientHashByDescription($des)) != null){
				if($des == $sender->getServer()->getSynapse()->getDescription()){
					$sender->sendMessage(TextFormat::RED . "Cannot transfer to the current server");
					return true;
			    }
			}else{
				$sender->sendMessage(TextFormat::RED . $target->getName() + " is not a SynapsePlayer or $server is not a SynapseClient");
                return true;
			}

			$sender->sendMessage("Transferring player " . $target->getDisplayName() . " to $server");
			if(!$this->transferPlayer($target, $server, "")){
				$sender->sendMessage(TextFormat::RED . "An error occurred during the transfer");
			}

			return true;
		}

		return false;
	}

}
