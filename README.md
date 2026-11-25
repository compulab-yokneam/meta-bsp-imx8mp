# Disclaimer

| !IMPORTANT! | This is a development branch, that is not relelased by CompuLab officially yet|
|---|---|

## Supported Compulab Products

* [`UCM-iMX8M-Plus - NXP i.MX8M Plus System-on-Module`](https://www.compulab.com/products/computer-on-modules/ucm-imx8m-plus-nxp-i-mx-8m-plus-som-system-on-module-computer/)
* [`MCM-iMX8Pv2 on SBC-MCM8PV2`](Documentation/mcm-imx8pv2.md)
* [`MCM-iMX8M-Plus - SMD solder-down System-on-Module`](https://www.compulab.com/products/computer-on-modules/mcm-imx8m-plus-nxp-i-mx-8m-plus-som-system-on-module/)
* [`CL-SOM-iMX8Plus - NXP i.MX8M-Plus System-on-Module`](https://www.compulab.com/products/computer-on-modules/cl-som-imx8plus-nxp-i-mx-8m-plus-system-on-module-computer/)
* [`IOT-GATE-IMX8PLUS - Industrial IoT Gateway`](https://www.compulab.com/products/iot-gateways/iot-gate-imx8plus-industrial-arm-iot-gateway/)
* [`IOT-GATE-IMX8PV2 - Industrial IoT Gateway`](Documentation/iot-gate-imx8pv2.md)
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
|mcm-imx8pv2|```export MACHINE=mcm-imx8pv2```|
|som-imx8m-plus|```export MACHINE=som-imx8m-plus```|
|iot-gate-imx8plus|```export MACHINE=iot-gate-imx8plus```|
|iot-gate-imx8pv2|```export MACHINE=iot-gate-imx8pv2```|
|sbc-iot-imx8plus|```export MACHINE=iot-gate-imx8plus```|
|iotdin-imx8p|```export MACHINE=iotdin-imx8p```|

## Initialize repo manifests

* NXP
```
repo init -u https://github.com/nxp-imx/imx-manifest.git -b imx-linux-wrynose -m imx-6.18.20-2.0.0.xml
```

* CompuLab
```
wget --directory-prefix .repo/local_manifests https://raw.githubusercontent.com/compulab-yokneam/meta-bsp-imx8mp/wrynose-6.18.20-2.0.0_u-boot-2026.04/scripts/meta-bsp-imx8mp.xml
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

U-Boot 2026.04 uses `u-boot-compulab_v2026.04-imx8mp` for every supported
machine. The recipe selects `compulab-imx8mp_defconfig` followed by the
machine's target `.config`. The generic `compulab-imx8mp` machine uses
`ucm-imx8m-plus.config` as its boot target.

Each machine has one bootloader image with D1/D2/D4/D8 support and the
LPDDR4 MRR probe, including probing with a valid EEPROM ID. `DRAM_CONF`
is no longer used. Remove old DRAM-subset settings and any
`require .../conf/dram-multiconfig.conf` line from existing build configurations.

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

* Run UUU with root privileges:
```
sudo uuu -bmap -v -b emmc_all flash.bin.tagged imx-image-full-${MACHINE}.wic.zst/*
```

#### Target Device ####

|NOTE|The target device must be in SDP or FB mode|
|---|---|


|MODE|Procedure to turn on|note|
|---|---|---|
|SDP|mmc dev 2 1; mmc erase 0x0 0x1000; reset|For advanced users only|
|FB|fastboot 0||

## Additional documentation

- [Adding a machine](Documentation/adding-a-machine.md): step-by-step file checklist,
  configuration examples, grouped payloads, bootloader builds, and validation.
- [Advanced Features](Documentation/advanced-features.md): grouped family images,
  multiple DRAM configurations, and multiple-machine bootloader builds.
- [Optional generic-family firmware updater](Documentation/generic-family-firmware-updater.md):
  the experimental EEPROM-selected bootloader update mechanism.
