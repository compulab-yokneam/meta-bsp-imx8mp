# Disclaimer

| !IMPORTANT! | This is a development branch, that is not relelased by CompuLab officially yet|
|---|---|

## Supported Compulab Products

* [`UCM-iMX8M-Plus - NXP i.MX8M Plus System-on-Module`](https://www.compulab.com/products/computer-on-modules/ucm-imx8m-plus-nxp-i-mx-8m-plus-som-system-on-module-computer/)
* [`MCM-iMX8M-Plus - SMD solder-down System-on-Module`](https://www.compulab.com/products/computer-on-modules/mcm-imx8m-plus-nxp-i-mx-8m-plus-som-system-on-module/)
* [`CL-SOM-iMX8Plus - NXP i.MX8M-Plus System-on-Module`](https://www.compulab.com/products/computer-on-modules/cl-som-imx8plus-nxp-i-mx-8m-plus-system-on-module-computer/)
* [`IOT-GATE-IMX8PLUS - Industrial IoT Gateway`](https://www.compulab.com/products/iot-gateways/iot-gate-imx8plus-industrial-arm-iot-gateway/)
* [`IOT-DIN-IMX8PLUS IoT Edge Gateway`](https://www.compulab.com/products/iot-gateways/iot-din-imx8plus-industrial-iot-gateway/)

# Configuring the build

* CompuLab Yocto configuration mapping
  * CompuLab Product To Yocto MACHINE:
    
    |CompuLab Product|Yocto MACHINE|local.conf entry|
    |:---|:---|:---|
    |ucm-imx8m-plus on SB-UCMIMX8PLUS|ucm-imx8m-plus|MACHINE = "ucm-imx8m-plus"|
    |ucm-imx8m-plus on SBEV-UCMIMX8PLUS|ucm-imx8m-plus-sbev|MACHINE = "ucm-imx8m-plus-sbev"|
    |mcm-imx8m-plus|mcm-imx8m-plus|MACHINE = "mcm-imx8m-plus"|
    |som-imx8m-plus|som-imx8m-plus|MACHINE = "som-imx8m-plus"|
    |iot-gate-imx8plus|iot-gate-imx8plus|MACHINE = "iot-gate-imx8plus"|
    |iotdin-imx8p|iotdin-imx8p|MACHINE = "iotdin-imx8p"|

  * CompuLab D[X] option to Yocto DRAM_CONF:

    |CompuLab D[X]|Yocto DRAM_CONF|local.conf entry|
    |:---|:---|:---|
    |D1|d1d8|DRAM_CONF = "d1d8"|
    |D2|d2|DRAM_CONF = "d2"|
    |D4|d4|DRAM_CONF = "d4"|
    |D8|d1d8|DRAM_CONF = "d8"|

## Setup Yocto environment

* WorkDir:
```
mkdir compulab-nxp-bsp && cd compulab-nxp-bsp
```
* Download NXP and CompuLab source:
```
source <(curl -L https://raw.githubusercontent.com/compulab-yokneam/meta-bsp-imx8mp/refs/heads/scarthgap/tools/run.me)
```
* Issue this command to init Yocto build environment:
```
source compulab-setup-env build-imx8mp
```

## Build targets
* Main targets:

| Target | Command | The target file location |
|--- |---|---|
|full image|```bitbake -k imx-image-full```|```${BUILDDIR}/tmp/deploy/images/${MACHINE}/imx-image-full-${MACHINE}.rootfs.wic.zst```|
|boot loader|```bitbake -k imx-boot```|```${BUILDDIR}/tmp/deploy/images/${MACHINE}/imx-boot-tagged```|

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
sudo bmaptool copy --bmap imx-image-full-${MACHINE}.rootfs.wic.bmap imx-image-full-${MACHINE}.rootfs.wic.zst /dev/sdX
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

* Issue uuu command with the root credentials:
```
sudo uuu -v -b emmc_all imx-boot-tagged imx-image-full-${MACHINE}.rootfs.wic.zst
```

#### Target Device ####

|NOTE|The target device must be in SDP or FB mode|
|---|---|


|MODE|Procedure to turn on|note|
|---|---|---|
|SDP|mmc dev 2 1; mmc erase 0x0 0x1000; reset|For advanced users only|
|FB|fastboot 0||

