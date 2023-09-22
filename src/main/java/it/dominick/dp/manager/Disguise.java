package it.dominick.dp.manager;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import it.dominick.dp.utils.NMSHelper;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class Disguise {
    private String name;
    private String texture;
    private String signature;

    private String originalName;
    private String originalTexture;
    private String originalSignature;

    public Disguise(String name, String texture, String signature) {
        this.name = name;
        this.texture = texture;
        this.signature = signature;
    }

    public boolean apply(Player player) {
        GameProfile gameProfile = null;

        try {
            gameProfile = NMSHelper.getInstance().getGameProfile(player);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }

        if (gameProfile == null) {
            return false;
        }

        this.originalName = player.getName();
        Property originalTextures = NMSHelper.getInstance().getTexturesProperty(gameProfile);
        if (originalTextures != null) {
            this.originalTexture = originalTextures.getValue();
            this.originalSignature = originalTextures.getSignature();
        }

        gameProfile.getProperties().clear();
        setGameProfileTexture(gameProfile);
        setGameProfileName(gameProfile, name);

        if (name != null) player.setDisplayName(name);

        Bukkit.getOnlinePlayers().forEach(all -> {
            all.hidePlayer(player);
            all.showPlayer(player);
        });

        return true;
    }

    public boolean remove(Player player) {
        this.name = originalName;
        this.texture = originalTexture;
        this.signature = originalSignature;
        return apply(player);
    }

    private void setGameProfileName(GameProfile profile, String name) {
        try {
            Field field = profile.getClass().getDeclaredField("name");
            field.setAccessible(true);
            field.set(profile, name);
            field.setAccessible(false);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void setGameProfileTexture(GameProfile profile) {
        profile.getProperties()
                .put("textures",
                        new Property(
                                "textures",
                                texture,
                                signature
                        ));
    }

    public String getName() {
        return originalName;
    }
}
