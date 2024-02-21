# Kernel Build Manual

## Prerequisites
It is up to developers to prepare the host machine; it requires:

* [Setup Cross Compiler](https://github.com/compulab-yokneam/meta-bsp-imx8mp/blob/kirkstone/Documentation/toolchain.md#linaro-toolchain-how-to)

## CompuLab Linux Kernel setup

* WorkDir:
```
mkdir -p compulab-kernel/build && cd compulab-kernel
```

* Set a CompuLab machine:

| Machine | Command Line |
|---|---|
|ucm-imx8m-plus|```export MACHINE=ucm-imx8m-plus```|
|mcm-imx8m-plus|```export MACHINE=mcm-imx8m-plus```|
|iot-gate-imx8plus|```export MACHINE=mcm-imx8m-plus```|

* Clone the source code:
```
git clone -b linux-compulab_v6.1.55 https://github.com/compulab-yokneam/linux-compulab.git
cd linux-compulab
```

## Compile the Kernel

* Apply the default CompuLab config:
```
make compulab_v8_defconfig compulab.config
```

* Ussue menuconfig on order to change the default CompuLab configuration:
```
make menuconfig
```

* Build the kernel
```
nice make -j`nproc`
```

* [Deploy the CompuLab Linux Kernel to CompuLab devices](https://github.com/compulab-yokneam/Documentation/blob/master/etc/linux_kernel_deployment.md#create-deb-package)
