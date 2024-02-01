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
|~~mcm-imx8m-plus~~|~~```export MACHINE=mcm-imx8m-plus```~~|
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

## Build targets

* Building the image:
```
bitbake -k imx-image-full
```

* Building the bootloader file only:

| build command | binary file location |
|---|---|
|```bitbake -k imx-boot```|```${BUILDDIR}/tmp/deploy/images/${MACHINE}/imx-boot-tagged```|

## Get back to the already created build environment
```
cd compulab-nxp-bsp
repo sync
source setup-environment build-${MACHINE}
```

## Deployment
### Create a bootable sd card

* Goto the `tmp/deploy/images/${MACHINE}` directory:
```
cd tmp/deploy/images/${MACHINE}
```

* Deploy the image:
```
zstd -dc imx-image-full-${MACHINE}.wic.zst > imx-image-full-${MACHINE}.wic
sudo bmaptool copy --bmap imx-image-full-${MACHINE}.wic.bmap imx-image-full-${MACHINE}.wic /dev/sdX
```
