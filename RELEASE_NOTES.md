# CompuLab i.MX8MP BSP release notes

## Scope

These notes describe changes added after commit `002beb7` through commit
`f86f0f1` on the `wrynose-6.18.20-2.0.0` branch.

Kernel-specific features were reviewed separately in the `linux-compulab`
repository from `b096ce610e95` through `f40ef053ea88`. Only changes that
apply to the CompuLab i.MX8MP family are included below; i.MX93, i.MX95, and
unrelated generic NXP changes from that range are excluded.

This branch is based on NXP Wrynose `6.18.20-2.0.0` and is currently a
development branch. It has not yet been declared an official CompuLab
release.

## Highlights

### Wrynose and Linux 6.18.20 platform update

- Updated BSP, kernel, connectivity, manifests, and layer compatibility for
  NXP Wrynose `6.18.20-2.0.0`.
- Standardized all CompuLab i.MX8MP machines on Linux `6.18.20` and the
  `compulab_v8_defconfig` kernel configuration.
- Added the corresponding Linux `6.18.20` manufacturing-tool recipe.
- Switched the default network management integration from ConnMan to
  NetworkManager.

### CompuLab i.MX8MP kernel platform support

The Linux tree now has a dedicated `arch/arm64/boot/dts/compulab` hierarchy
and a shared `compulab_v8_defconfig` for the CompuLab i.MX8MP family. The
device-tree set covers IOT-GATE-iMX8Plus, IOT-DIN-iMX8P, MCM-iMX8M-Plus,
SOM-iMX8M-Plus, UCM-iMX8M-Plus, and the UCM-iMX8M-Plus SBEV platform.

The platform descriptions use reusable base trees and overlays for supported
hardware options, including:

- HDMI, LVDS, and MIPI-DSI display configurations.
- PCIe, NVMe, eMMC/SD, Ethernet, Wi-Fi, and Bluetooth options.
- CAN, RS485, SPI, I2C, UART, GPIO, and USB device interfaces.
- WM8731 and WM8962 audio, including the EB-AUDIMX9 expansion board.
- RPMsg/M7, thermal, CMA, external watchdog, and Falcon boot variants.
- Camera combinations on CSI1 and CSI2 for the supported boards.

Add and remove configuration fragments complement the common defconfig so
features can be selected without maintaining independent full configurations.

### New i.MX8MP drivers and peripheral support

The kernel range adds or updates the following family-relevant drivers:

- MAX11108 ADC support for IOT-GATE expansion configurations.
- Worldsemi WS2812B addressable RGB LED support and device-tree binding.
- Startek ILI9881C MIPI-DSI panel support.
- An external GPIO watchdog driver and an i.MX8MP example overlay.
- NXP WLAN `lf-6.18.2_1.0.0` sources for optional PCIe, SDIO, and USB
  wireless builds. The driver remains disabled unless selected by the
  CompuLab WLAN configuration.
- Realtek PHY extensions for RTL8211F LED-mode configuration and RTL8211E
  clock-output disabling through device-tree properties.
- ABX80x RTC square-wave output, digital calibration, battery-mode bus
  handling, and oscillator-glitch robustness.
- Updated IMX219 camera support from the NXP camera software package.

### IOT-GATE-iMX8Plus v2

Linux support was added for the second-generation IOT-GATE-iMX8Plus hardware,
including overlays for:

- Cellular modem control with an emergency reset mechanism.
- PCIe Wi-Fi/Bluetooth on the M.2 Key E connector.
- SDIO/UART Wi-Fi/Bluetooth modules on the M.2 Key E connector.
- NVMe storage on the M.2 Key M connector.
- Two independently selectable CAN or RS485 ports.
- Four configurable industrial digital input/output signals.
- The front-panel command button and optional HDMI output.

Product-information nodes now expose the product name for
IOT-GATE-iMX8Plus, IOT-GATE-iMX8Plus v2, and IOT-DIN-iMX8P.

### Kernel camera and overlay improvements

In addition to the ISP userspace integration described below, the kernel now
provides composable SBEV overlays for IMX219 and OV5647 on CSI1/ISP0 and
CSI2/ISP1. One overlay can be selected per interface, allowing mixed-sensor
dual-camera configurations without replacing the base board model.

The e-con Systems e-CAM131 and e-CAM0521 modules are also supported through
ported AR1335 and AR0521 MCU-controlled V4L2 drivers. Their overlays support
CSI1/ISI0 and CSI2/ISI1, detect two- or four-lane endpoints, and keep MCU
firmware updates opt-in.

Every i.MX8MP overlay records its identity below `/chosen/overlays`, allowing
userspace to discover the active hardware configuration reliably. The kernel
configuration also removes unrelated multimedia drivers while retaining the
i.MX8MP HDMI, LVDS, MIPI-DSI, LCDIF, camera, and ISP paths.

### Standalone kernel packaging

The Linux source tree can produce CompuLab deployment artifacts without
BitBake:

- `cpl-targz-pkg`, `cpl-tarbz2-pkg`, `cpl-tarxz-pkg`, and `cpl-tarzst-pkg`
  create kernel tarballs using the CompuLab boot layout.
- `compulab-bindeb-pkg` creates the normal kernel Debian packages and a
  separately versioned `compulab-kernel-hooks` package.
- The kernel post-install hook backs up the active boot partition, installs
  and verifies the new kernel and device trees, mirrors DTB and DTBO files to
  the root filesystem's `/boot` directory, and avoids chroot, offline, and
  otherwise unsafe execution environments.
- `compulab-kernel-tools-bindeb-pkg` creates a standalone, statically linked
  Debian package of selected kernel tools. Its default set contains cpupower,
  IIO, SPI, Virtio, and selected kernel selftests; additional supported tools
  can be selected through environment variables.

### U-Boot boot-container generation

`u-boot-compulab` now provides the i.MX8MP boot container directly. A normal
bootloader build produces `flash.bin`, `flash.bin.tagged`, and the canonical
`imx-boot` artifacts used by image generation and WKS.

The recipe now:

- Uses U-Boot's in-tree DRAM configuration fragments.
- Supports the predefined `d1d8` and `d2d4` DRAM subsets.
- Uses the consolidated
  `u-boot-compulab_v2023.04-d1d8_d2d4` source branch.
- Uses `compulab-5.0` as the common release suffix and embeds it together
  with the selected DRAM configuration in the U-Boot version string.
- Exports the exact ATF, OP-TEE, and LPDDR4 training firmware selected by the
  Yocto build as reproducibly compressed archives for external U-Boot builds.

See [Building boot firmware](Documentation/imx_boot_image_build.md) for the
build commands and artifact locations.

### Multiple DRAM configurations in one build

The layer can build independent `d1d8` and `d2d4` boot containers for one
machine in a single BitBake invocation. Each configuration uses an isolated
multiconfig work directory, and both tagged and untagged containers are added
to the `u-boot-compulab` package.

The `d2d4` container remains the default used by WKS for SD-card images. The
feature is enabled with:

```bitbake
require ${TOPDIR}/../sources/meta-bsp-imx8mp/conf/dram-multiconfig.conf
```

Then build:

```shell
bitbake u-boot-compulab
```

### Complete family U-Boot build

The new `u-boot-compulab-multi` target builds both DRAM configurations for
all six supported U-Boot machines:

- `iot-gate-imx8plus`
- `iotdin-imx8p`
- `mcm-imx8m-plus`
- `som-imx8m-plus`
- `ucm-imx8m-plus`
- `ucm-imx8m-plus-sbev`

This produces 12 independently built boot containers and collects tagged and
untagged artifacts in one deploy tree and package. Enable the family
multiconfig and build the explicit aggregate target:

```bitbake
require ${TOPDIR}/../sources/meta-bsp-imx8mp/conf/u-boot-family-multiconfig.conf
```

```shell
bitbake u-boot-compulab-multi
```

The family bundle is intentionally not attached to regular image targets.
This prevents an ordinary image build from expanding its kernel and package
tasks into every U-Boot multiconfig.

### Selectable family image payloads

Machine-specific device trees, firmware, packages, features, and module
autoload settings are now maintained as reusable payload inventories.

Two independent image controls are available:

- `SUPER_EVK = "1"` adds the MCM, SOM, UCM, and SBEV-UCM payloads.
- `SUPER_IOT = "1"` adds the IOT-GATE and IOTDIN payloads.

Either option or both may be enabled for a regular CompuLab i.MX8MP machine.
The demonstration-only `compulab-imx8mp` machine enables both groups by
default and uses a larger grouped-image WKS layout.

### Optional family bootloader updater payload

The `compulab-bootloader` recipe now builds the complete six-machine,
two-DRAM family matrix without BitBake multiconfig. It installs uncompressed
boot containers, environment files, and `SHA256SUMS`, and deploys compressed
runtime containers and manufacturing images.

Together with `cl-firmware`, this supports an EEPROM-driven Linux service
that selects the matching machine and DRAM container, compares it with eMMC
boot0, writes and verifies an update when needed, and then reboots.

This mechanism is experimental, is not installed by default, and is not
recommended for production use. It must be enabled explicitly. See the
[optional updater documentation](recipes-bsp/compulab-bootloader/README.md)
before evaluating it.

### IMX219 and OV5647 camera support

Optional ISP and VVCAM integration was added for:

- Sony IMX219 at 1080p30.
- OmniVision OV5647 at 1080p30.
- Either sensor on CSI1/ISP0 or CSI2/ISP1.
- Mixed IMX219 and OV5647 dual-camera capture in either connector layout.

The integration includes sensor modules, ISP drivers, calibration and dewarp
data, runtime configurations, SBEV device-tree overlays, automatic sensor and
CSI detection, and retry handling for the OV5647 chip-ID read.

The new `isp-camera-devices` helper discovers capture devices using stable
VIV platform names and can generate or run single- and dual-camera GStreamer
pipelines. See [Optional ISP support](recipes-isp/README.md) for overlay and
capture examples.

### Application integration

- GoPoint recognizes CompuLab i.MX8MP machine names and exposes compatible
  i.MX8MP demonstrations.
- nnshark uses the GStreamer common submodule revision recorded by its source
  commit, keeping the application and build-system sources synchronized.
- `imx-car-navigation` fetches CANopenNode and other submodules during
  `do_fetch`, allowing compilation to remain offline as required by BitBake.

### Optional kernel tools package group

The new `packagegroup-compulab-kernel-tools` package group collects
target-side tools built from the active Linux source tree, including perf,
bpftool, cpupower, rtla, tmon, kernel selftests, USB/IP, SPI, IIO, PCI,
Virtio, and VSOCK utilities.

The package group is machine-specific and optional; add it to an image only
when the target requires these development or diagnostic tools.

## Quality and stability improvements

- Disabled automatic selection of the NXP `beepdec` GStreamer element while
  keeping it available for explicit pipelines. This avoids a system hang and
  lets decodebin and playbin select the working `avdec_aac` decoder.
- Restored standard Yocto patch-status enforcement. Patches in this layer are
  expected to carry valid `Upstream-Status` metadata, and violations are no
  longer downgraded to warnings.
- Made multi-container packaging architecture-independent through the
  `allarch` class while retaining SPDX support.

## Migration and compatibility notes

- ConnMan recipes and defaults were removed in favor of NetworkManager.
- Legacy desktop machine configuration files were removed. Select the normal
  machine and the required image target instead.
- Obsolete Linux 6.1, 6.6, and 6.12 recipes and old PREEMPT_RT integration
  were removed. This branch supports Linux 6.18.20.
- The separate legacy `meta-imx8mp-isp` integration and its setup hooks were
  retired. Current optional camera support is maintained in this layer under
  `recipes-isp`.
- Automatic installation of `compulab-bootloader` and `cl-firmware` was
  removed. Enabling them authorizes writes to eMMC boot0 and requires a tested
  recovery procedure.
- Do not set `DRAM_CONF = "d1d8 d2d4"` to request two images. Use the provided
  multiconfig workflow so each DRAM subset is built independently.
