# Yocto toolchain how to

## Get Yocto SDK

* Option #1<br>
  Download a ready to install SDK from this [location](https://drive.google.com/file/d/11ir2LRvftK9HAxzdwm6tvoVmv_Uz9tm_/view?usp=drive_link)
* Option #2<br>
  Build the SDK
```
bitbake -c populate_sdk imx-image-full
```

## Use Yocto SDK

* Install the SDK
```
sudo ${BUILDDIR}/tmp/deploy/sdk/fsl-imx-xwayland-glibc-x86_64-imx-image-full-armv8a-ucm-imx8m-plus-toolchain-6.1-mickledore.sh
```

* Set the installation path or accept the default:
```
NXP i.MX Release Distro SDK installer version 6.1-mickledore
============================================================
Enter target directory for SDK (default: /opt/fsl-imx-xwayland/6.1-mickledore):

============================================================
Enter target directory for SDK (default: /opt/fsl-imx-xwayland/6.1-mickledore): 
You are about to install the SDK to "/opt/fsl-imx-xwayland/6.1-mickledore". Proceed [Y/n]? y
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
 $ . /opt/fsl-imx-xwayland/6.1-mickledore/environment-setup-armv8a-poky-linux
```

* Setup the build environment
```
source /opt/fsl-imx-xwayland/6.1-mickledore/environment-setup-armv8a-poky-linux
```

* Validate installed compiler location

```
which ${CROSS_COMPILE}gcc
/opt/fsl-imx-xwayland/6.1-mickledore/sysroots/x86_64-pokysdk-linux/usr/bin/aarch64-poky-linux/aarch64-poky-linux-gcc
```

* Validate installed compiler version
```
${CROSS_COMPILE}gcc --version
aarch64-poky-linux-gcc (GCC) 12.3.0
Copyright (C) 2022 Free Software Foundation, Inc.
This is free software; see the source for copying conditions.  There is NO
warranty; not even for MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
```
