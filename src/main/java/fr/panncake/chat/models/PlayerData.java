package fr.panncake.chat.models;

public class PlayerData {

    private String currentChannel;

    public PlayerData() {
        this.currentChannel = "global"; // Default channel
    }

    public String getCurrentChannel() {
        return currentChannel;
    }

    public void setCurrentChannel(String currentChannel) {
        this.currentChannel = currentChannel;
    }
}
