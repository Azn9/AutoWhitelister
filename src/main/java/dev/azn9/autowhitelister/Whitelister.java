package dev.azn9.autowhitelister;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import dev.azn9.autowhitelister.gui.MainGui;
import dev.azn9.autowhitelister.gui.WaitingKeypressGui;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Whitelister {

    private static final Gson GSON = new Gson();

    private final List<String> alreadyWhitelisted = new ArrayList<>();
    private final MainGui mainGui;

    public Whitelister(MainGui mainGui) {
        this.mainGui = mainGui;
    }

    public void start(JPanel contentPane, String url, String apiKey, String whitelistCommand, double delay) {
        List<String> toWhitelist;
        try {
            toWhitelist = this.loadWhitelist(url, apiKey);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(contentPane, "Une erreur est survenue : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            this.mainGui.unhide();
            return;
        }

        if (toWhitelist.isEmpty()) {
            JOptionPane.showMessageDialog(contentPane, "Aucun pseudo n'a été trouvé.", "Erreur", JOptionPane.ERROR_MESSAGE);
            this.mainGui.unhide();
            return;
        }

        this.startWhitelist(toWhitelist, whitelistCommand, delay);
    }

    private void startWhitelist(List<String> toWhitelist, String whitelistCommand, double delay) {
        new WaitingKeypressGui(this.mainGui, toWhitelist, whitelistCommand, delay).show();
    }

    private List<String> loadWhitelist(String urlString, String apiKey) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setRequestProperty("Authorization", "Bearer " + apiKey);

        int statusCode = urlConnection.getResponseCode();
        if (statusCode == 403 || statusCode == 401) {
            throw new Exception("La clé d'API est incorrecte.");
        }

        if (statusCode == 404) {
            throw new Exception("L'URL est incorrecte.");
        }

        if (statusCode != 200) {
            throw new Exception("Une erreur est survenue.");
        }

        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
        }

        JsonArray array = Whitelister.GSON.fromJson(content.toString(), JsonArray.class);

        List<String> toWhitelist = new ArrayList<>();
        for (JsonElement jsonElement : array) {
            String username = jsonElement.getAsString();

            if (!this.alreadyWhitelisted.contains(username)) {
                this.alreadyWhitelisted.add(username);
                toWhitelist.add(username);
            }
        }

        return toWhitelist;
    }
}
