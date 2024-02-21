# Disclaimer

| !IMPORTANT! | This is a development branch, that is not relelased by CompuLab officially yet|
|---|---|

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
|mcm-imx8m-plus|```export MACHINE=mcm-imx8m-plus```|
|~~som-imx8m-plus~~|~~```export MACHINE=som-imx8m-plus```~~|
|~~iot-gate-imx8plus~~|~~```export MACHINE=iot-gate-imx8plus```~~|
|~~sbc-iot-imx8plus~~|~~```export MACHINE=iot-gate-imx8plus```~~|

## Initialize repo manifests

* NXP
```
repo init -u https://github.com/nxp-imx/imx-manifest.git -b imx-linux-mickledore -m imx-6.1.55-2.2.0.xml
```

* CompuLab
```
mkdir -p .repo/local_manifests
wget --directory-prefix .repo/local_manifests https://raw.githubusercontent.com/compulab-yokneam/meta-bsp-imx8mp/mickledore-2.2.0/scripts/meta-bsp-imx8mp.xml
```

* Sync Them all
```
repo sync
```
## Setup build environment

* Initialize the build environment:
```
source compulab-setup-env -b build-${MACHINE}
```

* Enable the required dram setting's subset:<br>
Use [Get the product DRAM configuration ](https://github.com/compulab-yokneam/meta-bsp-imx8mp/blob/mickledore-2.2.0/Documentation/dram.md) for more details

```
sed -i '$ a DRAM_CONF = "d2d4"' ${BUILDDIR}/conf/local.conf
```

* Enable the d1d8 dram setting's subset:
```
sed -i '/DRAM_CONF/d' ${BUILDDIR}/conf/local.conf
```

## Get back to the build environment
In order to use the already created build environment issue these commands:
```
cd /path/to/compulab-nxp-bsp
repo sync
source setup-environment build-${MACHINE}
```

## Build targets
| Target | Command | The target file location |
|--- |---|---|
|full image|```bitbake -k imx-image-full```|```${BUILDDIR}/tmp/deploy/images/${MACHINE}/imx-image-full-${MACHINE}.wic.zst```|
|boot loader|```bitbake -k imx-boot```|```${BUILDDIR}/tmp/deploy/images/${MACHINE}/imx-boot-tagged```|

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

* Issue uuu command with the root credentials:
```
zstd -dc imx-image-full-${MACHINE}.wic.zst > imx-image-full-${MACHINE}.wic
sudo uuu -v -b emmc_all imx-boot-tagged mx-image-full-${MACHINE}.wic
```

#### Target Device ####

|NOTE|The target device must be in SDP or FB mode|
|---|---|


|MODE|Procedure to turn on|note|
|---|---|---|
|SDP|mmc dev 2 1; mmc erase 0x0 0x1000; reset|For advanced users only|
|FB|fastboot 0||

