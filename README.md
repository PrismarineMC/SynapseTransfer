# SynapseTransfer
Edit of FastTransfer, for working with [Synapse](https://github.com/iTXTech/Synapse) by [iTXTech](https://github.com/iTXTech). [Original plugin](https://github.com/shoghicp/FastTransfer) made by [shoghicp](https://github.com/shoghicp).
# Commands
```/transfer [player] <server>``` - Transfer SynapsePlayer to other server. Servers can be setting in config.yml.
# Permissions
```synapsetransfer.command.transfer``` - Allow to use ```/transfer``` command. Default for op.
# API
```SynapseTransfer::transfer(Player $player, string $server, string $message)``` - Transfer SynapsePlayer to other SynapseClient. Example: ```$this->getServer()->getPluginManager()->getPlugin("SynapseTransfer")->transfer($player, "hub", ""); ```
# Phar
Coming soon...
