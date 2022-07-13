# Building Linux Kernel for CompuLab's i.MX8M Plus products

Supported machines:

* `ucm-imxi8-plus`
* `som-imxi8-plus`
* `iot-gate-imx8plus`

Define a `MACHINE` environment variable for the target product:

|Machine|Command Line|
|---|---|
|ucm-imx8m-plus|export MACHINE=ucm-imx8m-plus
|som-imx8m-plus|export MACHINE=som-imx8m-plus
|iot-gate-imx8plus|export MACHINE=iot-gate-imx8plus

Define the following environment variables:

|Description|Command Line|
|---|---|
|NXP release name|export NXP_RELEASE=lf-5.15.5-1.0.0|
|CompuLab branch; kernel version|export CPL_BRANCH=kirkstone CPL_KERNEL=5.15.5|

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
export PATCHES=$(pwd)/meta-bsp-imx8mp/recipes-kernel/linux/compulab/${CPL_KERNEL}/imx8mp
</pre>

## CompuLab Linux Kernel setup
* Clone the source tree and apply the CompuLab patches:
<pre>
git clone https://source.codeaurora.org/external/imx/linux-imx.git
git -C linux-imx checkout -b linux-compulab ${NXP_RELEASE}
git -C linux-imx am ${PATCHES}/*.patch
</pre>

* Add the linux-rt patches (optional):
<pre>
export PATCHES=$(pwd)/meta-bsp-imx8mp/recipes-kernel/linux-rt/compulab/${CPL_KERNEL}/imx8mp
git -C linux-imx am ${PATCHES}/*.patch
cd linux-imx
scripts/kconfig/merge_config.sh  -O arch/arm64/configs/ -m  arch/arm64/configs/${MACHINE}_defconfig arch/arm64/configs/rt.config
mv arch/arm64/configs/.config arch/arm64/configs/${MACHINE}_defconfig
cd -
</pre>

## Compile the Kernel
<pre>
make -C linux-imx ${MACHINE}_defconfig
make -C linux-imx
</pre>
