# Building boot firmware

This layer supports U-Boot 2026.04 from
`u-boot-compulab_v2026.04-imx8mp`. All machines use
`compulab-imx8mp_defconfig` followed by their target `.config` file.
The generic `compulab-imx8mp` machine uses `ucm-imx8m-plus.config`.
Each hardware target has one image with D1/D2/D4/D8 timings and MRR probing.

## Build the selected machine

```sh
bitbake u-boot-compulab
```

The normal artifacts are in `${BUILDDIR}/tmp/deploy/images/${MACHINE}`:

- `flash.bin`: the boot container.
- `flash.bin.tagged`: the UUU-tagged container used by image generation.
- `imx-boot` and `imx-boot.tagged`: canonical aliases for image tools.
- `u-boot-compulab-firmware/`: compressed ATF, OP-TEE and DDR firmware used
  by this build.

The installed package provides `/boot/flash.bin`, `/boot/flash.bin.tagged`
and machine-named aliases, without a DRAM suffix.

The `u-boot-compulab-env` package installs `/etc/fw_env.config` and the
target's generated default environment at `/etc/u-boot-initial-env`.

## Build every supported hardware target

Re-enter the environment through `compulab-setup-env` to install the updated
multiconfig templates. Enable the family configuration in `local.conf`:

```bitbake
require ${TOPDIR}/../sources/meta-bsp-imx8mp/conf/u-boot-family-multiconfig.conf
```

Then run:

```sh
bitbake u-boot-compulab-multi
```

This builds eight all-capacity containers: IOT-GATE-IMX8PLUS,
IOT-GATE-IMX8PV2, IOTDIN-IMX8P, MCM-iMX8M-Plus, MCM-iMX8Pv2,
SOM-iMX8M-Plus, UCM-iMX8M-Plus and UCM-iMX8M-Plus SBEV.
Each target has an isolated `${TOPDIR}/tmp-<multiconfig>` work/deploy tree.
The aggregate package and deploy tree contain:

```text
u-boot-compulab-multi/<machine>/flash.bin
u-boot-compulab-multi/<machine>/flash.bin.tagged
```

The bundle is only built by the explicit aggregate target. Enabling these
multiconfigs does not add cross-machine dependencies to normal image builds.
The generic machine is an image configuration, not an additional hardware
bootloader target.

## Single-recipe family bundle

`bitbake compulab-bootloader` builds the family in per-machine directories
without multiconfig. It packages `imx-boot_<machine>` and
`imx-boot_with-env_<machine>` under `/boot/compulab-bootloader/`, together
with `SHA256SUMS`. See the [bundle documentation](../recipes-bsp/compulab-bootloader/README.md).

## Existing build directories

Remove old `DRAM_CONF` assignments and any include of
`conf/dram-multiconfig.conf`. These are no longer part of the build flow.
The setup helper installs missing per-machine multiconfig templates;
old per-DRAM copies are no longer selected by the family configuration.

## External build

Use the [U-Boot 2026.04 instructions](https://github.com/compulab-yokneam/u-boot-compulab/blob/u-boot-compulab_v2026.04-imx8mp/doc/board/compulab/imx8mp-2026.rst)
with the exported firmware for a matching external build.
