# Building Boot Firmware for CompuLab's i.MX8M Plus products

## Yocto devtool ##

* Get back to the build environment:
In order to use the already created build environment issue these commands:
```
cd /path/to/compulab-nxp-bsp
repo sync
source setup-environment build-${MACHINE}
```

* Get the latest u-boot source code:
```
devtool modify u-boot-compulab
```

* Goto the u-boot-compulab source tree:
```
cd ${BUILDDIR}/workspace/sources/u-boot-compulab
```

* Build the u-boot-compulab:
```
devtool build u-boot-compulab
```

* Make and commit the changes
* Apply changes from external source tree to recipe:
```
devtool update-recipe u-boot-compulab
```

* Remove the workspace layer:
```
 bitbake-layers remove-layer  ${BUILDDIR}/workspace
```

* Issue the entirer bootloader build:
```
bitbake -k imx-boot
```
