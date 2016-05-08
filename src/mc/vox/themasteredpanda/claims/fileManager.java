package mc.vox.themasteredpanda.claims;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class fileManager {


    public mc.vox.themasteredpanda.claims.main plugin;
    public fileManager(main plugin) {
        this.main = plugin;
    }

    public main main;
    public File dataFile = null;
    public File langFile =  null;
    public File packageFile = null;
    public FileConfiguration data = null;
    public static YamlConfiguration dataConfig;
    public static YamlConfiguration langConfig;
    public static YamlConfiguration packageConfig;




    /*setup method for date.yml*/
    public FileConfiguration setupData() {
        dataFile = new File(this.main.getDataFolder() + File.separator + "data.yml"); /* creates the file */
        dataConfig = YamlConfiguration.loadConfiguration(dataFile); /*loads the file*/
        if (!dataFile.exists()) /*checks if it exists*/ {
            try {
				/*attempts to save the file*/
                dataConfig.save(dataFile);
                System.out.println("Creating data.yml..."); /*prints to the console*/
                System.out.println("Created data.yml!");
            } catch (IOException e) /*if a failure occurs*/ {
                System.out.println("Failed to save data.yml!"); /*prints to console*/
                System.out.println("Caused by: " + e.getMessage());
            }
        }
        return dataConfig; /*returns the YamlConfiguration dataConfig*/
    }

    /*setup method for lang.yml*/
    public FileConfiguration setupLang() {
        langFile = new File(this.main.getDataFolder() + File.separator + "lang.yml"); /* creates the file */
        langConfig = YamlConfiguration.loadConfiguration(langFile); /*loads the file*/
        boolean save = false; /*sets saving the file to false*/
        if (!langFile.exists()) /*checks if it exists*/ { /*Adds all of these messages to the file*/
            List<String> AdminHelpArr = new ArrayList<String>();
            langConfig.set("Messages.Prefix", "&8[&5VClaims&8]");
            langConfig.set("Messages.NoClaimsAvailable", "&cThere are no claims avalible!");
            langConfig.set("Messages.ClaimPackage", "&aType /claim <package>");
            langConfig.set("Messages.AlreadyClaimed", "&cYou have already claimed this package! Want to claim more? Buy them @ www.thevoxmc.net/donate!");
            langConfig.set("Messages.PromptForInventoryClear", "&aMake sure your inventory is clear!");
            langConfig.set("Messages.ClaimConfirm", "&aType /confirm to claim your package!");
            langConfig.set("Messages.NoPermission", "&cYou do not have permission to claim this package.");
            langConfig.set("Messages.NoPackages", "&cYou do not have any packages to claim. Want some? Donator @ www.thevoxmc.net/donate!");
            AdminHelpArr.add("&8&m----------&8[&5VClaim&8]&8&m----------");
            AdminHelpArr.add(" ");
            AdminHelpArr.add("&e/claim reset &7- &bClear the data.yml");
            AdminHelpArr.add("&e/vclaim reload &7- &bReload the config.yml");
            AdminHelpArr.add("&e/claim add <player>	<package>");
            AdminHelpArr.add(" ");
            AdminHelpArr.add("&8&m----------------------------");
            save = true; /*After adding all of the text above, it saves the file*/
        }
        if (save) /* if it is saved*/ {
            try {
                langConfig.save(langFile); /*Attempts to save again*/
                System.out.println("Creating lang.yml..."); /*prints to the console*/
                System.out.println("Created lang.yml!");
            } catch (IOException e) /*if a failure occurs*/ {
                System.out.println("Failed to save lang.yml!"); /*prints to the console*/
                System.out.print("Caused by: " + e.getMessage());
            }
        }
        return langConfig;
    }
    /*setup method for packages.yml*/
    public FileConfiguration setupPackages() {
        packageFile = new File(this.main.getDataFolder() + File.separator + "packages.yml"); 	/*creates the file*/
        packageConfig = YamlConfiguration.loadConfiguration(packageFile); 	/*loads the file*/
        boolean save = false; 	/*sets saving to false*/
        if (!packageFile.exists()) /*checks if it exists*/ { 	/*if file doesn't exist, then it will add all of the text to the newly created file*/
            packageConfig.set("packages.dirt.permission", "vox.vclaim.package.dirt");
            packageConfig.set("packages.dirt.name", "Dirt");
            packageConfig.set("package.dirt.commands", "give {playername} dirt 69");
            save = true; /*saves the file*/
        }
        if (save) /*if saved*/ {
            try {
                packageConfig.save(packageFile); 	/*attempts to save again*/
                System.out.println("Creating packages.yml..."); 	/*prints to console*/
                System.out.println("Created packages.yml!");
            } catch (IOException e) /*if an error occurs*/ {
                System.out.println("Failed to save packages.yml!"); /*prints to console*/
                System.out.print("Caused by: " + e.getMessage());
            }
        }
        return packageConfig; /*returns packageConfig*/
    }

    /*methods for saving, reloading, resetting, getting and reading from the data.yml*/
	 /*saving default data*/
    public void saveDefaultData()  {
        if (!dataFile.exists()) {
            plugin.getResource("data.yml" + false);
        }
    }
    /*reset data.yml method (delete the current one and make a new one*/
    public void resetData() {
        new File (this.plugin.getDataFolder() + File.separator + "data.yml").delete();
        setupData();
        saveDefaultData();
    }
    /*reload data method*/
    public void reloadData() {
        if (this.dataFile == null) {
            this.dataFile = new File(this.plugin.getDataFolder(), "data.yml");
        }
        this.data = YamlConfiguration.loadConfiguration(this.dataFile);
    }
    /*get data method*/
    public FileConfiguration getData() {
        if (this.data == null) {
            reloadData();
        }
        return this.data;
    }
    /*method for saving data*/
    public void saveData() {
        if ((this.data == null ) || (this.dataFile == null)) {
            return;
        } try {
            getData().save(this.dataFile);
        } catch (IOException e) {
            this.main.log("Could not save config to" + this.dataFile);
            this.main.log.warning(e.getMessage());
        }
    }
    /*methods for saving, reloading, getting and reading from the lang.yml*/
    public void saveLang()  {
        if (!langFile.exists()) {
            plugin.saveResource("lang.yml", false);
        }
    }
    public void reloadLang() throws Exception {
        if (!langFile.exists()) {
            YamlConfiguration.loadConfiguration(langFile);
        }
    }
    /*methods for saving, reloading, getting and reading from the packages.yml*/
    public void savePackages() throws Exception {
        if (!packageFile.exists()) {
            plugin.saveResource("packages.yml", false);
        }
    }
    public void reloadPackage() throws Exception {
        if (packageFile.exists()) {
            YamlConfiguration.loadConfiguration(packageFile);
        }
    }
}
