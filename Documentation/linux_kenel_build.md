# Kernel Build Manual

## External Build

### Prerequisites
It is up to developers to prepare the host machine; it requires:

* [Setup Cross Compiler](https://github.com/compulab-yokneam/meta-bsp-imx8mp/blob/kirkstone/Documentation/toolchain.md#linaro-toolchain-how-to)

### CompuLab Linux Kernel setup

* WorkDir:
```
mkdir -p compulab-kernel/build && cd compulab-kernel
```

* Set a CompuLab machine:

| Machine | Command Line |
|---|---|
|ucm-imx8m-plus|```export MACHINE=ucm-imx8m-plus```|
|mcm-imx8m-plus|```export MACHINE=mcm-imx8m-plus```|
|iot-gate-imx8plus|```export MACHINE=iot-gate-imx8plus```|

* Clone the source code:
```
git clone -b linux-compulab_v6.6.23 https://github.com/compulab-yokneam/linux-compulab.git
cd linux-compulab
```

### Compile the Kernel

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

* [Deploy the CompuLab Linux Kernel to CompuLab devices](https://github.com/compulab-yokneam/Documentation/blob/master/etc/linux_kernel_deployment.md)

## Internal Build

### Yocto devtool method

Use this method in order to modify and compile the linux-compulab kernel in the Yocto environment.<br>

* Get back to the build environment:<br>
In order to use the already created build environment issue these commands:
```
cd /path/to/compulab-nxp-bsp
repo sync
source setup-environment build-${MACHINE}
```

* Get the latest linux-compulab source code:
```
devtool modify linux-compulab
```

* Goto the linux-compulab source tree:
```
cd ${BUILDDIR}/workspace/sources/linux-compulab
```

* Build the linux-compulab:
```
devtool build linux-compulab
```

* Make and commit the changes
* Apply changes from external source tree to recipe:
```
devtool update-recipe linux-compulab
```

* Remove the workspace layer:
```
 bitbake-layers remove-layer  ${BUILDDIR}/workspace
```

* Issue the linux-compulab build using bitbake:
```
bitbake -k linux-compulab
```
