# Yocto toolchain how to

## Yocto SDK

* Build the SDK
```
bitbake -c populate_sdk imx-image-full
```

* Install the SDK
```
sudo ${BUILDDIR}/tmp/deploy/sdk/fsl-imx-xwayland-glibc-x86_64-imx-image-full-armv8a-ucm-imx8m-plus-toolchain-5.15-kirkstone.sh
```

* Set the installation path or accept the default:
```
NXP i.MX Release Distro SDK installer version 5.15-kirkstone
============================================================
Enter target directory for SDK (default: /opt/fsl-imx-xwayland/5.15-kirkstone): 


NXP i.MX Release Distro SDK installer version 5.15-kirkstone
============================================================
Enter target directory for SDK (default: /opt/fsl-imx-xwayland/5.15-kirkstone): 
The directory "/opt/fsl-imx-xwayland/5.15-kirkstone" already contains a SDK for this architecture.
If you continue, existing files will be overwritten! Proceed [y/N]? y
```

* Wait for the `done` message:
```
Extracting SDK...
..............................................................................................................................................................................................................................................................................................................................................................................................
..............................................................................................................................................................................................................................................................................................................................................................................................
................................................................................................................................................................................................................done
Setting it up...done
SDK has been successfully set up and is ready to be used.
Each time you wish to use the SDK in a new shell session, you need to source the environment setup script e.g.
 $ . /opt/fsl-imx-xwayland/5.15-kirkstone/environment-setup-armv8a-poky-linux
```

* Setup the build environment
```
source /opt/fsl-imx-xwayland/5.15-kirkstone/environment-setup-armv8a-poky-linux
```

* Validate installed compiler location

```
which ${CROSS_COMPILE}gcc
/opt/fsl-imx-xwayland/5.15-kirkstone/sysroots/x86_64-pokysdk-linux/usr/bin/aarch64-poky-linux/aarch64-poky-linux-gcc
```

* Validate installed compiler version
```
${CROSS_COMPILE}gcc --version
aarch64-poky-linux-gcc (GCC) 11.3.0
Copyright (C) 2021 Free Software Foundation, Inc.
This is free software; see the source for copying conditions.  There is NO
warranty; not even for MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
```
