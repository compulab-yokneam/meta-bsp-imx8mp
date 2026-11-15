# CompuLab i.MX8M Plus BSP Build Flow

This document describes the source-level build flow of
[`meta-bsp-imx8mp`](https://github.com/compulab-yokneam/meta-bsp-imx8mp/tree/wrynose-6.18.20-2.0.0),
branch `wrynose-6.18.20-2.0.0`, reviewed at commit
`6f474d3422bf8e84aa685e16409e083a09e073f4`.
The branch's [README](https://github.com/compulab-yokneam/meta-bsp-imx8mp/blob/6f474d3422bf8e84aa685e16409e083a09e073f4/README.md)
identifies it as a development branch. This review did not include a build.

## Overview

CompuLab extends the NXP Linux BSP with board-specific kernel, bootloader,
device-tree, firmware, and system integration. Yocto and BitBake combine these
components into a bootable Linux image and an optional application development
SDK.

## Block Diagram

```mermaid
flowchart TD
    A["NXP BSP manifest<br/>LF6.18.20_2.0.0 / Wrynose"]
    B["CompuLab local manifest<br/>CompuLab repositories and branches"]

    A --> C["Download source repositories<br/>repo sync"]
    B --> C

    C --> D["Yocto / OpenEmbedded layers<br/>Build tools, libraries and applications"]
    C --> E["NXP layers<br/>i.MX8MP platform support,<br/>firmware, graphics, multimedia and ML"]
    C --> F["CompuLab layers<br/>meta-bsp-imx8mp<br/>meta-compulab<br/>meta-compulab-bsp<br/>meta-compulab-uefi"]

    D --> G["Configure build environment<br/>compulab-setup-env"]
    E --> G
    F --> G

    H["Select target configuration<br/>Machine and DRAM configuration<br/>Distribution and image features"] --> G

    G --> I["Generated build configuration<br/>bblayers.conf / local.conf<br/>Optional multiconfig files"]

    I --> J["BitBake<br/>Resolve dependencies, fetch sources,<br/>apply patches, compile and package"]

    J --> K["CompuLab Linux 6.18.20<br/>Kernel, modules, device trees<br/>and overlays"]
    J --> L["Boot container assembly<br/>CompuLab U-Boot / SPL<br/>Arm Trusted Firmware, DDR firmware<br/>and OP-TEE"]
    J --> M["Root filesystem<br/>System services, firmware,<br/>CompuLab utilities and applications"]

    K --> N["Assemble bootable disk image<br/>imx-image-full-MACHINE.wic.zst"]
    L --> N
    M --> N

    L --> O["Standalone boot artifact<br/>flash.bin.tagged"]

    N --> P["Deploy to target<br/>SD card or eMMC"]
    O --> P

    J -. "Optional populate_sdk task" .-> Q["Application development SDK<br/>Cross compiler, headers and libraries"]
```

## Component Responsibilities

| Component | Role in this BSP |
|---|---|
| NXP manifest | Selects the underlying NXP BSP release and its repositories |
| CompuLab manifest | Adds the CompuLab repositories and setup helper |
| `meta-bsp-imx8mp` | Selects the board configuration, CompuLab kernel and bootloader, device trees, and DRAM settings |
| Shared CompuLab layers | Supply common utilities, desktop, graphics, multimedia, and other integration |
| BitBake | Builds packages and assembles the selected image and optional SDK |

The [machine configuration](https://github.com/compulab-yokneam/meta-bsp-imx8mp/blob/6f474d3422bf8e84aa685e16409e083a09e073f4/conf/machine/compulab-imx8mp.inc)
selects `linux-compulab` and `u-boot-compulab`. The latter also provides
`imx-boot`, so boot container assembly belongs under the CompuLab U-Boot build
in this diagram.

The [setup helper](https://github.com/compulab-yokneam/meta-bsp-imx8mp/blob/6f474d3422bf8e84aa685e16409e083a09e073f4/tools/compulab-setup-env)
initializes the NXP build environment and applies CompuLab configuration
templates for new build directories. It also installs the supplied multiconfig
files when entering new or existing build environments.

## Standard Build

After downloading and synchronizing the NXP and CompuLab repositories as
described in the layer's README, configure and build UCM-iMX8M-Plus with:

```bash
export MACHINE=ucm-imx8m-plus
source compulab-setup-env build-${MACHINE}

bitbake imx-image-full
```

Select the DRAM configuration appropriate to the hardware. The layer documents
`d1d8` and `d2d4` configurations, with a different configuration approach for
MCM-iMX8Pv2. See the [DRAM documentation](https://github.com/compulab-yokneam/meta-bsp-imx8mp/blob/6f474d3422bf8e84aa685e16409e083a09e073f4/Documentation/dram.md).

The SDK is a separate build task:

```bash
bitbake imx-image-full -c populate_sdk
```

## Optional Build Extensions

The layer supports grouped family images and multiple machine/DRAM bootloader
builds. These extend the standard per-machine flow and are described in the
[advanced features documentation](https://github.com/compulab-yokneam/meta-bsp-imx8mp/blob/6f474d3422bf8e84aa685e16409e083a09e073f4/Documentation/advanced-features.md).

- `SUPER_EVK` and `SUPER_IOT` add related device payloads to an image. The selected
  machine still controls the bootloader and default device tree.
- DRAM multiconfig builds create boot containers for both `d1d8` and `d2d4`.
- The explicit `u-boot-compulab-multi` target builds a family bootloader bundle;
  it is separate from the normal image build.

## Suggested Diagram Caption

> **CompuLab i.MX8M Plus BSP build flow:** CompuLab extends the NXP Linux BSP
> with board-specific kernel, bootloader, device-tree, firmware, and system
> integration. Yocto and BitBake combine these components into a bootable Linux
> image and an optional application development SDK.
