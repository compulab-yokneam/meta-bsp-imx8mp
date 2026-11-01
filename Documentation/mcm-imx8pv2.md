# MCM-iMX8Pv2 on SBC-MCM8PV2

Use `MACHINE=mcm-imx8pv2`. This machine selects
`mcm-imx8pv2_defconfig` and the U-Boot branch
`u-boot-compulab_v2023.04-d1d8_d2d4-devel`. Its kernel device trees are
`compulab/mcm-imx8pv2.dtb` (module and carrier) and
`compulab/mcm-imx8pv2-som.dtb` (module only). The carrier DTB is listed first
and matches U-Boot's default `fdtfile=mcm-imx8pv2.dtb`.

The initial device trees include both Ethernet ports, LVDS, MIPI-DSI,
RX8010 RTC and eMMC. FEC uses the hardware-confirmed PHY address 7.
Panel definitions remain provisional; confirm the actual panel models and
LVDS bit mapping using the kernel's MCM-iMX8Pv2 bring-up documentation.
PCIe, Wi-Fi and Bluetooth are not enabled in this machine's initial support.

The source branches must contain the MCM-iMX8Pv2 board-support commits;
local workspace commits must be published or supplied through `devtool`
when building from another build directory. The kernel uses the existing
`linux-compulab_v6.18.20` branch.

This machine builds one U-Boot image with the following configuration:

```text
mcm-imx8pv2_defconfig d0.config d1.config d2.config d4.config d8.config lpddr_mrr_probe.config
```

All four DRAM capacities and LPDDR4 MRR probing are enabled. The deployed
image is labeled `d1d2d4d8`; this machine does not use the `d1d8`/`d2d4`
multiconfig split. The optional family-wide bootloader bundle still covers
the existing split-DRAM machines; it does not include this development
U-Boot target.

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
