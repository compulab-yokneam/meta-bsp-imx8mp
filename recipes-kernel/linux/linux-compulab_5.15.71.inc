SRCBRANCH = "linux-compulab_v5.15.71-balena"
LOCALVERSION = "-clab-lts"
KERNEL_SRC ?= "git://github.com/compulab-yokneam/linux-compulab.git;protocol=https;branch=${SRCBRANCH}"
KBRANCH = "${SRCBRANCH}"
SRC_URI = "${KERNEL_SRC}"

SRCREV = "5c5d56afa7380b9c13a75f1054ccda7efea8ee7f"

# PV is defined in the base in linux-imx.inc file and uses the LINUX_VERSION definition
# required by kernel-yocto.bbclass.
#
# LINUX_VERSION define should match to the kernel version referenced by SRC_URI and
# should be updated once patchlevel is merged.
LINUX_VERSION = "5.15.71"
