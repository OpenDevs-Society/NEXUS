# NEXUS — Usability Review (27 Oct 2026)

Question: has ease-of-use actually been addressed? Answer with evidence from the
automated audits (386 measurements, 63 screens × 4 device profiles) plus the fixes
landed in the last two release tours. Verdict: the fundamentals are genuinely in
place — here is what exists, what was just fixed, and the short list still open.

## Already in place (verified)

- **Touch targets:** 0 findings — every link/button meets the 32px comfort bar
  (WCAG 2.5.8 AA is 24px; we enforce the stricter bar). Invisible hit-area
  expansion keeps the design pixel-identical.
- **Keyboard & mobile keyboards:** Enter submits on login, register, password
  reset, home/room/member forms, device rename, setup codes, HA token; mobile
  keyboards show "Go/Done". No form requires a button click.
- **Scroll stability:** site modals no longer shift the page horizontally
  (scrollbar-width compensation, 27 Oct).
- **Accessibility:** full `prefers-reduced-motion` support + a kill switch for
  sounds/motion/haptics in Settings; all icon-only buttons labeled; 68 languages
  supported out of the box with automated coverage gates.
- **Honesty UX:** unsupported devices/features say so in the UI instead of
  pretending — the single most trust-building usability choice in the project.
- **Onboarding & empty states:** fresh-account walkthrough exists; empty device
  states have explicit action buttons (audited across profiles).
- **Feedback:** sound/motion/haptics with per-user toggles; toasts for copy/send;
  scene runs play one sound, not device cacophony.

## Fixed this tour

- Invite email links now land with an explicit "Invite accepted" confirmation
  (previously a silent sign-in felt like "nothing happened").
- Site modal open/close no longer shifts layout.
- Notification-channel fields commit on Enter.

## Quick wins from the 27 Oct pass — status (17 Nov 2026)

1. **Destructive actions — done.** `nexus-undo.js` (loaded on every screen by
   `theme.js`) gives deletions a branded "Geri al" bar; `nexus-dialog.js` replaces
   native confirms.
2. **First-run device scan — done where the platform allows.** Adding a device from
   the catalog starts LAN discovery automatically (`integrations-page.js` →
   `nexusLan.autoScan`, 25 s limit, progress + not-found state). Where the shell has
   no LAN discovery bridge (browser, Android WebView) the pair screen says so
   honestly and offers QR, setup code or the catalog instead of a fake scan.
3. **Error messages with next steps — done.** Unreachable-device, cloud, map and
   update errors name the next step (same Wi-Fi, check connection, try again); the
   last raw one (MQTT broker timeout) now does too.
4. **Settings search — done.** `#settings-search` filters the settings page.
5. **Password reset from login error — done.** A failed login shows an inline
   "reset password" link prefilled with the typed e-mail (`showResetHint`).

No open items from this list.
