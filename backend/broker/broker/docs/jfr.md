# Java Flight Recorder and JDK Mission Control

The broker starts one continuous recording named `broker`. JDK Mission Control (JMC) is the desktop tool that opens it. JFR is part of JDK 25. JMC 9.1.2 is separate and is downloaded by `scripts/jmc.ps1` the first time you run it.

`mvnw spring-boot:run` starts the recording before Spring, with the `profile` template (method samples, allocations, exceptions, and lock events). The app then enables two broker events and writes `target/jfr/broker.jfr` on shutdown.

Restart `spring-boot:run` after this change. A DevTools reload does not pick up the new JVM flag. If the JVM flag is absent (an IDE run), the app still starts the same recording once it is ready.

## Open a recording

From `backend/broker/broker`:

```powershell
powershell -ExecutionPolicy Bypass -File .\scripts\jmc.ps1
```

That dumps a snapshot from the running broker when the process is up, then opens it in JMC. Pass `-Recording target\jfr\broker.jfr` to open a file you already have.

In JMC, the broker events are:

| Event | What it shows |
| --- | --- |
| `trading.http.Exchange` | Method, path, status, outcome, and duration of each request |
| `trading.auth.Attempt` | Login, logout, or registration outcome. Email and password are not recorded |

JDK events from the `profile` template (garbage collection, threads, allocation, exceptions) are in the same file. The Event Browser can filter on `trading.`.

To inspect a file without the GUI:

```powershell
jfr summary target\jfr\broker.jfr
jfr print --events trading.http.Exchange target\jfr\broker.jfr
```

## Snapshot while the app is running

`GET http://127.0.0.1:8080/actuator/jfr` returns the recording state. `POST` the same URL writes `target/jfr/broker-<timestamp>.jfr` and returns the path. Both are allowed from localhost only.

Or, with the broker pid:

```powershell
jcmd <pid> JFR.dump name=broker filename=target\jfr\snapshot.jfr
```

## Settings

`diagnostics.jfr` in `application.yml`:

- `enabled` — when false, the app does not start a recording. The `spring-boot:run` JVM flag still does, until you remove it.
- `settings` — `profile` (default) or `default`. `default` is lighter. A path to a `.jfc` file also works.
- `name` — must stay `broker` so it matches `-XX:StartFlightRecording=name=broker`.
- `directory`, `max-age`, `max-size`, `dump-on-exit` — ring buffer and the shutdown file.
- `http.stack-trace` — when true (default), every `trading.http.Exchange` includes a stack trace in JMC. Set to `false` to keep recordings smaller.

Recordings under `target/` are not committed. The JMC download is stored in `tools/jmc/`, which is gitignored. The app is extracted under `%LOCALAPPDATA%\broker-jmc` so the folder names fit Windows' path length limit.
