# Building Boot Firmware for CompuLab's i.MX8M Plus products

Supported machines:

* `ucm-imxi8-plus`

Define a `MACHINE` environment variable for the target product:

|Machine|Command Line|
|---|---|
|ucm-imx8m-plus|export MACHINE=ucm-imx8m-plus

Define the following environment variables:

|Description|Command Line|
|---|---|
|NXP release name|export NXP_RELEASE=lf-5.10.y-1.0.0|
|NXP firmware name|export NXP_FIRMWARE=firmware-imx-8.11.bin|
|CompuLab branch name|export CPL_BRANCH=ucm-imx8m-plus-r1.0|


## Prerequisites
It is up to developer to setup arm64 build environment:
* Download the [ARM tool chain](https://developer.arm.com/tools-and-software/open-source-software/developer-tools/gnu-toolchain/gnu-a/downloads/9-2-2019-12)
* Set environment variables:
<pre>
export ARCH=arm64
export CROSS_COMPILE=/opt/gcc-arm-9.2-2019.12-x86_64-aarch64-none-linux-gnu/bin/aarch64-none-linux-gnu-
</pre>
* Create a folder to organize the files:
<pre>
mkdir imx8mp
cd imx8mp
export SRC_ROOT=$(pwd)
</pre>

* Download CompuLab BSP
<pre>
git clone -b ${CPL_BRANCH} https://github.com/compulab-yokneam/meta-bsp-imx8mp.git
export LAYER_DIR=$(pwd)/meta-bsp-imx8mp
</pre>

## Mkimage setup
* Download the mkimage:
<pre>
git clone https://source.codeaurora.org/external/imx/imx-mkimage.git
git -C imx-mkimage checkout ${NXP_RELEASE} -b ${CPL_BRANCH}
</pre>

## Arm Trusted Firmware (ATF) setup
* Download the ATF:
<pre>
git clone https://source.codeaurora.org/external/imx/imx-atf.git
git -C imx-atf checkout ${NXP_RELEASE} -b ${CPL_BRANCH}
git -C imx-atf am ${LAYER_DIR}/recipes-bsp/imx-atf/compulab/imx8mp/*.patch
</pre>
* Make bl31.bin
<pre>
make -C imx-atf PLAT=imx8mp BUILD_BASE=build-optee SPD=opteed bl31
cp -v imx-atf/build-optee/imx8mp/release/bl31.bin ${SRC_ROOT}/imx-mkimage/iMX8M/
</pre>

## Firmware iMX setup
* Download the firmware-imx file:
<pre>
wget http://www.freescale.com/lgfiles/NMG/MAD/YOCTO/${NXP_FIRMWARE}
bash -x ${NXP_FIRMWARE} --auto-accept
cp -v $(find firmware* | awk '/train|hdmi_imx8|dp_imx8/' ORS=" ") ${SRC_ROOT}/imx-mkimage/iMX8M/
</pre>

## U-Boot
* Download the U-Boot source and apply CompuLab BSP patches:
<pre>
git clone https://source.codeaurora.org/external/imx/uboot-imx.git
git -C uboot-imx checkout ${NXP_RELEASE} -b ${CPL_BRANCH}
git -C uboot-imx am ${LAYER_DIR}/recipes-bsp/u-boot/compulab/imx8mp/*.patch
</pre>

* Compile U-Boot:
<pre>
make -C uboot-imx ${MACHINE}_defconfig
make -C uboot-imx
</pre>

* Copy files to the mkimage directory:
<pre>
cp -v $(find uboot-imx | awk -v v=${MACHINE} '(/u-boot-spl.bin$|u-boot.bin$|u-boot-nodtb.bin$|tools\/mkimage$/)||($0~v".dtb$")' ORS=" ") ${SRC_ROOT}/imx-mkimage/iMX8M/
</pre>

## OP-TEE Setup
Download the OP-TEE from:
<pre>
git clone https://source.codeaurora.org/external/imx/imx-optee-os
git -C imx-optee-os checkout ${NXP_RELEASE} -b ${CPL_BRANCH}
git -C imx-atf am ${LAYER_DIR}/recipes-security/optee-imx/compulab/imx8mm/*.patch
</pre>

* Set environment variables:
<pre>
export ARCH=arm
export CROSS_COMPILE=/usr/bin/arm-linux-gnu-
export CROSS_COMPILE64=/usr/bin/arm-linux-gnu-
</pre>

* Make tee.bin
<pre>
make -C imx-optee-os PLATFORM=imx PLATFORM_FLAVOR=mx8mpevk CFG_WERROR=y CFG_TEE_CORE_LOG_LEVEL=0 CFG_TEE_TA_LOG_LEVEL=0 CFG_DDR_SIZE=0x200000000ULL
cp -v imx-optee-os/build.mx8mpevk/core/tee.bin ${SRC_ROOT}/imx-mkimage/iMX8M/
</pre>

## Compiling the **flash.bin** imx-boot image:
* Unset these variables:
<pre>
unset ARCH CROSS_COMPILE
</pre>
* Issue:
<pre>
cd ${SRC_ROOT}/imx-mkimage/iMX8M
sed "s/\(^dtbs = \).*/\1${MACHINE}.dtb/;s/\(mkimage\)_uboot/\1/" soc.mak > Makefile
make clean
make flash_evk SOC=iMX8MP
</pre>

## Flashing
`dd if=flash.bin of=/dev/<your device> bs=1K seek=32 status=progress`
