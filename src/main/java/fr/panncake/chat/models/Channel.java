package fr.panncake.chat.models;

public class Channel {

    private final String name;
    private final String displayName;
    private final String permission;
    private final boolean defaultChannel;

    public Channel(String name, String displayName, String permission, boolean defaultChannel) {
        this.name = name;
        this.displayName = displayName;
        this.permission = permission;
        this.defaultChannel = defaultChannel;
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getPermission() {
        return permission;
    }

    public boolean isDefaultChannel() {
        return defaultChannel;
    }
}
