package com.nexus.app;

import android.os.Bundle;
import com.getcapacitor.BridgeActivity;

public class MainActivity extends BridgeActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Yerel eklentiler super.onCreate()'ten ÖNCE kaydedilmeli: BridgeActivity köprüyü onCreate
        // içinde kurar; sonradan eklenen eklenti köprüye hiç girmez (Capacitor.Plugins'te görünmez).
        registerPlugin(NexusKeepAlivePlugin.class);
        registerPlugin(NexusWidgetPlugin.class);
        // Durum/gezinme çubukları uygulama içi temaya uyar (theme.js → NexusSystemBars.apply).
        registerPlugin(NexusSystemBarsPlugin.class);
        super.onCreate(savedInstanceState);
        // Cihaz sırları için Keystore şifreleme köprüsü (window.NexusSecure); WebView yoksa sessizce atlanır.
        if (getBridge() != null && getBridge().getWebView() != null) {
            getBridge().getWebView().addJavascriptInterface(new NexusSecureStore(), "NexusSecure");
        }
    }
}
