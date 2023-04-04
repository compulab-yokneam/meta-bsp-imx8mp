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

## Initialize repo manifests

* NXP
```
repo init -u https://github.com/nxp-imx/imx-manifest -b imx-linux-kirkstone -m imx-5.15.32-2.0.0.xml
```

* CompuLab
```
mkdir -p .repo/local_manifests
wget --directory-prefix .repo/local_manifests https://raw.githubusercontent.com/compulab-yokneam/meta-bsp-imx8mp/ucm-imx8m-plus-r2.0/scripts/meta-bsp-imx8mp.xml
```

* Sync Them all
```
repo sync
```

## Get the product DRAM configuration

The device DRAM configuration is at the device label and is also stored in the device EEPROM.

* Getting memory configuration from the label:
Use the 'D' option code from the device label.

* Getting memory configuration from the EEPROM:

| Environment | Command | Output | Description |
|---|---|---|---|
| U-Boot| i2c dev 1; i2c md 0x50 0x90 0x10|0090: 43 31 38 30 30 51 4d 2d 44 34 2d 4e 33 32 2d 00    C1800QM-D4-N32-.| D4 = 4G
| Kernel| i2cdump -f -y 1 0x50 2>/dev/null \| awk '/^90:/' |0: 43 31 38 30 30 51 4d 2d 44 34 2d 4e 33 32 2d 00    C1800QM-D4-N32-.| |

* Mapping of product DRAM configuration to DRAM_CONF:

|Product DRAM Option|DRAM_CONF|
|---|---|
|D1,D8|d1d8|
|D2,D4|d2d4|

## Setup build environment

* Initialize the build environment:
```
source compulab-setup-env -b build-${MACHINE}
```


* Enable the required dram setting's subset:

  * For d2d4 dram setting's subset:
  ```
  sed -i '$ a DRAM_CONF = "d2d4"' ${BUILDDIR}/conf/local.conf
  ```

  * For d1d8 dram setting's subset:
  ```
  sed -i '/DRAM_CONF/d' ${BUILDDIR}/conf/local.conf
  ```

##  Building full rootfs image:

| Build Target | Build command | binary file location |
|---|---|---|
| full rootfs image |```bitbake -k imx-image-full```|```${BUILDDIR}/tmp/deploy/images/${MACHINE}/imx-image-full-${MACHINE}.wic.bz2```|


## Deployment
### Create a live SD card

* Goto the `${BUILDDIR}/tmp/deploy/images/${MACHINE}` directory:
```
cd ${BUILDDIR}/tmp/deploy/images/${MACHINE}
```

* Deploy the image:
```
sudo bmaptool copy imx-image-full-${MACHINE}.wic.bz2 --bmap imx-image-full-${MACHINE}.wic.bmap /dev/sdX
```

## Optional targets
* Building bootloader only:

| Build Target | Build command | binary file location |
|---|---|---|
| bootloader |```bitbake -k imx-boot```|```${BUILDDIR}/tmp/deploy/images/${MACHINE}/imx-boot-tagged```|
