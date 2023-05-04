## Developmet

* Build the SDK
```
bitbake -c populate_sdk compulab-qt6-build-env
```

* Install the SDK
```
sudo ${BUILDDIR}/tmp/deploy/sdk/fsl-imx-xwayland-glibc-x86_64-compulab-qt6-build-env-armv8a-ucm-imx8m-plus-toolchain-5.15-kirkstone.sh
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

* Make use of the build environment
```
. /opt/fsl-imx-xwayland/5.15-kirkstone/environment-setup-armv8a-poky-linux
```
