# Building Boot Firmware for CompuLab's i.MX8M Plus products

## [External Build](https://github.com/compulab-yokneam/u-boot-compulab/blob/u-boot-compulab_v2023.04/README.md)

## Internal Build

### Yocto devtool method

Use this method in order to modify and create the imx-boot binary in the Yocto environment.<br>

* Get back to the build environment:
In order to use the already created build environment issue these commands:
```
cd /path/to/compulab-nxp-bsp
repo sync
source setup-environment build-${MACHINE}
```

* Get the latest u-boot source code:
```
devtool modify u-boot-compulab
```

* Goto the u-boot-compulab source tree:
```
cd ${BUILDDIR}/workspace/sources/u-boot-compulab
```

* Build the u-boot-compulab:
```
devtool build u-boot-compulab
```

* Make and commit the changes
* Apply changes from external source tree to recipe:
```
devtool update-recipe u-boot-compulab
```

* Remove the workspace layer:
```
 bitbake-layers remove-layer  ${BUILDDIR}/workspace
```

* Build the complete bootloader container:
```
bitbake -k u-boot-compulab
```

### Build all DRAM variants in one invocation

Initialize or re-enter the build through the CompuLab setup helper:

```
source compulab-setup-env <build-directory>
```

The helper installs the `d1d8` and `d2d4` multiconfig files under
`${BUILDDIR}/conf/multiconfig`, including for an existing build directory. To
build both boot containers without changing the default `DRAM_CONF` in
`local.conf`, run:

```
bitbake \
    -R ${BUILDDIR}/../sources/meta-bsp-imx8mp/conf/dram-multiconfig.conf \
    u-boot-compulab
```

The resulting `u-boot-compulab` package contains both boot containers:

```
/boot/flash.bin-${MACHINE}-d1d8
/boot/flash.bin-${MACHINE}-d1d8.tagged
/boot/flash.bin-${MACHINE}-d2d4
/boot/flash.bin-${MACHINE}-d2d4.tagged
```

The base configuration is forced to `d2d4` for this build. Its canonical
`flash.bin`, `flash.bin.tagged`, `imx-boot`, and `imx-boot.tagged` deploy files
are therefore the `d2d4` container. WKS consumes `imx-boot.tagged` for the
SD-card image. The additional `d1d8` container is built in an isolated
multiconfig work directory:

```
${BUILDDIR}/tmp/deploy/images/${MACHINE}/flash.bin
${BUILDDIR}/tmp/deploy/images/${MACHINE}/flash.bin.d2d4
${BUILDDIR}/tmp/deploy/images/${MACHINE}/flash.bin.tagged.d2d4
${BUILDDIR}/tmp-d1d8/deploy/images/${MACHINE}/flash.bin.d1d8
${BUILDDIR}/tmp-d1d8/deploy/images/${MACHINE}/flash.bin.tagged.d1d8
```

Do not use `DRAM_CONF = "d1d8 d2d4"`: that combines configuration fragments
in one U-Boot build instead of creating two independent boot containers.

### Build the complete CompuLab i.MX8MP family

The family build covers these six U-Boot machines:

```
iot-gate-imx8plus
iotdin-imx8p
mcm-imx8m-plus
som-imx8m-plus
ucm-imx8m-plus
ucm-imx8m-plus-sbev
```

Each machine is built independently for `d1d8` and `d2d4`, producing 12 boot
containers. Re-enter an existing build through the CompuLab setup helper so
the family multiconfig files are installed:

```
source compulab-setup-env <build-directory>
```

Use the family configuration instead of `dram-multiconfig.conf` in
`${BUILDDIR}/conf/local.conf`:

```
require ${TOPDIR}/../sources/meta-bsp-imx8mp/conf/u-boot-family-multiconfig.conf
```

Then issue one build command:

```
bitbake u-boot-compulab-multi
```

Every machine and DRAM pair has a separate work and deploy directory. For
example:

```
${BUILDDIR}/tmp-ucm-imx8m-plus-d1d8/deploy/images/ucm-imx8m-plus/flash.bin.d1d8
${BUILDDIR}/tmp-ucm-imx8m-plus-d2d4/deploy/images/ucm-imx8m-plus/flash.bin.d2d4
```

The aggregate target copies tagged and untagged images into its package:

```
/boot/u-boot-compulab-multi/<machine>/flash.bin-d1d8
/boot/u-boot-compulab-multi/<machine>/flash.bin-d1d8.tagged
/boot/u-boot-compulab-multi/<machine>/flash.bin-d2d4
/boot/u-boot-compulab-multi/<machine>/flash.bin-d2d4.tagged
```

The same tree is deployed under:

```
${BUILDDIR}/tmp/deploy/images/${MACHINE}/u-boot-compulab-multi/
```

The generic `compulab-imx8mp` machine is not part of the matrix because it
does not have a `compulab-imx8mp_defconfig`. It can still be the base machine
running the aggregate recipe because all U-Boot builds execute in their own
multiconfig contexts.
