/**
 * Android yerel eklenti sözleşmesi.
 *
 *   1. MainActivity yerel eklentileri super.onCreate()'ten ÖNCE kaydeder. BridgeActivity köprüyü
 *      onCreate içinde kurar; sonra kaydedilen eklenti köprüye girmez ve web katmanında
 *      Capacitor.Plugins.X hiç görünmez (v1.0.59'a kadar NexusKeepAlive/NexusWidget böyle sessizce
 *      devre dışıydı).
 *   2. Sistem çubukları uygulama içi temaya uyar: theme.js her tema uygulamasında
 *      NexusSystemBars.apply({ dark, color }) çağırır; çağrı yalnız Android yerel platformda yapılır.
 */
import { describe, expect, it } from "vitest";
import { existsSync, readFileSync } from "node:fs";
import { join, resolve } from "node:path";

function repoRoot(): string {
  let dir = resolve(process.cwd());
  for (let i = 0; i < 8; i++) {
    if (existsSync(join(dir, "pnpm-workspace.yaml"))) return dir;
    dir = resolve(dir, "..");
  }
  return resolve(process.cwd());
}

const ROOT = repoRoot();
const JAVA = join(ROOT, "android", "app", "src", "main", "java", "com", "nexus", "app");

describe("Android yerel eklentileri", () => {
  const main = readFileSync(join(JAVA, "MainActivity.java"), "utf8");

  it("her yerel eklenti super.onCreate()'ten önce kaydedilir", () => {
    const superAt = main.indexOf("super.onCreate(savedInstanceState);");
    expect(superAt).toBeGreaterThan(0);
    for (const p of ["NexusKeepAlivePlugin", "NexusWidgetPlugin", "NexusSystemBarsPlugin"]) {
      const at = main.indexOf(`registerPlugin(${p}.class)`);
      expect(at, p).toBeGreaterThan(0);
      expect(at, p).toBeLessThan(superAt);
    }
  });

  it("NexusSystemBars eklentisi var ve theme.js onu yalnız Android'de çağırır", () => {
    const plugin = readFileSync(join(JAVA, "NexusSystemBarsPlugin.java"), "utf8");
    expect(plugin).toContain('@CapacitorPlugin(name = "NexusSystemBars")');
    expect(plugin).toContain("setAppearanceLightStatusBars");
    expect(plugin).toContain("setAppearanceLightNavigationBars");
    const theme = readFileSync(join(ROOT, "src", "app", "theme.js"), "utf8");
    expect(theme).toContain("syncNativeSystemBars(!!isDark)");
    expect(theme).toMatch(
      /Capacitor\.getPlatform\(\) === 'android'[\s\S]{0,200}Capacitor\.Plugins\.NexusSystemBars/,
    );
  });
});
