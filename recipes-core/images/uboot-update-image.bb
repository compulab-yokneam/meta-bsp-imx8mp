SUMMARY = "CompuLab U-Boot Update Image"
LICENSE = "MIT"

# Inherit the standard core-image class template
inherit core-image

# Tell Yocto to build a .wic image
IMAGE_FSTYPES = "wic"

# Point Yocto to your custom kickstart file
WKS_FILE = "uboot-update-image.wks.in"

IMAGE_ROOTFS_COMMAND = "true"
IMAGE_FEATURES = ""
IMAGE_LINGUAS = ""
IMAGE_INSTALL = ""

IMAGE_BOOT_FILES = "imx-boot.tagged;imx-boot-${MACHINE}-${DRAM_CONF} boot.update.scr;boot.scr"

DEPENDS:append = " u-boot-update-script "

IMAGE_LINK_NAME:append = "-${DRAM_CONF}"

IMAGE_NAME_SUFFIX = ""
