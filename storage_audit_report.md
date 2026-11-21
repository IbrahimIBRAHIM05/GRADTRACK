# Windows 11 Storage Audit — Read-only findings

**Scan date:** 2026-11-05 (local)  
**Scope:** Read-only PowerShell/.NET filesystem and uninstall-registry inspection. No cleanup or changes were performed. Sizes are approximate logical file sizes (GiB unless marked GB); Windows allocated size, compression, hard links, sparse files, and inaccessible items can differ.

## A. Overall storage summary

| Volume | Capacity | Used | Free | Notes |
|---|---:|---:|---:|---|
| C: NTFS | 118.5 GiB (127,271,956,480 bytes) | 117.8 GiB | 765 MiB (802,250,752 bytes) | The free-space figure is from .NET DriveInfo. |
| D: | Not available | — | — | PowerShell exposed a D: PSDrive, but `D:\` did not exist; it was not a readable mounted volume. |

Selected accessible top-level totals from recursive file scans: `C:\Windows` 23.83 GiB; `C:\Program Files` 16.85 GiB; `C:\Program Files (x86)` 5.66 GiB; `C:\ProgramData` 1.05 GiB; `C:\Users\CW\steam` 8.94 GiB; Downloads 7.01 GiB; `.android` 6.84 GiB; Android SDK/Studio under `C:\Program Files\Android` 3.26 GiB; `.gradle` 1.59 GiB; AndroidStudioProjects 1.10 GiB; Desktop 1.41 GiB; Google program files 1.00 GiB; XAMPP 0.75 GiB; AppData Local 0.60 GiB; AppData Roaming 0.12 GiB; Python315 0.17 GiB. These are separate scan roots and should not be summed blindly where roots nest.

Root system files observed: `C:\hiberfil.sys` 6.36 GiB; `C:\pagefile.sys` 4.54 GiB; `C:\swapfile.sys` 16 MiB; `C:\DumpStack.log.tmp` 12 KiB. Preserve these; Windows manages them. Their presence explains several GB not represented by ordinary directory scans.

## B. Largest folders (ranked among notable inspected folders)

A verified full top-50 all-volume directory ranking was not possible: system inventory APIs (CIM) returned Access denied, Windows protected folders were not fully readable, and there is no usable D: volume. This is a ranked list of the largest fully inspected candidate trees, not a claim of a complete global ranking.

| Folder | Size | Latest observed modification | Notes |
|---|---:|---|---|
| `C:\Users\CW\.android\avd` | 6.83 GiB | 2026-10-19 | Android virtual device disk images; may contain emulator state and user data. |
| `C:\Users\CW\steam\steamapps\common\Burglin' Gnomes Demo` | 4.90 GiB | 2026-04-09 | Installed Steam demo. |
| `C:\Program Files\Android\Android Studio` | 3.26 GiB | 2026-08-29 | IDE install (plugins 2.38 GiB; lib 0.77 GiB; bundled JBR 0.10 GiB). Preserve unless uninstalling via official method. |
| `C:\Users\CW\steam\steamapps\common\MECCHA CHAMELEON` | 2.42 GiB | 2026-07-01 | Installed Steam game. |
| `C:\Users\CW\Downloads\Pummel-Party-SteamRIP.com` | 2.21 GiB | 2026-04-17 | Extracted game files; separately an archive exists. User decision; do not assume duplicate can be removed. |
| `C:\Users\CW\AndroidStudioProjects\gradtraker` | 1.10 GiB | 2026-10-28 | Active-looking repository/build tree; preserve. |
| `C:\Users\CW\.gradle\caches` | 1.43 GiB | 2026-10-28 | Gradle dependency/build caches; generally regenerable but can require downloads/rebuilds. |
| `C:\Users\CW\Downloads` | 7.01 GiB | 2026-11-05 | Includes installer files and the extracted Pummel Party tree. |
| `C:\Users\CW\steam` | 8.94 GiB | 2026-07-01 | Steam install/libraries, includes games above. |
| `C:\Windows` | 23.83 GiB | 2026-11-05 | Windows system files; do not manually remove. |

## C. Largest files

The all-C: top-100 scan was saved as [storage_top_files.csv](storage_top_files.csv). It uses exact paths, byte sizes, and last-write times. It is limited to files enumerable by the current user. Selected largest and relevant entries:

| File | Size | Modified | Review |
|---|---:|---|---|
| `C:\hiberfil.sys` | 6.36 GiB | 2026-11-05 | Windows hibernation/Fast Startup file; configuration-managed, do not manually delete. |
| `C:\pagefile.sys` | 4.54 GiB | 2026-10-29 | Windows virtual memory; preserve. |
| `C:\Users\CW\Downloads\android-studio-quail4-windows.exe` | 1.38 GiB | 2026-10-21 | Installer; installed Android Studio is present, but retain if you want this offline installer. |
| `C:\Users\CW\Downloads\ideaIU-2025.2.4.exe` | 1.33 GiB | 2025-11-10 | Old IntelliJ installer; IntelliJ IDEA 2025.2.4 is installed. Candidate only if installer no longer needed. |
| `C:\Users\CW\Downloads\Pummel-Party-SteamRIP.com.rar` | 1.29 GiB | 2026-04-17 | Archive plus 2.21 GiB extracted directory. Could be duplicate content; inspect/decide before removing either. |
| `C:\Users\CW\Downloads\NVIDIA_app_v11.0.7.247.exe` | 168.5 MiB | 2026-06-30 | Installer; NVIDIA components installed. |
| `C:\Users\CW\Downloads\xampp-windows-x64-8.2.12-0-VS16-installer.exe` | 150.3 MiB | 2025-12-20 | Installer; XAMPP installed at `C:\xampp`. |
| `C:\Users\CW\Downloads\DiscordSetup.exe` | 117.8 MiB | 2025-11-14 | Installer; whether still wanted unknown. |
| `C:\Users\CW\Downloads\VSCodeUserSetup-x64-1.105.1 (1).exe` | 110.1 MiB | 2025-11-08 | Duplicated-name installer; installed VS Code is suggested by its temp installer cache, but installed-app registry entry was not found. |
| `C:\Users\CW\Downloads\TeamViewer_Setup_x64.exe` | 80.1 MiB | 2026-06-30 | Installer; TeamViewer installed. |
| `C:\Users\CW\Downloads\Git-2.56.0.2-64-bit.exe` | 63.7 MiB | 2026-10-21 | Installer; Git installed. |
| `C:\Users\CW\AppData\Local\Temp\vscode-stable-user-x64\...` | 0.24 GiB total | 2026-11-05 | VS Code setup staging in Temp. Confirm installer is not running before considering Windows Temp cleanup. |
| `C:\Users\CW\AppData\Local\Temp\gradtrack-gradle-home\...` | 0.18 GiB total | 2026-10-30 | Project-specific temporary Gradle home; linked by name to gradtraker, so preserve until verified. |

## D. AppData breakdown

| Location | Total | Major contents / type | Last activity evidence | Assessment / impact |
|---|---:|---|---|---|
| `C:\Users\CW\AppData\Local` | 0.60 GiB | 663 files; Temp accounts for ~0.60 GiB; browser/program caches and app data otherwise small at current scan granularity. | Files up to 2026-11-05 | Do not treat all of Local as disposable. Windows Temp may contain active installer/build data. |
| `...\Local\Temp\vscode-stable-user-x64` | 0.24 GiB | VS Code installer staging/cache. | 2026-11-05 | Likely reproducible, but active setup may rely on it. Review after confirming no install/update is active. |
| `...\Local\Temp\gradtrack-gradle-home` | 0.18 GiB | Gradle generated jars/cache for gradtraker. | 2026-10-30 | Regenerable, but can trigger download/rebuild and may currently be used by project tooling. |
| `...\Local\Temp\chocolatey` | 0.16 GiB | Chocolatey package/install staging, installer executables and scratch. | 2026-11-05 | Often transient; recent activity indicates possibly active package installs. Defer until idle. |
| `C:\Users\CW\AppData\LocalLow` | <0.01 GiB | 82 files, includes Intel ShaderCache and app data. | 2026-11-05 | Small; shader cache is regenerable, but avoid blanket deletion. |
| `C:\Users\CW\AppData\Roaming` | 0.12 GiB | 757 files; includes Codex browser components/cache and user settings. | 2026-11-05 | Mostly app state/settings and small caches. Removing whole app folders could lose configuration/session data. |

Top named Local subfolders other than Temp each rounded below 0.01 GiB in the available report. Observed names include JetBrains, Android, Kotlin, Microsoft, BraveSoftware, Codex, Steam, TeamViewer, NVIDIA, Packages, and Programs; individually small at scan resolution. No major browser cache was found in AppData at this point.

## E. Installed applications ranked by known size

The uninstall registry yielded 50+ entries, but `EstimatedSize` and locations were missing for most entries, so a reliable size ranking per application is unavailable. Folder totals below are measured installation trees where identifiable. Last-used data is not reliably exposed by the inspected registry; usage status is therefore unknown unless installer/game metadata provides evidence.

| App | Publisher / version | Install path / measured size | Usage / uninstaller |
|---|---|---|---|
| Android Studio | Google LLC / 2026.1 | `C:\Program Files\Android\Android Studio` / 3.26 GiB | Installed; recent IDE-related files. Official uninstall entry present. |
| Steam library games | Valve / 2.10.91.91 | `C:\Users\CW\steam` / 8.94 GiB including games | Games below; official Steam uninstall/library management available. |
| IntelliJ IDEA | JetBrains / 2025.2.4 | `C:\Program Files\JetBrains\IntelliJ IDEA 2025.2.4` (size not isolated) | Installed; last use unknown. Official uninstall entry present. |
| XAMPP | Apache Friends / 8.2.12-0 | `C:\xampp` / 0.75 GiB | Installed; last use unknown. Official uninstall entry present. |
| Chrome | Google / 156.0.8078.80 | `C:\Program Files\Google\Chrome\Application` (Google tree 1.00 GiB; includes other Google files) | Installed; last use unknown. Official uninstall entry present. |
| Brave | Brave Software / 156.1.98.63 | `C:\Program Files\BraveSoftware\Brave-Browser\Application` (not separately measured) | Installed; last use unknown. Official uninstall entry present. |
| Edge / WebView2 | Microsoft / 156.0.4314.58 | `C:\Program Files (x86)\Microsoft\Edge\Application`; WebView2 separate | Windows-integrated/runtime; preserve. |
| Visual Studio Build Tools 2026 | Microsoft / 18.11.3 | `C:\Program Files (x86)\Microsoft Visual Studio\18\BuildTools` (within 5.66 GiB x86 tree) | Installed; likely dev dependency. Official Installer available. |
| Office Professional Plus 2021 | Microsoft / 16.0.20326.20232 | `C:\Program Files\Microsoft Office` (within Program Files 16.85 GiB) | Installed; usage unknown. Official Office maintenance/uninstall. |
| Python 3.15.2 | Python Software Foundation / 3.15.2150.0 | `C:\Python315` / 0.17 GiB plus registry components | Installed; multiple components. Official uninstaller. |
| Node.js | Node.js Foundation / 26.13.0 | Registry entry (path absent) | Installed; usage unknown. Official uninstaller. |
| Git | Git community / 2.56.0.2 | `C:\Program Files\Git` (included in Program Files) | Installed; likely development tool. Official uninstaller. |
| TeamViewer | TeamViewer / 15.83.5 | `C:\Program Files\TeamViewer` | Installed; use unknown. Official uninstaller. |
| WinRAR | win.rar GmbH / 7.13.0 | `C:\Program Files\WinRAR` | Installed; use unknown. Official uninstaller. |
| Microsoft Visual C++ runtimes, NVIDIA driver/components, Office components, Edge WebView2, Dolby, Vulkan, OneDrive, Teams add-in, Microsoft Visual Studio Installer | Various | Mixed system/program folders; per-app size unavailable | Preserve unless there is a specific reason; shared runtimes/drivers can affect other apps. |

## F. Games

| Game | Path | Size | Last played metadata | Potential recovery / confidence |
|---|---|---:|---|---|
| Burglin' Gnomes Demo | `C:\Users\CW\steam\steamapps\common\Burglin' Gnomes Demo` | 4.90 GiB | Steam manifest timestamp 2026-03-17 UTC (converted from Unix LastPlayed) | Up to ~4.90 GiB if you choose to uninstall via Steam; not safe to infer unused. |
| MECCHA CHAMELEON | `C:\Users\CW\steam\steamapps\common\MECCHA CHAMELEON` | 2.42 GiB | Steam manifest timestamp 2026-07-01 UTC | Up to ~2.42 GiB via Steam if unwanted. |
| Steamworks Shared Redistributables | Steam common | 0.16 GiB | Not a game; manifest says never played | Shared runtime; preserve. |
| Pummel Party extracted copy | `C:\Users\CW\Downloads\Pummel-Party-SteamRIP.com` | 2.21 GiB | Files dated 2024-06-02; folder modified 2026-04-17 | Personal/downloaded game files. Review. Archive adds another 1.29 GiB and may be duplicate. |

No Epic/Xbox library was identified by the queried paths/installed-app registry, but this is not proof none exists. Steam manifests show last-played values, not a reliable complete game-usage record. No other mounted local volume could be scanned.

## G. Android Studio / development storage

| Item | Size | Purpose / regenerability | Risk / recommendation |
|---|---:|---|---|
| Android Studio installation `C:\Program Files\Android\Android Studio` | 3.26 GiB | IDE, plugins, libraries, bundled Java runtime | Preserve; modify through official uninstall/installer only. |
| Android SDK `C:\Users\CW\AppData\Local\Android` | <0.01 GiB visible | Standard SDK location appears nearly empty. Actual SDK may be configured elsewhere; inspect Android Studio SDK Manager to verify. | Do not assume SDK absent. |
| AVD data `C:\Users\CW\.android\avd` | 6.83 GiB | Emulator virtual disks/snapshots | Can be recreated, but deleting loses emulator contents, snapshots, and possibly app/test data; retain until each AVD is identified. |
| `.android\cache` | 0.01 GiB | Android tooling cache | Re-creatable; small. |
| Gradle `C:\Users\CW\.gradle\caches` | 1.43 GiB | Dependencies, transforms, build caches | Re-fetch/rebuild possible; may cause slower builds and network use. Candidate only after no builds running. |
| Gradle wrapper distributions `...\.gradle\wrapper` | 0.15 GiB | Downloaded Gradle versions | Re-downloadable, but needed versions will be downloaded again. |
| Project `C:\Users\CW\AndroidStudioProjects\gradtraker` | 1.10 GiB | Git project and generated/build data; recent modifications | Do not remove. Need per-subfolder distinction before considering build outputs. |
| Java/JDK | Included: Android Studio bundled JBR 0.10 GiB | Runtime for IDE/builds | Preserve; separate JDK install inventory not established. |
| Kotlin caches | Tiny in Local and Gradle tree | Compiler cache/profile | Regenerable; small. |
| APK/AAB | Not separately inventoried | Build outputs may reside within project/build folders | No safe aggregate size known; project scan did not isolate file extensions. |
| Git repositories | Workspace `gradtraker` exists; other home folders not globally enumerable by first-pass scan | Source/control history may be important | Preserve; no abandonment inference based on age. |

## H. Prioritized review candidates and estimated space savings

Savings below are gross, conditional, and not additive when paths overlap. For example, a parent folder's total includes all children; the candidate totals below are chosen to avoid double-counting obvious nested entries.

| Priority / class | Exact path | Size | Evidence / modified | Regenerable? / risks | Recommended next action / estimated savings |
|---|---|---:|---|---|---|
| A – cache | `C:\Users\CW\.gradle\caches` | 1.43 GiB | Gradle cache files; last activity 2026-10-28 | Generally regenerated; rebuild/download delay, offline builds may fail until restored | Review after builds stop; possible ~1.43 GiB. |
| A/B – temp | `C:\Users\CW\AppData\Local\Temp\vscode-stable-user-x64` | 0.24 GiB | Recent setup staging, 2026-11-05 | Usually regenerable; active setup could break | Verify no installer/update active; possible ~0.24 GiB. |
| A/B – temp | `C:\Users\CW\AppData\Local\Temp\gradtrack-gradle-home` | 0.18 GiB | Project-named generated Gradle files, 2026-10-30 | Regenerable; project build may use this exact custom Gradle home | Inspect Gradle configuration first; possible ~0.18 GiB. |
| B – temp | `C:\Users\CW\AppData\Local\Temp\chocolatey` | 0.16 GiB | Recent install staging, 2026-11-05 | Often recreatable; active package setup risk | Wait until installs finish and use supported cleanup; possible ~0.16 GiB. |
| C – installer | `C:\Users\CW\Downloads\ideaIU-2025.2.4.exe` | 1.33 GiB | 2025-11-10; same version installed | Re-downloadable; loses offline installer | Keep only if offline reinstall needed; possible 1.33 GiB. |
| C – installer | `C:\Users\CW\Downloads\android-studio-quail4-windows.exe` | 1.38 GiB | 2026-10-21; Android Studio installed | Re-downloadable; loses offline installer | Decide whether installer is needed; possible 1.38 GiB. |
| C – installer | `C:\Users\CW\Downloads\NVIDIA_app_v11.0.7.247.exe` | 0.16 GiB | 2026-06-30; NVIDIA app components installed | Re-downloadable | Decide; possible 0.16 GiB. |
| C – installer | `C:\Users\CW\Downloads\xampp-windows-x64-8.2.12-0-VS16-installer.exe` | 0.15 GiB | 2025-12-20; XAMPP installed | Re-downloadable | Decide; possible 0.15 GiB. |
| C – game archive/extracted copy | `C:\Users\CW\Downloads\Pummel-Party-SteamRIP.com.rar` and extracted directory | 3.50 GiB combined | Archive dated 2026-04-17; extracted content files dated 2024-06-02 | Duplicate likely, but archive may be the only reinstall source; downloaded personal content | Compare contents and decide which to retain; possible up to 3.50 GiB, do not count both as certain savings. |
| E – game | `C:\Users\CW\steam\steamapps\common\Burglin' Gnomes Demo` | 4.90 GiB | LastPlayed 2026-03-17 UTC | Re-downloadable; saves/mods may exist elsewhere | Review recent play; uninstall through Steam only if approved. |
| E – game | `C:\Users\CW\steam\steamapps\common\MECCHA CHAMELEON` | 2.42 GiB | LastPlayed 2026-07-01 UTC | Re-downloadable; saves/mods may exist elsewhere | Review; potential recovery 2.42 GiB. |
| F/G – development | `C:\Users\CW\.android\avd` | 6.83 GiB | Latest 2026-10-19 | Recreate emulator, but lose virtual device state/snapshots/apps | Identify AVD names and use Android Studio Device Manager; no deletion recommendation until reviewed. |
| G – project | `C:\Users\CW\AndroidStudioProjects\gradtraker` | 1.10 GiB | Active/recent modifications | Not safely disposable; includes source/repository and build outputs | Inspect project subfolders; preserve source and Git metadata. |
| H – personal | Desktop 1.41 GiB; Downloads 7.01 GiB | Mixed files | Recent activity | Personal data, installers, game copy | Review file by file; no automated cleanup recommendation. |

Windows system category (I): `C:\Windows` measured 23.83 GiB. `C:\Windows\Temp` enumerated as 0 files; actual protected/system cache accounting is incomplete. `C:\$Recycle.Bin` was not readable and returned 0 enumerable files, so Recycle Bin size is **unknown**, not empty. `C:\System Volume Information` was inaccessible; restore points/shadow copies could not be measured. Component store/WinSxS should be inspected with Microsoft's supported DISM analysis and cleaned only through Windows Settings > System > Storage > Temporary files / Storage Sense or supported DISM servicing commands after explicit approval. Windows Update and Delivery Optimization caches were not reliably measurable in this read-only pass. Do not manually delete WinSxS, hibernation/page files, system restore data, or protected Windows files.

## I. Old files and projects requiring review

- `C:\Users\CW\Downloads\ideaIU-2025.2.4.exe` is about one year old and duplicate in function with the installed version; installer retention is personal preference.
- The Pummel Party extracted folder contains files from 2024 and is paired with a 1.29 GiB archive. Age alone does not prove either is unnecessary.
- `C:\Users\CW\AndroidStudioProjects\gradtraker` is a recent 1.10 GiB project. Preserve; identify build output separately before proposing savings.
- Downloads also has Discord, VS Code, Git, XAMPP, TeamViewer, NVIDIA, and Node installers. Several corresponding products are installed; whether to retain offline installers is unknown.
- No duplicate-file hash analysis was run. The archive/extracted game relationship is a likely content duplicate, not verified by hash.

## J. Recommended cleanup plan for later approval

1. First review installers in Downloads (about 3.0 GiB across the Android Studio, IntelliJ, NVIDIA, XAMPP, Discord, VS Code, TeamViewer, Git, and Node installers listed in the CSV), keeping any needed offline copies.
2. Compare the Pummel Party archive with its extracted folder; up to 3.50 GiB could be recovered if one copy is no longer needed.
3. Check Steam last-played and save/mod data; the two installed games total about 7.32 GiB.
4. Review AVD names, snapshots, and emulator state; up to 6.83 GiB, but this carries the highest development-data risk.
5. Consider Gradle and installer temp caches only after confirming no build/install process is active; these yield about 2.01 GiB across listed cache/temp candidates (without the temp VS Code 0.24 GiB? included separately: total listed A/B caches = 2.01 GiB). Some folders may contain active staging files.
6. Use Windows Storage settings for Windows temporary files and inspect component store/restore points with supported tools; measurements were incomplete and not included in savings totals.

Conditional potential recovery from the specific review set (installers about 3.42 GiB, one of archive/extracted game copy up to 2.21 GiB or 1.29 GiB, games 7.32 GiB, AVD 6.83 GiB, Gradle/temp candidates 2.01 GiB) is **up to ~21.8 GiB**, but that is a ceiling, not a recommendation or safe/available promise. It excludes overlaps, protected system categories, personal files, and uncertain/active data. Cache/temp items are the lowest-risk class only after verifying processes are idle; games/installers require your choice; emulator data and projects require careful review.

## Scan limitations / confidence

- CIM queries for logical disks and OS data returned Access denied; .NET DriveInfo provided C: capacity/free-space instead.
- D: appeared as a PowerShell drive name but did not exist as a path. No other local volume was available.
- Protected Windows/system areas, Recycle Bin, and System Volume Information were not fully readable without elevated access. No security controls were bypassed.
- Some directory totals are recursive file sums and can be affected by inaccessible files, junctions, and file-system accounting. The overall application list comes from uninstall registry entries, which omit Store/portable apps and frequently have no size or last-use data.
- A full reliable top-50 folder list and authoritative app size ranking could not be produced from available read access. `storage_top_files.csv` provides the accessible all-C: top-100 file list.

**No files were deleted, moved, modified, or uninstalled.**

