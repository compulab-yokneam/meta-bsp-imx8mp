# Configuring the build

## Setup Yocto environment

* WorkDir:
```
mkdir compulab-nxp-bsp && cd compulab-nxp-bsp
```
* Set a CompuLab machine:

| Machine | Command Line |
|---|---|
|iot-gate-imx8plus|```export MACHINE=iot-gate-imx8plus```|
|sbc-iot-imx8plus|```export MACHINE=iot-gate-imx8plus```|

## Initialize repo manifests

* NXP
```
repo init -u https://source.codeaurora.org/external/imx/imx-manifest -b imx-linux-kirkstone -m imx-5.15.32-2.0.0.xml
```

* CompuLab
```
mkdir -p .repo/local_manifests
wget --directory-prefix .repo/local_manifests https://github.com/compulab-yokneam/meta-bsp-imx8mp/blob/iot-gate-imx8plus-r1.0/scripts/meta-bsp-imx8mp.xml

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
* Building the image:
```
bitbake -k imx-image-multimedia
```

## Deployment
### Create a bootable sd card

* Goto the `tmp/deploy/images/${MACHINE}` directory:
```
cd tmp/deploy/images/${MACHINE}
```

* Deploy the image:
```
sudo bmaptool copy imx-image-multimedia-${MACHINE}.wic.bz2 --bmap imx-image-multimedia-${MACHINE}.wic.bmap /dev/sdX
```
