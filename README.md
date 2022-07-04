# Configuring the build

## Setup Yocto environment

* WorkDir:
```
mkdir compulab-bsp && cd compulab-bsp
```
* Set a CompuLab machine:

| Machine | Command Line |
|---|---|
|ucm-imx8m-plus|```export MACHINE=ucm-imx8m-plus```|
|iot-gate-imx8plus|```export MACHINE=iot-gate-imx8plus```|

* Initialize repo manifests:
```
repo init -u https://source.codeaurora.org/external/imx/imx-manifest  -b imx-linux-honister -m imx-5.15.5-1.0.0.xml
wget --directory-prefix .repo/manifests https://raw.githubusercontent.com/compulab-yokneam/meta-bsp-imx8mp/honister/scripts/imx-5.15.5-1.0.0_compulab.xml
repo init -m imx-5.15.5-1.0.0_compulab.xml
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
sudo bmaptool copy imx-image-multimedia-ucm-imx8m-plus.wic.bz2 --bmap imx-image-multimedia-ucm-imx8m-plus.wic.bmap /dev/sdX
```
