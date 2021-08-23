# Configuring the build

## Setup Yocto environment

* WorkDir
```
mkdir compulab-bsp && cd compulab-bsp
```
* Set a CompuLab machine:
```
export MACHINE=ucm-imx8m-plus
```
* Initialize repo manifests
```
repo init -u https://source.codeaurora.org/external/imx/imx-manifest -b imx-linux-gatesgarth -m imx-5.10.9-1.0.0.xml
wget --directory-prefix .repo/manifests https://raw.githubusercontent.com/compulab-yokneam/meta-bsp-imx8mp/gatesgarth/scripts/imx-5.10.9-1.0.0_compulab.xml
repo init -m imx-5.10.9-1.0.0_compulab.xml
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

# UUU
Prerquirements:
* Refer to the [`mfgtools`](https://github.com/NXPmicro/mfgtools) for details about the tool and install it.
* Connect the device to a Linux desktop using a TypeC (USB1) port. On the Linux machine open up a terminal window and type:<br>```udevadm monitor```
* Turn on the device, stop in U-Boot and issue:<br>```fastboot 0```
* The Linux machine teminal must show this up:<pre>
monitor will print the received events for:
UDEV - the event which udev sends out after rule processing
KERNEL - the kernel uevent
KERNEL[29006471.097823] add      /devices/pci0000:00/0000:00:14.0/usb1/1-8 (usb)
KERNEL[29006471.098557] add      /devices/pci0000:00/0000:00:14.0/usb1/1-8/1-8:1.0 (usb)
UDEV  [29006471.108663] add      /devices/pci0000:00/0000:00:14.0/usb1/1-8 (usb)
UDEV  [29006471.111241] add      /devices/pci0000:00/0000:00:14.0/usb1/1-8/1-8:1.0 (usb)

Goto ${DEPLOYDIR} first:
```
cd ${BUILDDIR}/tmp/deploy/images/${MACHINE}
```

## Burn bootloader into emmc
```
sudo uuu -v -b emmc imx-boot-ucm-imx8m-plus-sd.bin-flash_evk
```

## Burn rootfs image into emmc
```bash
sudo uuu -v -b emmc_all imx-boot-ucm-imx8m-plus-sd.bin-flash_evk imx-image-full-ucm-imx8m-plus-20210810135453.rootfs.wic.bz2
```

# SDP
The device gets into this mode if the default boot device does not have a valid bootloader.

## Boot the device using the SDP
The simpest way to make the device get into this mode is to issue AltBoot w/out sd-card in the P23 slot.<br>
How to proceed:
* Turn on the device, stop in U-Boot.
* On the Linux machine open up a terminal window and type:<br>```udevadm monitor```
* On the device:
  * press and hold `ALT_BOOT`
  * toggle `RESET`
  * relase `ALT_BOOT`
* The Linux machine teminal must show this up:<pre>
(ins)# udevadm monitor 
monitor will print the received events for:
UDEV - the event which udev sends out after rule processing
KERNEL - the kernel uevent
KERNEL[29005568.006052] add      /devices/pci0000:00/0000:00:14.0/usb1/1-8 (usb)
KERNEL[29005568.006786] add      /devices/pci0000:00/0000:00:14.0/usb1/1-8/1-8:1.0 (usb)
KERNEL[29005568.007427] add      /devices/pci0000:00/0000:00:14.0/usb1/1-8/1-8:1.0/0003:1FC9:0146.158A (hid)
KERNEL[29005568.007887] add      /class/usbmisc (class)
KERNEL[29005568.007967] add      /devices/pci0000:00/0000:00:14.0/usb1/1-8/1-8:1.0/usbmisc/hiddev0 (usbmisc)
KERNEL[29005568.008199] add      /devices/pci0000:00/0000:00:14.0/usb1/1-8/1-8:1.0/0003:1FC9:0146.158A/hidraw/hidraw0 (hidraw)
UDEV  [29005568.010271] add      /class/usbmisc (class)
UDEV  [29005568.016915] add      /devices/pci0000:00/0000:00:14.0/usb1/1-8 (usb)
UDEV  [29005568.019897] add      /devices/pci0000:00/0000:00:14.0/usb1/1-8/1-8:1.0 (usb)
UDEV  [29005568.022544] add      /devices/pci0000:00/0000:00:14.0/usb1/1-8/1-8:1.0/usbmisc/hiddev0 (usbmisc)
UDEV  [29005568.022577] add      /devices/pci0000:00/0000:00:14.0/usb1/1-8/1-8:1.0/0003:1FC9:0146.158A (hid)
UDEV  [29005568.023299] add      /devices/pci0000:00/0000:00:14.0/usb1/1-8/1-8:1.0/0003:1FC9:0146.158A/hidraw/hidraw0 (hidraw)

## Download the bootloader
```
sudo uuu -v imx-boot-ucm-imx8m-plus-sd.bin-flash_evk
```
