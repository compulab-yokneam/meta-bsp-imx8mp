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
|som-imx8m-plus|```export MACHINE=som-imx8m-plus```|

# Initialize repo manifests:

* NXP
```
repo init -u https://github.com/nxp-imx/imx-manifest -b imx-linux-hardknott -m imx-5.10.72-2.2.0.xml
```

* CompuLab
```
mkdir -p .repo/local_manifests
wget --directory-prefix .repo/local_manifests https://raw.githubusercontent.com/compulab-yokneam/meta-bsp-imx8mp/som-imx8m-plus-r1.0/scripts/imx-5.10.72-2.2.0_compulab.xml
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
