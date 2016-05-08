package mc.vox.themasteredpanda.claims;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

import net.md_5.bungee.api.ChatColor;

public class main
  extends JavaPlugin
{
  public fileManager file;
  public Logger log = Logger.getLogger("Minecraft");
  public boolean claimsAvailable = false;
  public HashMap<String, String> claims = new HashMap<String, String>();
  
  public void onEnable()
  {
	this.file.setupData();
    this.file.setupLang();
    this.file.setupPackages();
    log("[" + getDescription().getName() + "]" + " v" + getDescription().getVersion() + " is now enabled!");
    this.file.getData().options().copyDefaults(true);
    saveConfig();
  }


public void onDisable()
  {
    saveConfig();
    log("[" + getDescription().getName() + "]" + " v" + getDescription().getVersion() + " is now disabled....bye!");
  }
  
  public void log(String string)
  {
    this.log.info(string);
  }
  
  public boolean onCommand(CommandSender s, Command cmd, String label, String[] args)
  {
    if (cmd.getName().equalsIgnoreCase("claim")) {
      if ((s instanceof Player))
      {
        Player p = (Player)s;
        if (args.length == 0)
        {
          this.claimsAvailable = false;
          p.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Prefix") + getConfig().getString("ClaimPackage")));
          List<String> packagesList = new ArrayList<String>(getConfig().getConfigurationSection("packages").getKeys(false));
          Multiset<String> packages = HashMultiset.create(packagesList);
          for (Multiset.Entry<String> entry : packages.entrySet())
          {
            if (p.hasPermission(getConfig().getString("packages." + (String)entry.getElement() + ".permission"))) {}
            String packageName = getConfig().getString("packages." + (String)entry.getElement() + ".name");
            p.sendMessage(getConfig().getString(ChatColor.translateAlternateColorCodes('&', "Prefix" + packageName)));
            this.claimsAvailable = true;
          }
          if (!this.claimsAvailable) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Prefix") + getConfig().getString("NoClaimsAvalible")));
          }
        }
        if (args.length == 1)
        {
          List<String> packagesList = new ArrayList<String>(getConfig().getConfigurationSection("packages").getKeys(false));
          Multiset<String> packages = HashMultiset.create(packagesList);
          for (Multiset.Entry<String> entry : packages.entrySet()) {
            if (getConfig().getString("packages." + (String)entry.getElement() + ".name").equalsIgnoreCase(args[0])) {
              if (p.hasPermission(getConfig().getString("packages." + (String)entry.getElement() + ".permission")))
              {
                if ((this.file.getData().getStringList(((String)entry.getElement()).toLowerCase()) != null) && (this.file.getData().getStringList((String)entry.getElement()).contains(p.getName())))
                {
                  p.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Prefix") + getConfig().getString("AlreadyClaimed")));
                  return false;
                }
                String playerName = p.getName();
                String packageName = (String)entry.getElement();
                this.claims.put(playerName, packageName);
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Prefix") + getConfig().getString("PromptForInventoryClear")));
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Prefix") + getConfig().getString("ClaimComfirm")));
              }
              else
              {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Prefix") + getConfig().getString("NoPermission")));
              }
            }
          }
        }
      }
      else
      {
        log("Only players can claim packages!");
      }
    }
    List<String> players;
    if (cmd.getName().equalsIgnoreCase("confirm")) {
      if ((s instanceof Player))
      {
        Player p = (Player)s;
        if (this.claims.containsKey(p.getName()))
        {
          String packageName = (String)this.claims.get(p.getName());
          
          players = new ArrayList<String>(this.file.getData().getStringList(packageName));
          players.add(p.getName());
          this.file.data.set(packageName, players);
          this.file.saveData();
          for (String commandList : getConfig().getStringList("packages." + packageName + ".commands")) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commandList.replaceAll("<name>", p.getName()));
          }
          p.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Prefix") + getConfig().getString("PlayerHasClaimed").replace("{packagename}", getConfig().getString(new StringBuilder("packages.").append(packageName).append(".name").toString()))));
        }
        else
        {
          p.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Prefix") + getConfig().getString("NoPackages")));
        }
        this.claims.remove(p.getName());
      }
      else
      {
        log("Only players can confirm claims!");
      }
    }
    if ((cmd.getName().equalsIgnoreCase("vclaim")) && 
      ((s instanceof Player)))
    {
      Player p = (Player)s;
      if (args.length == 0)
      {
        for (String msg : getConfig().getStringList("AdminHelp")) {
          p.sendMessage(ChatColor.translateAlternateColorCodes('&', msg).replace("{prefix}", getConfig().getString("Prefix")));
        }
        String playerList;
        if (args.length == 1)
        {
          if (args[0].equalsIgnoreCase("reset"))
          {
            this.file.dataFile.delete();
            this.file.data.options().copyDefaults(true);
            this.file.saveDefaultData();
            this.file.reloadData();
          }
          if (args[0].equalsIgnoreCase("reload"))
          {
            getConfig().options().copyDefaults(true);
            saveDefaultConfig();
            reloadConfig();
          }
          if (args[0].equalsIgnoreCase("add"))
          {
            String playerName = args[1];
            playerList = args[2].toLowerCase();
            if (this.file.getData().getStringList(playerList) != null)
            {
              List<String> players1 = new ArrayList<String>(this.file.getData().getStringList(playerList));
              players1.add(playerName);
              this.file.getData().set(playerList, players1);
              this.file.saveData();
              p.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Prefix") + playerName + " added to " + playerList + "."));
            }
          }
        }
        else if (args.length == 0)
        {
          for (String msg : getConfig().getStringList("AdminHelp")) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', msg).replace("{prefix}", getConfig().getString("Prefix")));
          }
          if (args.length == 1)
          {
            if (args[0].equalsIgnoreCase("reset"))
            {
              this.file.dataFile.delete();
              this.file.data.options().copyDefaults(true);
              this.file.saveDefaultData();
              this.file.reloadData();
            }
            if (args[0].equalsIgnoreCase("reload"))
            {
              getConfig().options().copyDefaults(true);
              saveDefaultConfig();
              reloadConfig();
            }
            if (args[0].equalsIgnoreCase("add")) {
              for (String msg : getConfig().getStringList("AdminHelp")) {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', msg).replace("{prefix}", getConfig().getString("Prefix")));
              }
            }
            if ((args.length == 3) && (args[0].equalsIgnoreCase("add")))
            {
              String playerName = args[1];
              String playerList1 = args[2].toLowerCase();
              if (this.file.getData().getStringList(playerList1) != null)
              {
                List<String> players1 = new ArrayList<String>(this.file.getData().getStringList(playerList1));
                players1.add(playerName);
                this.file.getData().set(playerList1, players1);
                this.file.saveData();
              }
              else
              {
                List<String> players1 = new ArrayList<String>();
                players1.add(playerName);
                this.file.getData().set(playerList1, players1);
                this.file.saveData();
                log(playerName + " added to " + playerList1 + ".");
              }
            }
          }
        }
      }
    }
    return false;
  }
}
