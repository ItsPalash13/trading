# Opens the broker JFR recording in JDK Mission Control 9.1.2.
# Downloads JMC into tools/jmc on first use when jmc is not already on PATH.
#
#   powershell -ExecutionPolicy Bypass -File .\scripts\jmc.ps1
#   powershell -ExecutionPolicy Bypass -File .\scripts\jmc.ps1 -Dump
#   powershell -ExecutionPolicy Bypass -File .\scripts\jmc.ps1 -Recording target\jfr\broker.jfr

param(
	[string]$Recording,
	[switch]$Dump
)

$ErrorActionPreference = "Stop"
$ModuleRoot = Split-Path -Parent $PSScriptRoot
$JmcVersion = "9.1.2"
$ZipName = "jmc-$JmcVersion" + "_windows-x64.zip"
$Url = "https://download.java.net/java/GA/jmc9/05/binaries/$ZipName"
$ExpectedHash = "2ccc7fd0c10b9da90cf0ac73c12c35358f233322c4362efcb17679f556495119"
$ZipDir = Join-Path $ModuleRoot "tools\jmc"
# The project path is too long for some files inside the JMC zip (Windows 260-character limit).
$ToolsDir = Join-Path $env:LOCALAPPDATA "broker-jmc"

function Find-JmcExecutable {
	$cmd = Get-Command jmc -ErrorAction SilentlyContinue
	if ($cmd) {
		return $cmd.Source
	}
	if (Test-Path $ToolsDir) {
		$local = Get-ChildItem -Path $ToolsDir -Filter jmc.exe -Recurse -ErrorAction SilentlyContinue |
			Select-Object -First 1
		if ($local) {
			return $local.FullName
		}
	}
	return $null
}

function Install-Jmc {
	New-Item -ItemType Directory -Force -Path $ZipDir | Out-Null
	New-Item -ItemType Directory -Force -Path $ToolsDir | Out-Null
	$zipPath = Join-Path $ZipDir $ZipName
	$validZip = $false
	if (Test-Path $zipPath) {
		$actual = (Get-FileHash -Algorithm SHA256 $zipPath).Hash.ToLowerInvariant()
		$validZip = $actual -eq $ExpectedHash
	}
	if (-not $validZip) {
		Write-Host "Downloading JDK Mission Control $JmcVersion..."
		$ProgressPreference = "SilentlyContinue"
		Invoke-WebRequest -Uri $Url -OutFile $zipPath
		$actual = (Get-FileHash -Algorithm SHA256 $zipPath).Hash.ToLowerInvariant()
		if ($actual -ne $ExpectedHash) {
			Remove-Item $zipPath -Force
			throw "Checksum mismatch for $ZipName"
		}
	}
	Write-Host "Extracting JDK Mission Control $JmcVersion..."
	$partial = Join-Path $ZipDir ("jmc-" + $JmcVersion + "_windows-x64")
	if (Test-Path $partial) {
		Remove-Item $partial -Recurse -Force
	}
	Get-ChildItem -Path $ToolsDir -Force | Remove-Item -Recurse -Force
	Add-Type -AssemblyName System.IO.Compression.FileSystem
	[System.IO.Compression.ZipFile]::ExtractToDirectory($zipPath, $ToolsDir)
	$installed = Find-JmcExecutable
	if (-not $installed) {
		throw "jmc.exe was not found after extracting $ZipName"
	}
	return $installed
}

function Get-BrokerProcessId {
	$proc = Get-CimInstance Win32_Process -Filter "Name = 'java.exe'" |
		Where-Object { $_.CommandLine -like "*com.example.trading.TradingApplication*" } |
		Select-Object -First 1
	if ($proc) {
		return $proc.ProcessId
	}
	return $null
}

$jmc = Find-JmcExecutable
if (-not $jmc) {
	$jmc = Install-Jmc
}

if ($Dump -or -not $Recording) {
	$brokerPid = Get-BrokerProcessId
	if ($brokerPid) {
		$dir = Join-Path $ModuleRoot "target\jfr"
		New-Item -ItemType Directory -Force -Path $dir | Out-Null
		$snapshot = Join-Path $dir ("broker-" + (Get-Date -Format "yyyyMMdd-HHmmss") + ".jfr")
		Write-Host "Dumping JFR recording from pid $brokerPid"
		if (Get-Variable -Name PSNativeCommandUseErrorActionPreference -Scope Global -ErrorAction SilentlyContinue) {
			$PSNativeCommandUseErrorActionPreference = $false
		}
		& jcmd $brokerPid JFR.dump "name=broker" "filename=$snapshot"
		if ($LASTEXITCODE -eq 0 -and (Test-Path $snapshot)) {
			$Recording = $snapshot
		}
	}
}

if (-not $Recording) {
	$dir = Join-Path $ModuleRoot "target\jfr"
	$latest = Get-ChildItem -Path $dir -Filter *.jfr -ErrorAction SilentlyContinue |
		Sort-Object LastWriteTime -Descending |
		Select-Object -First 1
	if ($latest) {
		$Recording = $latest.FullName
	}
}

if ($Recording) {
	Write-Host "Opening $Recording"
	Start-Process -FilePath $jmc -ArgumentList "`"$Recording`""
}
else {
	Write-Host "No recording found. Opening JDK Mission Control. Start the broker, then attach to the local JVM."
	Start-Process -FilePath $jmc
}
