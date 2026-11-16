package com.nexus.app;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

/**
 * NEXUS — Sistem çubukları (durum + gezinme) uygulama içi temaya uyar.
 *
 * Capacitor'ın yerleşik SystemBars eklentisi çubuk zeminini ve simge rengini telefonun
 * sistem temasına göre ayarlar; NEXUS'ta tema uygulama içinden seçildiği için (Ayarlar →
 * Karanlık Mod) sistem açıkken uygulama koyuysa üstte beyaz şerit kalıyordu. Web katmanı
 * (theme.js) her tema uygulamasında apply({ dark, color }) çağırır: zemin sayfanın üst
 * rengine boyanır, simgeler koyu zeminde açık / açık zeminde koyu olur. Sistem teması
 * değişince SystemBars kendi rengini yeniden yazdığından son durum hemen geri uygulanır.
 */
@CapacitorPlugin(name = "NexusSystemBars")
public class NexusSystemBarsPlugin extends Plugin {

    private Boolean lastDark = null;
    private int lastColor = Color.TRANSPARENT;

    @PluginMethod
    public void apply(PluginCall call) {
        final boolean dark = Boolean.TRUE.equals(call.getBoolean("dark", false));
        int color = dark ? Color.parseColor("#0F172A") : Color.WHITE;
        String hex = call.getString("color", null);
        if (hex != null) {
            try {
                color = Color.parseColor(hex);
            } catch (IllegalArgumentException ignored) {
                // Geçersiz renk: tema varsayılanı kalır.
            }
        }
        final int finalColor = color;
        getBridge().executeOnMainThread(() -> {
            lastDark = dark;
            lastColor = finalColor;
            paint();
            call.resolve();
        });
    }

    @Override
    protected void handleOnConfigurationChanged(Configuration newConfig) {
        super.handleOnConfigurationChanged(newConfig);
        // SystemBars aynı olayda zemini sistem temasına döndürür; onun ardından çalışsın.
        if (lastDark == null || getActivity() == null) return;
        getActivity().getWindow().getDecorView().post(this::paint);
    }

    @Override
    protected void handleOnResume() {
        super.handleOnResume();
        if (lastDark == null || getActivity() == null) return;
        getActivity().getWindow().getDecorView().post(this::paint);
    }

    @SuppressWarnings("deprecation")
    private void paint() {
        if (lastDark == null || getActivity() == null) return;
        Window window = getActivity().getWindow();
        View decor = window.getDecorView();
        // Kenardan kenara düzende çubukların arkası decor zeminidir (Android 15+ ve Capacitor 8).
        decor.setBackgroundColor(lastColor);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.VANILLA_ICE_CREAM) {
            // Eski sürümlerde çubuk kendi rengini çizer.
            window.setStatusBarColor(lastColor);
            window.setNavigationBarColor(lastColor);
        }
        WindowInsetsControllerCompat controller = WindowCompat.getInsetsController(window, decor);
        controller.setAppearanceLightStatusBars(!lastDark);
        controller.setAppearanceLightNavigationBars(!lastDark);
    }
}
