# Disclaimer

| !IMPORTANT! | This is a development branch, that is not relelased by CompuLab officially yet|
|---|---|

## Supported Compulab Products

* [`UCM-iMX8M-Plus - NXP i.MX8M Plus System-on-Module`](https://www.compulab.com/products/computer-on-modules/ucm-imx8m-plus-nxp-i-mx-8m-plus-som-system-on-module-computer/)
* [`MCM-iMX8M-Plus - SMD solder-down System-on-Module`](https://www.compulab.com/products/computer-on-modules/mcm-imx8m-plus-nxp-i-mx-8m-plus-som-system-on-module/)
* [`CL-SOM-iMX8Plus - NXP i.MX8M-Plus System-on-Module`](https://www.compulab.com/products/computer-on-modules/cl-som-imx8plus-nxp-i-mx-8m-plus-system-on-module-computer/)
* [`IOT-GATE-IMX8PLUS - Industrial IoT Gateway`](https://www.compulab.com/products/iot-gateways/iot-gate-imx8plus-industrial-arm-iot-gateway/)
* [`IOT-DIN-IMX8PLUS IoT Edge Gateway`](https://www.compulab.com/products/iot-gateways/iot-din-imx8plus-industrial-iot-gateway/)
* [`CL-SOM-iMX8Plus - NXP i.MX8M-Plus System-on-Module`](https://www.compulab.com/products/computer-on-modules/cl-som-imx8plus-nxp-i-mx-8m-plus-system-on-module-computer/)

# Configuring the build

## Setup Yocto environment

* WorkDir:
```
mkdir compulab-nxp-bsp && cd compulab-nxp-bsp
```
* Set a CompuLab machine:

| Machine | Command Line |
|---|---|
|ucm-imx8m-plus|```export MACHINE=ucm-imx8m-plus```|
|ucm-imx8m-plus-sbev|```export MACHINE=ucm-imx8m-plus-sbev```|
|~~mcm-imx8m-plus~~|~~```export MACHINE=mcm-imx8m-plus```~~|
|som-imx8m-plus|```export MACHINE=som-imx8m-plus```|
|iot-gate-imx8plus|```export MACHINE=iot-gate-imx8plus```|
|sbc-iot-imx8plus|```export MACHINE=iot-gate-imx8plus```|
|iotdin-imx8p|```export MACHINE=iotdin-imx8p```|
|compulab-imx8mp|```export MACHINE=compulab-imx8mp```|

> **Note:** The `compulab-imx8mp` machine is a proof-of-concept configuration
> provided for demonstration purposes only. It is not recommended for
> production use.

## Initialize repo manifests

* NXP
```
repo init -u https://github.com/nxp-imx/imx-manifest.git -b imx-linux-wrynose -m imx-6.18.20-2.0.0.xml
```

* CompuLab
```
wget --directory-prefix .repo/local_manifests https://raw.githubusercontent.com/compulab-yokneam/meta-bsp-imx8mp/wrynose-6.18.20-2.0.0/scripts/meta-bsp-imx8mp.xml
```

* Sync Them all
```
repo sync
```
## Setup build environment

* Initialize the build environment:
```
source compulab-setup-env build-${MACHINE}
```

* Enable the required dram setting's subset:<br>
Use [Get the product DRAM configuration ](https://github.com/compulab-yokneam/meta-bsp-imx8mp/blob/wrynose-6.18.20-2.0.0/Documentation/dram.md) for more details

```
sed -i '$ a DRAM_CONF = "d2d4"' ${BUILDDIR}/conf/local.conf
```

* Enable the d1d8 dram setting's subset:
```
sed -i '/DRAM_CONF/d' ${BUILDDIR}/conf/local.conf
sed -i '$ a DRAM_CONF = "d1d8"' ${BUILDDIR}/conf/local.conf
```

## Get back to the build environment
In order to use the already created build environment issue these commands:
```
cd /path/to/compulab-nxp-bsp
repo sync
source setup-environment build-${MACHINE}
```

## Build targets
* Main targets:

| Target | Command | The target file location |
|--- |---|---|
|full image|```bitbake -k imx-image-full```|```${BUILDDIR}/tmp/deploy/images/${MACHINE}/imx-image-full-${MACHINE}.wic.zst```|
|boot loader|```bitbake -k u-boot-compulab```|```${BUILDDIR}/tmp/deploy/images/${MACHINE}/flash.bin.tagged```|

* Other available targets (no desktop environment):

| Target | Command | The target file location |
|--- |---|---|
|fsl network image|```bitbake -k fsl-image-network-full-cmdline```|```${BUILDDIR}/tmp/deploy/images/${MACHINE}/fsl-image-network-full-cmdline-${MACHINE}.wic.zst```|
|oe core image|```bitbake -k core-image-full-cmdline```|```${BUILDDIR}/tmp/deploy/images/${MACHINE}/core-image-full-cmdline-${MACHINE}.wic.zst```|

## Advanced Features

The following optional U-Boot workflows use Yocto multiconfig. Enable only one
of the configuration files below in `${BUILDDIR}/conf/local.conf` at a time.

### Build multiple DRAM configurations

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
[`Documentation/imx_boot_image_build.md`](Documentation/imx_boot_image_build.md)
for the exact artifact paths.

### Build multiple machines

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

The target builds 12 independent boot containers and creates the
`u-boot-compulab-multi` package. The tagged and untagged files are organized
under `/boot/u-boot-compulab-multi/<machine>/`. The same directory layout is
available in the deploy directory. See
[`Documentation/imx_boot_image_build.md`](Documentation/imx_boot_image_build.md)
for the supported matrix and exact paths.

## Deployment
### Bootable sd card method
#### Host Machine ####

* Goto the `tmp/deploy/images/${MACHINE}` directory:
```
cd tmp/deploy/images/${MACHINE}
```

* Deploy the image:
```
zstd -dc imx-image-full-${MACHINE}.wic.zst > imx-image-full-${MACHINE}.wic
sudo bmaptool copy --bmap imx-image-full-${MACHINE}.wic.bmap imx-image-full-${MACHINE}.wic /dev/sdX
```
#### Target Device ####
* Turn off the device
* Insert the created sd-card
* Turn on the device and issue AltBoot

### UUU method
#### Host Machine ####
* Goto the `tmp/deploy/images/${MACHINE}` directory:
```
cd tmp/deploy/images/${MACHINE}
```

* Issue uuu command with the root credentials for a ``non compulab-imx8mp``:
```
sudo uuu -bmap -v -b emmc_all flash.bin.tagged mx-image-full-${MACHINE}.wic.zst/*
```

* Issue uuu command with the root credentials for the ``compulab-imx8mp``:

|Target device|UUU Command|
|---|---|
|som-imx8m-plus|```sudo uuu -bmap -d -v -b emmc_all compulab-bootloader/mfg/som-imx8m-plus/imx-boot_with-env_som-imx8m-plus_d2d4 imx-image-full-compulab-imx8mp.rootfs.wic.zst/*```
|ucm-imx8m-plus-sbev|```sudo uuu -bmap -d -v -b emmc_all compulab-bootloader/mfg/ucm-imx8m-plus-sbev/imx-boot_with-env_ucm-imx8m-plus-sbev_d2d4 imx-image-full-compulab-imx8mp.rootfs.wic.zst/*```


#### Target Device ####

|NOTE|The target device must be in SDP or FB mode|
|---|---|


|MODE|Procedure to turn on|note|
|---|---|---|
|SDP|mmc dev 2 1; mmc erase 0x0 0x1000; reset|For advanced users only|
|FB|fastboot 0||

## Optional generic-family firmware updater

The generic `compulab-imx8mp` machine has an optional experimental firmware
update mechanism consisting of the `compulab-bootloader` and `cl-firmware`
packages. These packages are not installed by default and are not recommended
for general or production use yet.

The system initially boots from the bootloader already installed in eMMC
boot0. When the optional packages are enabled, the `cl-firmware` service reads
the product and DRAM configuration from EEPROM during Linux startup, selects
the corresponding family bootloader, and updates eMMC boot0 when its contents
differ. See
[`recipes-bsp/compulab-bootloader/README.md`](recipes-bsp/compulab-bootloader/README.md)
before enabling this mechanism.
