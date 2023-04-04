SRCBRANCH = "lf-5.10.y"
LOCALVERSION = "-1.0.0"
KERNEL_SRC ?= "git://github.com/nxp-imx/linux-imx.git;protocol=https"
SRC_URI = "${KERNEL_SRC};branch=${SRCBRANCH}"

SRCREV = "a68e31b63f864ff71cd4adb40fbc9e1edc75c250"

# PV is defined in the base in linux-imx.inc file and uses the LINUX_VERSION definition
# required by kernel-yocto.bbclass.
#
# LINUX_VERSION define should match to the kernel version referenced by SRC_URI and
# should be updated once patchlevel is merged.
LINUX_VERSION = "5.10.72"
