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

* Flash the image onto the sd-card:
```
sudo bmaptool copy imx-image-full-ucm-imx8m-plus.wic.bz2 --bmap imx-image-full-ucm-imx8m-plus.wic.bmap /dev/sdX
```

### Deployment the image onto the device eMMC:
#### An interactive mode:
* Insert the sd-card into the P23 slot.
* Turn on the device.
* When in Linux issue:<br>```cl-deploy```
#### A non interactive mode:
* Insert the sd-card into the P23 slot.
* Turn on the device, stop in U-Boot and type:<br>```setenv root_opt ${root_opt} init=usr/local/bin/cl-init```
* The installer will start running automatically:
```
 Autoinstaller
 ------------------------------------------------------------------------------


          +--------cl-deploy will get started in 5 seconds-----------+
          | Configuration file /etc/cl-auto.conf parameters:         |
          | --------------------------------------------------       |
          | # Destination is the module inthernal media.             |
          | # Autoinstaller how to:                                  |
          | ## cp /usr/share/cl-deploy/cl-auto.conf.sample           |
          | /etc/cl-auto.conf                                        |
          | ## cl-auto -A                                            |
          | DST=/dev/mmcblk2                                         |
          | QUIET=Yes                                                |
          | --------------------------------------------------       |
          | Press any key for terminating ...                        |
          +----------------------------------------------------------+
          |                 <Stop Auto Installer>                    |
          +----------------------------------------------------------+




which: no systemctl in ((null))

    Clonning Block Device
    Started: /dev/mmcblk1 ==> /dev/mmcblk2

Checking that no-one is using this disk right now ... OK

Disk /dev/mmcblk2: 29.12 GiB, 31268536320 bytes, 61071360 sectors
Units: sectors of 1 * 512 = 512 bytes
Sector size (logical/physical): 512 bytes / 512 bytes
I/O size (minimum/optimal): 512 bytes / 512 bytes
Disklabel type: dos
Disk identifier: 0x94e280c2

Old situation:

Device         Boot  Start      End  Sectors  Size Id Type
/dev/mmcblk2p1 *     16384   186775   170392 83.2M  c W95 FAT32 (LBA)
/dev/mmcblk2p2      196608 12799115 12602508    6G 83 Linux

>>> Script header accepted.
>>> Script header accepted.
>>> Script header accepted.
>>> Script header accepted.
>>> Script header accepted.
>>> Created a new DOS disklabel with disk identifier 0x94e280c2.
/dev/mmcblk2p1: Created a new partition 1 of type 'W95 FAT32 (LBA)' and of size 83.2 MiB.
Partition #1 contains a vfat signature.
/dev/mmcblk2p2: Created a new partition 2 of type 'Linux' and of size 6 GiB.
Partition #2 contains a ext4 signature.
/dev/mmcblk2p3: Done.

New situation:
Disklabel type: dos
Disk identifier: 0x94e280c2

Device         Boot  Start      End  Sectors  Size Id Type
/dev/mmcblk2p1 *     16384   186775   170392 83.2M  c W95 FAT32 (LBA)
/dev/mmcblk2p2      196608 12799115 12602508    6G 83 Linux

The partition table has been altered.
Calling ioctl() to re-read partition table.
Syncing disks.
Disk /dev/mmcblk2: 29.12 GiB, 31268536320 bytes, 61071360 sectors
Units: sectors of 1 * 512 = 512 bytes
Sector size (logical/physical): 512 bytes / 512 bytes
I/O size (minimum/optimal): 512 bytes / 512 bytes
Disklabel type: dos
Disk identifier: 0x94e280c2

Old situation:

Device         Boot  Start      End  Sectors  Size Id Type
/dev/mmcblk2p1 *     16384   186775   170392 83.2M  c W95 FAT32 (LBA)
/dev/mmcblk2p2      196608 12799115 12602508    6G 83 Linux

/dev/mmcblk2p2:
New situation:
Disklabel type: dos
Disk identifier: 0x94e280c2

Device         Boot  Start      End  Sectors  Size Id Type
/dev/mmcblk2p1 *     16384   186775   170392 83.2M  c W95 FAT32 (LBA)
/dev/mmcblk2p2      196608 61071359 60874752   29G 83 Linux

The partition table has been altered.
Calling ioctl() to re-read partition table.
Syncing disks.
mkfs.fat 4.1 (2017-01-24)
[ /dev/mmcblk1p1 => /dev/mmcblk2p1 ]: 30.3MiB 0:00:00 [55.5MiB/s] [<=>         ]
mke2fs 1.45.6 (20-Mar-2020)
/dev/mmcblk2p2 contains a ext4 file system labelled 'root'
        last mounted on / on Thu Jan  1 00:00:03 1970
Discarding device blocks: done
Creating filesystem with 7609344 4k blocks and 1905008 inodes
Filesystem UUID: 13229d46-e15f-4406-9996-120046836dec
Superblock backups stored on blocks:
        32768, 98304, 163840, 229376, 294912, 819200, 884736, 1605632, 2654208,
        4096000

Allocating group tables: done
Writing inode tables: done
Creating journal (32768 blocks): done
Writing superblocks and filesystem accounting information: done

[ /dev/mmcblk1p2 => /dev/mmcblk2p2 ]: 3.41GiB 0:01:36 [36.1MiB/s] [<=>         ]

    Done: /dev/mmcblk1 ==> /dev/mmcblk2

Sample /usr/share/cl-deploy/app/00_cl-deploy.app script
         #####  #     #  #####   #####  #######  #####   #####
        #     # #     # #     # #     # #       #     # #     #
        #       #     # #       #       #       #       #
         #####  #     # #       #       #####    #####   #####
              # #     # #       #       #             #       #
        #     # #     # #     # #     # #       #     # #     #
         #####   #####   #####   #####  #######  #####   #####
        Autoinstall mode has been disabled successfully.
        Init: /tmp/tmp.Fq418wPIJ8//sbin/init: symbolic link to ../lib/systemd/systemd
        Rebooting in 3 seconds...
```

# UUU
Prerquirements:
* Refer to the [`mfgtools`](https://github.com/NXPmicro/mfgtools) for details about the tool and install it.
* Connect the device to a Linux desktop using a TypeC (USB1) port.
* On the Linux machine open up a terminal window and type:<br>```udevadm monitor```
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
