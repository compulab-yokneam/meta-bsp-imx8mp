# Building Linux Kernel for CompuLab's i.MX8M Plus products

Supported machines:

* `iot-gate-imx8plus`
* `sbc-iot-imx8plus`

Define a `MACHINE` environment variable for the target product:

|Machine|Command Line|
|---|---|
|iot-gate-imx8plus|export MACHINE=iot-gate-imx8plus
|sbc-iot-imx8plus|export MACHINE=iot-gate-imx8plus

Define the following environment variables:

|Description|Command Line|
|---|---|
|NXP release name|export NXP_RELEASE=lf-5.15.5-1.0.0|
|CompuLab branch name|export CPL_BRANCH=iot-gate-imx8plus-r1.0|
|Linux kernel version|export KERNEL_VER=5.15.5|

## Prerequisites
* [Setup arm64 toolchain](https://github.com/compulab-yokneam/meta-bsp-imx8mp/blob/ucm-imx8m-plus-r1.1/Documentation/toolchain.md)
* Make sure environment variables **ARCH** and **CROSS_COMPILE** are properly set. Example:
<pre>
ARCH=arm64
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
export PATCHES=$(pwd)/meta-bsp-imx8mp/recipes-kernel/linux/compulab/${KERNEL_VER}/imx8mp
</pre>

## CompuLab Linux Kernel setup
* Clone the source tree and apply the CompuLab patches:
<pre>
git clone https://source.codeaurora.org/external/imx/linux-imx.git
git -C linux-imx checkout -b linux-compulab ${NXP_RELEASE}
git -C linux-imx am ${PATCHES}/*.patch
</pre>

## Initialize the Kernel configuration
<pre>
make -C linux-imx ${MACHINE}_defconfig
</pre>

## Customize the Kernel configuration
<pre>
make -C linux-imx menuconfig
</pre>

## Compile the Kernel
<pre>
make -C linux-imx
</pre>

