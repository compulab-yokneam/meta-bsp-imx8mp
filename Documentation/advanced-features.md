# Advanced Features

The following options extend the standard per-machine build when a family-wide
image or additional bootloader variants are required.

## Build grouped family images

Two independent options install related machine payloads into an image built
for any CompuLab i.MX8MP machine. Enable the MCM, MCM-iMX8Pv2, SOM, UCM,
and SBEV-UCM group
with:

```
SUPER_EVK = "1"
```

Enable the IOT-GATE-IMX8PLUS, IOT-GATE-IMX8PV2 and IOTDIN group with:

```
SUPER_IOT = "1"
```

Add either or both settings to `${BUILDDIR}/conf/local.conf`, then build the
image normally. Each option adds that group's DTBs and overlays, firmware,
machine features, private configuration applications and tools, and kernel
module autoload settings. The selected machine still controls the bootloader
and default device tree. The `compulab-imx8mp` demonstration machine enables
both groups by default. Per-machine inventories are maintained under
`conf/machine/include/machine-payload/`.

## Build multiple DRAM configurations

This and the multiple-machine U-Boot workflow use Yocto multiconfig. Enable
only one of their configuration files in `${BUILDDIR}/conf/local.conf` at a
time.

The supported DRAM configuration options are `d1d8` and `d2d4`. Enable both
options for the selected machine with:

```
require ${TOPDIR}/../sources/meta-bsp-imx8mp/conf/dram-multiconfig.conf
```

Build both boot containers in one invocation:

```
bitbake u-boot-compulab
```

The resulting `u-boot-compulab` package contains tagged and untagged versions
of both boot containers under `/boot`. The base `d2d4` container provides the
default `imx-boot.tagged` used by WKS, while the additional `d1d8` artifact is
deployed below `${BUILDDIR}/tmp-d1d8`. See
[`Documentation/imx_boot_image_build.md`](imx_boot_image_build.md)
for the exact artifact paths.

## Build multiple machines

Re-enter the build through `compulab-setup-env` after updating this layer. The
helper installs the family multiconfig files in
`${BUILDDIR}/conf/multiconfig`. Enable the family configuration in
`${BUILDDIR}/conf/local.conf`:

```
require ${TOPDIR}/../sources/meta-bsp-imx8mp/conf/u-boot-family-multiconfig.conf
```

Build every supported machine with both DRAM configurations:

```
bitbake u-boot-compulab-multi
```

The target builds 14 independent boot containers and creates the
`u-boot-compulab-multi` package. The tagged and untagged files are organized
under `/boot/u-boot-compulab-multi/<machine>/`. The same directory layout is
available in the deploy directory. See
[`Documentation/imx_boot_image_build.md`](imx_boot_image_build.md)
for the supported matrix and exact paths.

The family bundle is built only by the explicit command above; enabling the
multiconfig file does not add it to normal image targets. This keeps
`imx-image-full` from expanding its kernel and package tasks into all 14
U-Boot-only multiconfigs.

For the faster, single-recipe family build used by the experimental firmware
updater, build `compulab-bootloader` instead. It builds the complete family
matrix in separate machine/DRAM work directories without BitBake
multiconfigs. The recipe has a different package and deploy layout; see
[`recipes-bsp/compulab-bootloader/README.md`](../recipes-bsp/compulab-bootloader/README.md).
