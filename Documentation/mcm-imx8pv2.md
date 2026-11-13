# MCM-iMX8Pv2 on SBC-MCM8PV2

This guide covers the `mcm-imx8pv2` machine: the CompuLab MCM-iMX8Pv2
module on the SBC-MCM8PV2 carrier board. They describe the support present
in U-Boot 2023.04 and Linux 6.18.20 in the current development BSP.

## Machine configuration

Use `MACHINE=mcm-imx8pv2`. This machine selects
`mcm-imx8pv2_defconfig` for U-Boot and `compulab_v8_defconfig` for Linux.
The default Linux device tree is `compulab/mcm-imx8pv2.dtb`, which combines
the module and carrier board. It is listed first in the payload and matches
U-Boot's default `fdtfile=mcm-imx8pv2.dtb`. The module-only device tree is
`compulab/mcm-imx8pv2-som.dtb`; the complete inventory is listed below.

The source branches must contain the MCM-iMX8Pv2 board-support commits;
local workspace commits must be published or supplied through `devtool`
when building from another build directory.

## Bootloader features

The machine uses `u-boot-compulab` from the
`u-boot-compulab_v2023.04-d1d8_d2d4-devel` branch.

### DRAM

One bootloader image includes LPDDR4 profiles for **1 GB, 2 GB, 4 GB and
8 GB** assemblies (D1, D2, D4 and D8). SPL provides DRAM discovery,
training and EEPROM-backed identification, and passes the detected memory
ranges to Linux.

The machine builds this image with the following configuration:

```text
mcm-imx8pv2_defconfig d0.config d1.config d2.config d4.config d8.config lpddr_mrr_probe.config
```

The deployed image is labeled `d1d2d4d8`; this machine does not use the
`d1d8`/`d2d4` multiconfig split. The optional family-wide bootloader bundle
covers the split-DRAM machines and does not include this development
U-Boot target.

The machine also enables the experimental LPDDR4 mode-register-read (MRR)
probe, including probing when the EEPROM already contains identification.
A valid EEPROM profile still determines full training. Inconclusive probe
results fall back to the existing discovery procedure. Each fitted DRAM
assembly requires validation; including a capacity's profiles does not
establish timing margins for every memory part.

### Available interfaces

| Interface | Bootloader support |
| --- | --- |
| Debug console | UART2 at 115200 baud through the P3 USB-to-UART connection. |
| eMMC | On-module, eight-bit USDHC3 interface, U-Boot device `mmc 2`. |
| Micro-SD | Bootable P4 socket on USDHC2, U-Boot device `mmc 1`; four-bit, fixed 3.3 V signaling, up to 50 MHz. |
| EQOS Ethernet | P5, `ethernet@30bf0000`, Realtek RTL8211F PHY at MDIO address 4. |
| FEC Ethernet | P6, `ethernet@30be0000`, Microchip/Micrel KSZ9131 PHY at MDIO address 7; MDIO bus `FEC0`. |
| USB1 | OTG controller configuration; USB download and manufacturing support. P21 is the peripheral-only SDP/recovery connection for NXP MfgTools/UUU. |
| USB2 | Host controller and USB storage support. E1 closed selects J2; E1 open selects the USB interface on P8. |
| LVDS | P14, 1024 × 600 panel configuration, 24-bit SPWG mapping and PWM backlight. |
| MIPI-DSI | P12, four-lane Startek ILI9881C panel configuration at 720 × 1280, with PWM backlight. |
| HDMI | P22, EDID-based 720p60/1080p60 selection or explicit selection of either mode. |
| Board management | PCA9450C PMIC, module and carrier board EEPROMs, RX8010 RTC, PCA9555 GPIO expander, GPIO and I2C commands. |

U-Boot selects one display path per boot:

| `video_link` | Output |
| --- | --- |
| `0` | MIPI-DSI |
| `1` | LVDS, the default |
| `2` | HDMI |

For HDMI, `hdmi_mode=auto` is the default: a supported mode is selected
from EDID, with 720p60 as the fallback. Explicit values are `720p60` and
`1080p60`. Display selection changes take effect after saving the
environment and resetting the board. Linux display selection is independent
of these U-Boot variables.

### Supported boot candidates

Both distro boot and the BSP loader scan USB storage, micro-SD and eMMC
in that order. The default boot command runs distro boot first, followed
by the BSP loader.

| Medium | BSP candidate | U-Boot device | Default Linux root filesystem |
| --- | --- | --- | --- |
| USB storage | `usb_ul` | `usb 0`, first enumerated storage device | `/dev/sda2` |
| Micro-SD on P4 | `sd_ul` | `mmc 1` | `/dev/mmcblk1p2` |
| On-module eMMC | `emmc_ul` | `mmc 2` | `/dev/mmcblk2p2` |

The BSP loader uses partition 1 for the kernel, device tree and selected
overlays, and partition 2 for the root filesystem by default. It supports
boot scripts and applies the space-separated overlay list in `fdtofile`
to the DTB selected by `fdtfile`. The default is `mcm-imx8pv2.dtb`.

Ethernet DHCP/TFTP loading and an NFS-root boot environment are also
available through `netboot`; network boot is separate from the default
three-candidate scan. ROM selection of the medium containing the
bootloader is separate from U-Boot's selection of the Linux boot medium.
P21 SDP is a recovery/manufacturing path rather than a USB storage host.

## Linux-compulab features

The kernel uses the `linux-compulab_v6.18.20` branch. Module support includes
UART2 (`/dev/ttymxc1`), eMMC (`/dev/mmcblk2`), PCA9450C power management,
EEPROM, RX8010 RTC, status LED and watchdog.

### Carrier board interfaces

| Feature | Carrier board connector | Linux support |
| --- | --- | --- |
| EQOS Ethernet | P5 | Gigabit Ethernet with RTL8211F, MDIO address 4. |
| FEC Ethernet | P6 | Gigabit Ethernet with KSZ9131, MDIO address 7. |
| Micro-SD | P4 | USDHC2, `/dev/mmcblk1`; four-bit, fixed 3.3 V, up to 50 MHz. |
| USB1 host | J3 | Default role in carrier board configurations. |
| USB1 peripheral | P21 | Selected with `sbc-mcm8pv2-usbdev.dtbo`; Linux gadget configuration is also required. Serial gadget support is available through `g_serial`. |
| USB2 host | J2 / P8 | E1 closed routes USB2 to J2; E1 open routes it to P8. SuperSpeed pairs connect directly to J2. USB1 peripheral selection leaves USB2 in host mode. |
| LVDS | P14 | DRM display, 1024 × 600 BOE panel definition, 24-bit SPWG mapping and PWM backlight. |
| LVDS touchscreen | P13 | Goodix GT911 on I2C3 at `0x5d`, included with LVDS support. |
| MIPI-DSI | P12 | DRM display, four-lane Startek panel definition at 720 × 1280 and PWM backlight. |
| HDMI | P22 | DRM video with EDID mode discovery and HDMI audio. |
| Analog audio | J6 / J5 | WM8962B on SAI3/I2C3; stereo headphone playback on J6 and stereo line capture on J5. |
| PCIe | P23 | PCIe root port and PHY for an M.2 Key E module, with reset control. Endpoint driver and firmware depend on the installed module. |
| CAN | P17 | FlexCAN2 with ATA6563 transceiver; controller supports CAN FD. |
| UART4 | P19 | RX/TX on `/dev/ttymxc3`. |
| UART1 and UART3 | P20 | Independent RX/TX ports, `/dev/ttymxc0` and `/dev/ttymxc2`. |
| GPIO | P18 | Six named userspace lines: `P18P3`, `P18P4`, `P18P5`, `P18P6`, `P18P7`, `P18P9`. P18P4 also serves ALT_BOOT/SW1. |
| SPI | P15 / P16 | ECSPI2 on P15 (`/dev/spidev2.0`) and ECSPI1 on P16 (`/dev/spidev1.0`), with 500 kHz userspace SPI placeholders. |
| I2C | P16 | I2C4, `/dev/i2c-3`, at 100 kHz; may be used together with P16 SPI. |
| Board management | On-board | Shared power supplies, PCA9555 GPIO expander and carrier board EEPROM on I2C3. |

### Device trees and feature overlays

The default `mcm-imx8pv2.dtb` includes the carrier board interfaces,
LVDS with touchscreen, and HDMI. MIPI-DSI is disabled in this default.

| Device tree | Configuration |
| --- | --- |
| `mcm-imx8pv2-som.dtb` | Module only; USB1 remains in peripheral mode. |
| `mcm-imx8pv2-base.dtb` | Module plus shared carrier board devices, micro-SD and USB hosts. |
| `sbc-mcm8pv2-headless.dtb` | Base plus both Ethernet ports, WM8962 audio, PCIe, CAN, header UARTs, GPIO, SPI and I2C; displays disabled. |
| `sbc-mcm8pv2-lvds.dtb` | Headless configuration plus LVDS and touchscreen. |
| `sbc-mcm8pv2-mipi.dtb` | Headless configuration plus MIPI-DSI. |
| `sbc-mcm8pv2-hdmi.dtb` | Headless configuration plus HDMI. |
| `sbc-mcm8pv2-allheads.dtb` | Headless configuration plus LVDS, MIPI-DSI and HDMI. |
| `mcm-imx8pv2.dtb` | Headless configuration plus LVDS and HDMI; default. |

Separate feature overlays allow the base DTB to be tailored to the
required interfaces. All filenames below use the `sbc-mcm8pv2` prefix.

| Overlay | Feature |
| --- | --- |
| `sbc-mcm8pv2.dtbo` | Shared carrier board devices for the module-only DTB. |
| `sbc-mcm8pv2-enet.dtbo` | EQOS Ethernet. |
| `sbc-mcm8pv2-enet1.dtbo` | FEC Ethernet. |
| `sbc-mcm8pv2-lvds.dtbo` | LVDS, backlight and touchscreen. |
| `sbc-mcm8pv2-mipi.dtbo` | MIPI-DSI and backlight. |
| `sbc-mcm8pv2-hdmi.dtbo` | HDMI video and audio. |
| `sbc-mcm8pv2-wm8962.dtbo` | Analog audio. |
| `sbc-mcm8pv2-usbdev.dtbo` | USB1 peripheral mode. |
| `sbc-mcm8pv2-pcie.dtbo` | PCIe. |
| `sbc-mcm8pv2-p17_can.dtbo` | CAN. |
| `sbc-mcm8pv2-p19_uart-b.dtbo` | UART4. |
| `sbc-mcm8pv2-p20_uart-a.dtbo` | UART1 and UART3. |
| `sbc-mcm8pv2-p18_gpio.dtbo` | GPIO. |
| `sbc-mcm8pv2-p15_spi.dtbo` | ECSPI2. |
| `sbc-mcm8pv2-p16_spi.dtbo` | ECSPI1. |
| `sbc-mcm8pv2-p16_i2c.dtbo` | I2C4. |

Features included in the headless DTB are inherited by every DTB derived
from it. Starting from the module-only DTB requires the shared carrier
board overlay before the feature overlays. Avoid applying an overlay
again when its feature is already included in the selected DTB. USB1
host and peripheral modes are alternatives. Active overlay identities
are exposed under `/proc/device-tree/chosen/overlays/`.

### Validation and limitations

Linux boot with U-Boot video enabled, the LVDS backlight and mapping,
and the Goodix touchscreen have been verified during board bring-up.
HDMI cable connection and disconnection detection has been verified with
the HPD internal pull-down enabled. Both Linux and U-Boot configure this
bias to prevent the disconnected input from remaining high.
FEC PHY address 7 has been confirmed on hardware. Full carrier board
validation remains in progress, including DRAM assembly coverage,
storage boot paths, USB enumeration and SuperSpeed operation, PCIe
endpoint enumeration, and peripheral traffic tests.

Suspend/resume remains under investigation: the serial console can become
unresponsive after wake-up, and failed attempts have required a power cycle.

LVDS and MIPI-DSI definitions describe specific panels; other panels may
require different timings or drivers. Wi-Fi and Bluetooth require an
appropriate module, drivers, firmware and configuration. Camera support
is not provided by the current MCM-iMX8Pv2 device-tree set.
Confirm the actual panel models and LVDS bit mapping against the hardware
and the kernel's MCM-iMX8Pv2 bring-up documentation.

## Grouped images with SUPER_EVK

MCM-iMX8Pv2 is part of the `SUPER_EVK` kernel payload group. Add this to
`conf/local.conf` to include the MCM, MCM-iMX8Pv2, SOM, UCM and SBEV-UCM
DTBs and overlays in the same image:

```bitbake
SUPER_EVK = "1"
```

This works with `MACHINE=mcm-imx8pv2` and with the other CompuLab i.MX8MP
machines. The shared inventory in
`conf/machine/include/machine-payload/mcm-imx8pv2.inc` lists all 24
MCM-iMX8Pv2 DTBs and overlays. It is used by both this machine and grouped
images so their payloads stay aligned.

The selected machine controls the bootloader and default DTB. For
`MACHINE=mcm-imx8pv2`, the all-capacity U-Boot configuration, development
branch and default `mcm-imx8pv2.dtb` remain selected. Grouped images retain
PCI, Wi-Fi and Bluetooth machine features needed by the other boards;
this does not enable those interfaces in the MCM-iMX8Pv2 device tree.
`SUPER_IOT` can be enabled alongside `SUPER_EVK` for the IOT payloads.

Runtime usage is documented in the
[MCM-iMX8Pv2 How-To Guide](http://192.168.10.106/mediawiki/index.php/MCM-iMX8Pv2:_Yocto_Linux:_How-To_Guide).
