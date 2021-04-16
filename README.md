# Configuring the build

## Setup Yocto environment

* WorkDir
```
mkdir compulab-bsp && cd compulab-bsp
```
* Set a CompuLab machine:
```
export MACHINE=ucm-imx8m-plus
```

## Initialize repo manifests

* NXP
```
repo init -u https://source.codeaurora.org/external/imx/imx-manifest  -b imx-linux-gatesgarth -m imx-5.10.9-1.0.0.xml

repo sync
```

* CompuLab
```
mkdir -p .repo/local_manifests
cd .repo/local_manifests
wget https://raw.githubusercontent.com/compulab-yokneam/meta-bsp-imx8mp/gatesgarth/scripts/meta-bsp-imx8mp.xml
cd -
```

* Sync Them all
```
repo sync
cd .repo/local_manifests
ln -sf ../../sources/meta-bsp-imx8mp/scripts/meta-bsp-imx8mp.xml .
cd -
```

## Setup build environment

* Initialize the build environment:
```
source sources/meta-bsp-imx8mp/tools/setup-env -b build-${MACHINE}
```
* Building the image:
```
bitbake -k imx-image-full
```

## Deployment
### Create a bootable sd card

* Goto the `tmp/deploy/images/${MACHINE}` directory:
```
cd tmp/deploy/images/${MACHINE}
```

* Deploy the image:
```
sudo bmaptool copy imx-image-full-ucm-imx8m-plus.wic.bz2 --bmap imx-image-full-ucm-imx8m-plus.wic.bmap /dev/sdX
```
