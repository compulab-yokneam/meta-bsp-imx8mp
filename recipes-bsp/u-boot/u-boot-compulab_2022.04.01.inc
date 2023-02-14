LICENSE = "GPL-2.0-or-later"
LIC_FILES_CHKSUM = "file://Licenses/gpl-2.0.txt;md5=b234ee4d69f5fce4486a80fdaf4a4263"

#SRC_URI = "git://github.com/nxp-imx/uboot-imx.git;protocol=https;branch=${SRCBRANCH}"
#SRCBRANCH = "lf_v2022.04"
#LOCALVERSION ?= "-clab_2022.04"
#SRCREV = "181859317bfafef1da79c59a4498650168ad9df6"

SRC_URI = "git://github.com/compulab-yokneam/u-boot-clab;protocol=https;branch=u-boot-clab-lf_v2022.04"
# Modify these as desired
SRCREV = "7117919c876c38fec0fb8a0e4a6257766c923a5a"

DEPENDS += " python3-setuptools-native "

COMPATIBLE_MACHINE = "(mx8mp-devel)"
