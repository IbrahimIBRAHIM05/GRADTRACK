# Storage Cleanup Execution Report

**Date:** 2026-11-05 (local)  
**Scope:** Only the specific cleanup actions approved in the attached plan. No Windows files/settings, apps, projects, documents, AVDs, or other Downloads items were modified. Steam game uninstalls are still pending because Steam's UI became unavailable during its client update.

## Summary

- Removed both approved Pummel Party copies, six verified installed-app installers, and the approved Gradle cache directory.
- Skipped the VS Code installer because its setup process was actively running.
- Skipped the NVIDIA App installer because the NVIDIA App itself could not be confirmed as installed; only NVIDIA driver/components were found.
- Steam manifests confirmed both approved games, but the Steam client restarted during an update and did not expose a targetable window after a recovery launch attempt. Per Windows app-control instructions, I stopped without switching to another uninstall method. Both games remain installed.
- Android emulator inspection is complete; no AVD data was changed.
- IntelliJ IDEA, Android Studio, Gradle wrapper distributions, GradTrack, and other protected items remain present.

## Actions completed

| Exact path | Action | Size before removal | Result |
|---|---|---:|---|
| `C:\Users\CW\Downloads\Pummel-Party-SteamRIP.com` | Removed exact extracted folder | 2,368,305,232 bytes (2.206 GiB logical) | Path absent at verification |
| `C:\Users\CW\Downloads\Pummel-Party-SteamRIP.com.rar` | Removed exact archive | 1,387,156,875 bytes (1.292 GiB) | Path absent at verification |
| `C:\Users\CW\Downloads\android-studio-quail4-windows.exe` | Removed installer; Android Studio 2026.1 uninstall entry and installation confirmed | 1,484,417,720 bytes (1.382 GiB) | Path absent |
| `C:\Users\CW\Downloads\ideaIU-2025.2.4.exe` | Removed installer; IntelliJ IDEA 2025.2.4 installed | 1,428,948,848 bytes (1.331 GiB) | Path absent; IntelliJ installation preserved |
| `C:\Users\CW\Downloads\xampp-windows-x64-8.2.12-0-VS16-installer.exe` | Removed installer; XAMPP 8.2.12-0 installed | 157,583,456 bytes (0.147 GiB) | Path absent |
| `C:\Users\CW\Downloads\DiscordSetup.exe` | Removed installer; running Discord executable confirmed installed copy | 123,573,688 bytes (0.115 GiB) | Path absent; Discord app was left running |
| `C:\Users\CW\Downloads\TeamViewer_Setup_x64.exe` | Removed installer; TeamViewer 15.83.5 installed | 83,986,912 bytes (0.078 GiB) | Path absent |
| `C:\Users\CW\Downloads\Git-2.56.0.2-64-bit.exe` | Removed installer; Git 2.56.0.2 installed | 66,763,664 bytes (0.062 GiB) | Path absent |
| `C:\Users\CW\.gradle\caches` | Removed approved Gradle cache tree | Approximately 1.43 GiB per audit (exact pre-delete byte count was not captured) | Directory absent at verification |

Known exact file/folder sizes above total 6.613 GiB; including the previously measured approximate Gradle cache size, logical data removed was about 8.04 GiB. Actual free-space change is measured separately below.

## Skipped actions

| Exact path / action | Reason |
|---|---|
| `C:\Users\CW\Downloads\VSCodeUserSetup-x64-1.105.1 (1).exe` | A `CodeSetup-stable-...` process and its `.tmp` process were running during pre-deletion checks. The installer remains present (115,493,856 bytes); no attempt was made to stop the setup. |
| `C:\Users\CW\Downloads\NVIDIA_app_v11.0.7.247.exe` | The installed-app inventory showed NVIDIA driver/control-panel components but did not confirm the NVIDIA App. The exact installer remains present (176,688,672 bytes). |
| Steam uninstall of Burglin' Gnomes Demo and MECCHA CHAMELEON | Steam manifest files verified both registrations (App IDs 4356080 and 4704690). Neither game nor Steam was running before launch, and no per-game Steam userdata folders or matching named game folders were found in the inspected user data locations. Steam then launched into a client update (50.6 MB of 227.9 MB), restarted, and exposed no targetable window. A recovery launch also failed to expose a window. I stopped without changing the games or attempting another uninstall route. |

The two game folders still exist and measured 4.90 GiB and 2.42 GiB. Steam, its shared redistributables, and all game saves/user data remain untouched. No matching local save folder was identified in the inspected `AppData\Local`, `LocalLow`, `Roaming`, `Documents`, or Steam `userdata` app-ID paths; Steam Cloud or other game-specific storage remains possible. Since no game uninstall occurred, this does not risk save loss.

## Android Virtual Device inspection — no changes made

One AVD is configured:

| Field | Finding |
|---|---|
| AVD name | `Pixel_6_Pro` (directory `C:\Users\CW\.android\avd\Pixel_6_Pro.avd`) |
| Device model | Google Pixel 6 Pro (`pixel_6_pro`) |
| Android version / API | Android 16 / API 36 (`android-36`, Google APIs x86_64 image) |
| Total on-disk usage | 6.83 GiB across 39 files |
| Data partition | Configured virtual size 10 GiB; `userdata-qemu.img.qcow2` currently occupies about 2.13 GiB; a legacy `userdata-qemu.img` is also present at about 0.01 GiB |
| Snapshot | `snapshots\default_boot`; about 4.01 GiB, led by a 4.00 GiB `ram.img`; other snapshot files are small |
| Other notable disk data | `sdcard.img` about 0.50 GiB; cache images about 0.15 GiB combined |
| Most recent file modification | 2026-10-28 2:20 PM |
| Running? | No emulator process was found in the process checks |
| User data evidence | The 2.13 GiB user-data image and 0.50 GiB SD-card image may contain emulator apps/data; contents were not opened or altered. |
| Re-creatable? | The device definition/system image can be recreated if its SDK image remains available, but doing so would lose current emulator user data and snapshots. |

`Pixel_6_Pro.ini` and its `config.ini` were readable. Android Studio itself was not running, so Device Manager was not opened; the on-disk AVD configuration above was inspected directly. No AVD folder, snapshot, setting, or data file was modified.

## Preservation verification

At final verification, these all existed:

- IntelliJ IDEA: `C:\Program Files\JetBrains\IntelliJ IDEA 2025.2.4` (about 4.71 GiB measured).
- Android Studio: `C:\Program Files\Android\Android Studio` (about 3.26 GiB).
- GradTrack project: `C:\Users\CW\AndroidStudioProjects\gradtraker` (about 1.10 GiB).
- Gradle wrapper distributions: `C:\Users\CW\.gradle\wrapper` (about 0.15 GiB).
- Pixel 6 Pro AVD: `C:\Users\CW\.android\avd\Pixel_6_Pro.avd` (about 6.83 GiB).

The Steam games still exist:

- `C:\Users\CW\steam\steamapps\common\Burglin' Gnomes Demo` — 4.90 GiB.
- `C:\Users\CW\steam\steamapps\common\MECCHA CHAMELEON` — 2.42 GiB.

No Gradle, Java, Android Studio, emulator, or game process was found before cleanup. No custom `GRADLE_USER_HOME` value or custom Gradle cache path was found in the inspected environment/configuration. The project-specific temporary Gradle home under Local Temp was not touched.

## Free-space comparison

- Initial audit free-space reading: 802,250,752 bytes (about 0.75 GiB).
- Immediately before the elevated approved cleanup operation, C: showed 1,335,156,736 bytes free (about 1.24 GiB). The increase between the initial audit and this pre-action reading occurred before any approved target was removed; its cause is unknown.
- Immediately after removals, free space measured 10,029,637,632 bytes (about 9.34 GiB), a gain of 8,694,480,896 bytes (about 8.10 GiB) versus the immediate pre-action reading.
- At final verification, free space measured 9,725,427,712 bytes (about 9.06 GiB). This is 8,390,270,976 bytes (about 7.81 GiB) above the immediate pre-action reading. Steam was performing a client update during this interval, so free space fluctuated after cleanup.

## Errors / limitations

- Steam's update/restart left no targetable UI window; the approved game uninstallations remain incomplete and require Steam to be usable before retrying.
- The initial non-elevated deletion attempt did not remove targets; the approved actions were then completed in an elevated command after exact-target checks. No wildcard paths or force deletion were used.
- Steam save inspection was limited to the readable local paths and app-ID directories listed above.
- No other cleanup tool, Windows cleanup, uninstall, registry change, or system setting change was performed.

## Remaining user decision

When Steam's client update is complete and its window is available, retry only the approved Steam uninstall flow for the two registered games. All other completed cleanup remains complete; skipped installers and AVD data remain untouched.
