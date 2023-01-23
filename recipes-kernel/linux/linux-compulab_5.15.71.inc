SRCBRANCH = "lf-5.15.y"
LOCALVERSION = "-clab-lts"
KERNEL_SRC ?= "git://github.com/nxp-imx/linux-imx.git;protocol=https;branch=${SRCBRANCH}"
KBRANCH = "${SRCBRANCH}"
SRC_URI = "${KERNEL_SRC}"

SRCREV = "3313732e9984cb8a6b10a9085c7e18d58e770d56"

# PV is defined in the base in linux-imx.inc file and uses the LINUX_VERSION definition
# required by kernel-yocto.bbclass.
#
# LINUX_VERSION define should match to the kernel version referenced by SRC_URI and
# should be updated once patchlevel is merged.
LINUX_VERSION = "5.15.71"
